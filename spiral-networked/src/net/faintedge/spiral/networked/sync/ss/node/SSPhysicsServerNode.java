package net.faintedge.spiral.networked.sync.ss.node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.faintedge.spiral.core.Spatial;
import net.faintedge.spiral.networked.Net;
import net.faintedge.spiral.networked.PhysicsMoveGraphicalController;
import net.faintedge.spiral.networked.PhysicsSpatialGraphicalController;
import net.faintedge.spiral.networked.PhysicsSpatialSyncCreate;
import net.faintedge.spiral.networked.sync.ss.SSPhysicsMoveObjectManager;
import net.faintedge.spiral.networked.sync.ss.SSPhysicsSpatialObjectManager;
import net.faintedge.spiral.physics.PhysicsSystem;
import net.faintedge.spiral.spatial.Node;

import com.captiveimagination.jgn.JGN;
import com.captiveimagination.jgn.clientserver.JGNServer;
import com.captiveimagination.jgn.synchronization.SynchronizationManager;
import common.Mode;

public class SSPhysicsServerNode extends Node {

  private static final long SPATIAL_UPDATE_DELAY = 50;
  private static final Mode MODE = Mode.SERVER;
  
  private JGNServer server;
  private SynchronizationManager spatialSyncManager;
  private SynchronizationManager moveSyncManager;
  private SSPhysicsSpatialObjectManager spatialObjectManager;
  private SSPhysicsMoveObjectManager moveObjectManager;
  
  private Queue<Spatial> spatialsForSync = new ConcurrentLinkedQueue<Spatial>();
  private PhysicsSystem physics;
  private float ptm;
  
  public SSPhysicsServerNode(String name, PhysicsSystem physics, float ptm) {
    super(name);
    this.physics = physics;
    this.ptm = ptm;
  }
  
  public void listen(int tcp, int udp) throws IOException {
    Net.init();

    InetSocketAddress serverReliable = new InetSocketAddress(InetAddress.getLocalHost(), tcp);
    InetSocketAddress serverFast = new InetSocketAddress(InetAddress.getLocalHost(), udp);
    
    server = new JGNServer(serverReliable, serverFast);

    PhysicsSpatialGraphicalController physicsSpatialController = createPhysicsSpatialController(physics);
    spatialSyncManager = new SynchronizationManager(server, physicsSpatialController, (short) 2);
    
    PhysicsMoveGraphicalController physicsMoveController = createPhysicsMoveController();
    moveSyncManager = new SynchronizationManager(server, physicsMoveController, (short) 3);
    
    spatialObjectManager = createPhysicsSpatialObjectManager(MODE, physics);
    spatialObjectManager.setSpatialSyncManager(spatialSyncManager);
    spatialSyncManager.addSyncObjectManager(spatialObjectManager);
    server.addMessageListener(spatialObjectManager);
    
    moveObjectManager = createPhysicsMoveObjectManager(MODE);
    moveObjectManager.setMoveSyncManager(moveSyncManager);
    moveSyncManager.addSyncObjectManager(moveObjectManager);
    server.addMessageListener(moveObjectManager);
    
    JGN.createThread(server, spatialSyncManager, moveSyncManager).start();
  }

  @Override
  protected void doRemove(Spatial spatial) {
    super.doRemove(spatial);
  }

  @Override
  protected void doAdd(Spatial spatial) {
    super.doAdd(spatial);
    
    try {
      if (spatialsForSync.remove(spatial)) {
        spatialSyncManager.register(spatial, PhysicsSpatialSyncCreate.fromSpatial(spatial), SPATIAL_UPDATE_DELAY);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void addForSync(Spatial spatial) {
    spatialsForSync.add(spatial);
    super.add(spatial);
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
