package net.faintedge.spiral.networked.sync.ss.node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.faintedge.spiral.core.Spatial;
import net.faintedge.spiral.networked.Net;
import net.faintedge.spiral.networked.PhysicsMoveGraphicalController;
import net.faintedge.spiral.networked.PhysicsSpatialGraphicalController;
import net.faintedge.spiral.networked.PhysicsSpatialSyncCreate;
import net.faintedge.spiral.networked.sync.ss.OnCreateListener;
import net.faintedge.spiral.networked.sync.ss.RegisterSpatialReply;
import net.faintedge.spiral.networked.sync.ss.RegisterSpatialRequest;
import net.faintedge.spiral.networked.sync.ss.SSPhysicsMoveObjectManager;
import net.faintedge.spiral.networked.sync.ss.SSPhysicsSpatialObjectManager;
import net.faintedge.spiral.physics.PhysicsSystem;
import net.faintedge.spiral.physics.control.PhysicsMoveControl;
import net.faintedge.spiral.spatial.Node;

import com.captiveimagination.jgn.JGN;
import com.captiveimagination.jgn.clientserver.JGNClient;
import com.captiveimagination.jgn.event.MessageListener;
import com.captiveimagination.jgn.message.Message;
import com.captiveimagination.jgn.synchronization.SynchronizationManager;
import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;

import common.Mode;

public class SSPhysicsClientNode extends Node implements MessageListener, OnCreateListener {
  
  private static final Logger logger = Logger.getLogger(SSPhysicsClientNode.class.getName());
  private static final Mode MODE = Mode.CLIENT;
  
  private JGNClient client;
  private SSPhysicsSpatialObjectManager spatialObjectManager;
  private SSPhysicsMoveObjectManager moveObjectManager;
  private SynchronizationManager spatialSyncManager;
  private SynchronizationManager moveSyncManager;

  private Spatial controlledSpatial;
  private int controlledSpatialReqId;
  private PhysicsSystem physics;
  private float ptm;
  private Object controlledSpatialMessage;
  private Map<Integer, OnCreateListener> callbacks = new HashMap<Integer, OnCreateListener>();
  private int nextRequestId = 0;
  
  public SSPhysicsClientNode(String name, PhysicsSystem physics, float ptm) {
    super(name);
    this.physics = physics;
    this.ptm = ptm;
  }
  
  public void connect(InetAddress address, int tcp, int udp) throws IOException, InterruptedException {
    Net.init();
    
    InetSocketAddress serverReliable = new InetSocketAddress(address, tcp);
    InetSocketAddress serverFast = new InetSocketAddress(address, udp);
    client = new JGNClient(new InetSocketAddress(InetAddress.getLocalHost(), 0), new InetSocketAddress(InetAddress.getLocalHost(), 0));
    // this must be the first message listener for client, or else things will go badly
    // (in other words, SSPhysicsMoveObjectManager.messageReceived will fire before this.messageReceived
    // and not send the callback (which will usually add a move control) before SSPhysicsMoveObjectManager
    // looks to sync the move control)
    client.addMessageListener(this);
    
    PhysicsSpatialGraphicalController physicsSpatialController = createPhysicsSpatialController(physics);
    spatialSyncManager = new SynchronizationManager(client, physicsSpatialController, (short) 2);
    
    PhysicsMoveGraphicalController physicsMoveController = createPhysicsMoveController();
    moveSyncManager = new SynchronizationManager(client, physicsMoveController, (short) 3);
    
    spatialObjectManager = createPhysicsSpatialObjectManager(MODE, physics);
    spatialObjectManager.setSpatialSyncManager(spatialSyncManager);
    spatialSyncManager.addSyncObjectManager(spatialObjectManager);
    client.addMessageListener(spatialObjectManager);
    spatialObjectManager.addOnCreateListener(this);
    
    moveObjectManager = createPhysicsMoveObjectManager(MODE);
    moveObjectManager.setMoveSyncManager(moveSyncManager);
    moveSyncManager.addSyncObjectManager(moveObjectManager);
    client.addMessageListener(moveObjectManager);
    
    JGN.createThread(client, spatialSyncManager, moveSyncManager).start();
    client.connectAndWait(serverReliable, serverFast, 1000);
  }

  @Override
  protected void doRemove(Spatial spatial) {
    super.doRemove(spatial);
    // only stuff that the client removes is its own move control
    if (spatial == controlledSpatial) {
      PhysicsMoveControl moveControl = (PhysicsMoveControl) spatial.getControl(PhysicsMoveControl.class);
      moveSyncManager.unregister(moveControl);
    }
  }

  @Override
  public void add(Spatial spatial) {
    logger.info("Added spatial " + spatial.getName());
    super.add(spatial);
  }

  public int addForSync(PhysicsSpatialSyncCreate create, boolean controlled, OnCreateListener callback) {
    int requestId = nextRequestId++;
    this.callbacks.put(requestId, callback);
    
    if (controlled) {
      if (controlledSpatialMessage != null) {
        throw new RuntimeException("There can only be one! controlledSpatial");
      }
      controlledSpatialMessage = create;
    }
    
    // send request to server
    // SSPhysicsSpatialObjectManager will add the actual spatial when it's registered
    RegisterSpatialRequest request = new RegisterSpatialRequest();
    logger.log(Level.INFO, "sending request to register client spatial");
    request.setCreate(create);
    if (controlled) {
      controlledSpatialReqId = requestId;
      spatialObjectManager.setControlledRequestId(controlledSpatialReqId);
      request.setRequestId(controlledSpatialReqId);
      request.setClientControlled(true);
    }
    client.sendToServer(request);
    return requestId;
  }

  @Override
  public void onCreate(SynchronizeCreateMessage message, Object object) {
    if (message instanceof PhysicsSpatialSyncCreate) {
      PhysicsSpatialSyncCreate msg = (PhysicsSpatialSyncCreate) message;
      OnCreateListener callback = callbacks.get(msg.getRequestId());
      if (callback != null) {
        callback.onCreate(message, object);
      }
    }
  }

  @Override
  public void messageReceived(Message message) {
    
  }

  @Override
  public void messageSent(Message message) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void messageCertified(Message message) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void messageFailed(Message message) {
    // TODO Auto-generated method stub
    
  }

  protected PhysicsSpatialGraphicalController createPhysicsSpatialController(PhysicsSystem physics) {
    return new PhysicsSpatialGraphicalController(physics);
  }

  protected PhysicsMoveGraphicalController createPhysicsMoveController() {
    return new PhysicsMoveGraphicalController();
  }

  protected SSPhysicsSpatialObjectManager createPhysicsSpatialObjectManager(Mode mode, PhysicsSystem physics) {
    return new SSPhysicsSpatialObjectManager(mode, this, physics, ptm);
  }

  protected SSPhysicsMoveObjectManager createPhysicsMoveObjectManager(Mode mode) {
    return new SSPhysicsMoveObjectManager(mode);
  }

}
