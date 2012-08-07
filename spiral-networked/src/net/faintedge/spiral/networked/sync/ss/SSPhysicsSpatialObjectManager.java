package net.faintedge.spiral.networked.sync.ss;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.faintedge.spiral.core.Spatial;
import net.faintedge.spiral.networked.PhysicsSpatialSyncCreate;
import net.faintedge.spiral.networked.sync.control.PhysicsMoveSyncCreate;
import net.faintedge.spiral.physics.PhysicsControl;
import net.faintedge.spiral.physics.PhysicsSystem;
import net.faintedge.spiral.physics.control.DebugPhysicsMoveControl;
import net.faintedge.spiral.physics.control.PhysicsMoveControl;
import net.faintedge.spiral.spatial.Node;
import net.faintedge.spiral.spatial.Text;
import net.faintedge.spiral.spatial.control.FollowControl;

import org.newdawn.slick.Color;

import com.captiveimagination.jgn.event.MessageListener;
import com.captiveimagination.jgn.message.Message;
import com.captiveimagination.jgn.synchronization.SyncObjectManager;
import com.captiveimagination.jgn.synchronization.SynchronizationManager;
import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeRemoveMessage;
import common.Mode;


public class SSPhysicsSpatialObjectManager implements SyncObjectManager, MessageListener {

  // debug
  public static int timesCalledRegisterSpatial = 0;
  public static int timesCalledCreate = 0;
  
  // constants
  public static final long SPATIAL_UPDATE_DELAY = 55;
  private static final Logger logger = Logger.getLogger(SSPhysicsSpatialObjectManager.class.getName());
  
  private Node rootNode;
  private PhysicsSystem system;
  private int controlledRequestId = -1;
  private Mode mode;
  private float ptm;

  private SynchronizationManager physicsSpatialSyncManager;
  
  private Map<Short, Spatial> debugSpatials = new HashMap<Short, Spatial>();
  private LinkedList<OnCreateListener> onCreateListeners = new LinkedList<OnCreateListener>();

  public SSPhysicsSpatialObjectManager(Mode mode, Node rootNode, PhysicsSystem system, float ptm) {
    this.mode = mode;
    this.rootNode = rootNode;
    this.system = system;
    this.ptm = ptm;
  }

  /**
   * Redirects register requests to a SyncManager
   * @param object
   * @param create
   * @param delay
   * @throws IOException 
   */
  public void register(Object object, SynchronizeCreateMessage create) throws IOException {
    if (create instanceof PhysicsSpatialSyncCreate) {
      timesCalledRegisterSpatial++;
      Spatial spatial = (Spatial) object;
      physicsSpatialSyncManager.register(object, create, SPATIAL_UPDATE_DELAY);
      logger.log(Level.INFO, "registered physics spatial (" + spatial.getName() + ") with obj id " + physicsSpatialSyncManager.findSyncObjectId(object));
    } else {
      throw new UnsupportedOperationException();
    }
  }

  @Override
  public Object create(SynchronizeCreateMessage msg) {
    timesCalledCreate++;
    Spatial spatial = null;
    if (msg instanceof PhysicsSpatialSyncCreate) {
      logger.log(Level.INFO, "received spatial sync create with object id " + msg.getSyncObjectId() + ", " + msg.getSyncManagerId());
      if (SSCache.spatialsBySyncId.get(msg.getSyncObjectId()) != null) {
        logger.log(Level.SEVERE, "received spatial sync create for existing obj id " + msg.getSyncObjectId());
      }
      PhysicsSpatialSyncCreate spatialCreate = (PhysicsSpatialSyncCreate) msg;
      boolean localControlled = spatialCreate.isClientControlled() && controlledRequestId == spatialCreate.getRequestId();
      spatial = PhysicsSpatialSyncCreate.createPhysicsSpatial(spatialCreate, !localControlled, system, ptm);
      PhysicsControl physics = (PhysicsControl) spatial.getControl(PhysicsControl.class);
      if (localControlled) {
        PhysicsMoveControl localControl = new DebugPhysicsMoveControl(physics, spatialCreate.getMoveSpeed());
        spatial.addControl(localControl);
      }
      if (spatial.getControl(PhysicsMoveControl.class) == null) {
        throw new RuntimeException("fail");
      }
    } else {
      throw new UnsupportedOperationException("in create");
    }
    
    notifyOnCreate(msg, spatial);
    this.addSpatial(spatial, msg.getPlayerId(), msg.getSyncObjectId());
    return spatial;
  }

  private void notifyOnCreate(SynchronizeCreateMessage msg, Spatial spatial) {
    for (OnCreateListener listener : onCreateListeners ) {
      listener.onCreate(msg, spatial);
    }
  }

  public void addOnCreateListener(OnCreateListener listener) {
    onCreateListeners.add(listener);
  }
  
  @Override
  public boolean remove(SynchronizeRemoveMessage msg, Object object) {
    if (object instanceof Spatial) {
      Spatial spatial = (Spatial) object;
      debugSpatials.remove(msg.getSyncObjectId());
      rootNode.remove(spatial);
      return true;
    }
    throw new UnsupportedOperationException();
  }

  private void addSpatial(Spatial spatial, short playerId, short syncObjectId) {
    // add a debug text thingy
    Text debugText = new Text("syncid:" + syncObjectId, Color.white);
    debugText.setName("debug text for " + spatial.getName());
    debugText.addControl(new FollowControl(spatial));
    debugSpatials.put(syncObjectId, debugText);
    rootNode.add(debugText);
    
    rootNode.add(spatial);
    SSCache.spatialsBySyncId.put(syncObjectId, spatial);
    if (SSCache.spatialsByPlayerId.get(playerId) == null) {
      SSCache.spatialsByPlayerId.put(playerId, new HashMap<Short, Spatial>());
    }
    SSCache.spatialsByPlayerId.get(playerId).put(syncObjectId, spatial);
  }

  /**
   * Register a spatial authoritative of this server from a client request
   * @param msg
   */
  private void registerSpatialForRemote(PhysicsSpatialSyncCreate msg) {
    short playerId = msg.getPlayerId();
    Spatial spatial = PhysicsSpatialSyncCreate.createPhysicsSpatial(msg, true, system, ptm);
    try {
      // now add
      this.register(spatial, msg);
      short syncObjectId = physicsSpatialSyncManager.findSyncObjectId(spatial);
      
      // send reply to client if they wanted to control it
      if (msg.isClientControlled()) {
        PhysicsMoveSyncCreate moveCreate = new PhysicsMoveSyncCreate();
        moveCreate.setParentSyncObjectId(syncObjectId);
        
        RegisterSpatialReply reply = new RegisterSpatialReply();
        reply.setRequestId(msg.getRequestId());
        reply.setMoveCreate(moveCreate);
        reply.setSyncObjectId(syncObjectId);
        msg.getMessageClient().sendMessage(reply);
        logger.log(Level.INFO, "sent register spatial reply to remote player " + playerId);
      }
      this.addSpatial(spatial, playerId, syncObjectId);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void messageReceived(Message message) {
    short playerId = message.getPlayerId();
    if (message instanceof RegisterSpatialRequest) {
      RegisterSpatialRequest msg = (RegisterSpatialRequest) message;
      msg.getCreate().setClientControlled(msg.isClientControlled());
      msg.getCreate().setRequestId(msg.getRequestId());
      msg.getCreate().setPlayerId(playerId);
      msg.getCreate().setMessageClient(message.getMessageClient());
      registerSpatialForRemote(msg.getCreate());
    }
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

  public int getControlledRequestId() {
    return controlledRequestId;
  }

  public void setControlledRequestId(int controlledRequestId) {
    this.controlledRequestId = controlledRequestId;
  }

  public void setSpatialSyncManager(SynchronizationManager physicsSpatialSyncManager) {
    this.physicsSpatialSyncManager = physicsSpatialSyncManager;
  }
  
}