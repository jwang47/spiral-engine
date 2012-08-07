package net.faintedge.spiral.networked.sync.ss;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.faintedge.spiral.core.Spatial;
import net.faintedge.spiral.networked.sync.control.PhysicsMoveSyncCreate;
import net.faintedge.spiral.physics.control.PhysicsMoveControl;

import com.captiveimagination.jgn.event.MessageListener;
import com.captiveimagination.jgn.message.Message;
import com.captiveimagination.jgn.synchronization.SyncObjectManager;
import com.captiveimagination.jgn.synchronization.SynchronizationManager;
import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeRemoveMessage;
import common.Mode;

public class SSPhysicsMoveObjectManager implements SyncObjectManager, MessageListener {

  public static int timesCalledRegisterMove = 0;
  public static final long MOVE_CONTROL_UPDATE_DELAY = 55;
  private static final Logger logger = Logger.getLogger(SSPhysicsMoveObjectManager.class.getName());
  
  private SynchronizationManager physicsMoveSyncManager;
  private Mode mode;
  
  public SSPhysicsMoveObjectManager(Mode mode) {
    this.mode = mode;
  }

  public void register(PhysicsMoveControl object, PhysicsMoveSyncCreate create) throws IOException {
    timesCalledRegisterMove++;
    physicsMoveSyncManager.register(object, create, MOVE_CONTROL_UPDATE_DELAY);
    PhysicsMoveSyncCreate msg = (PhysicsMoveSyncCreate) create;
    Spatial spatial = SSCache.spatialsBySyncId.get(msg.getParentSyncObjectId());
    while (physicsMoveSyncManager.findSyncObjectId(object) == -1) {
      Thread.yield();
    }
    logger.log(Level.INFO, "registered physics move (" + spatial.getName() + ") with obj id " + physicsMoveSyncManager.findSyncObjectId(object));
  }

  @Override
  public Object create(SynchronizeCreateMessage scm) {
    if (scm instanceof PhysicsMoveSyncCreate) {
      PhysicsMoveSyncCreate m = (PhysicsMoveSyncCreate) scm;
      logger.log(Level.INFO, "move sync create for parent obj id " + m.getParentSyncObjectId());
      // server should receive this
      // don't create anything new because the PhysicsMoveControl should've already been created in create()->PhysicsSpatialSyncCreate
      Spatial spatial = SSCache.spatialsBySyncId.get(m.getParentSyncObjectId());
      if (spatial == null) {
        throw new RuntimeException("fail");
      }
      return spatial.getControl(PhysicsMoveControl.class);
    } else {
      throw new UnsupportedOperationException();
    }
  }

  @Override
  public boolean remove(SynchronizeRemoveMessage srm, Object obj) {
    return true;
  }

  @Override
  public void messageReceived(Message message) {
    if (message instanceof RegisterSpatialReply) {
      logger.log(Level.INFO, "received register spatial reply");
      final RegisterSpatialReply msg = (RegisterSpatialReply) message;
      logger.log(Level.INFO, "queued register reply for processing");
      new Thread() {
        public void run() {
          while (SSCache.spatialsBySyncId.get(msg.getSyncObjectId()) == null) {
            Thread.yield();
          }
          logger.log(Level.INFO, "register local move control! for obj id " + msg.getSyncObjectId());
          // register the move control now!
          Spatial spatial = SSCache.spatialsBySyncId.get(msg.getSyncObjectId());
          PhysicsMoveControl moveControl = (PhysicsMoveControl) spatial.getControl(PhysicsMoveControl.class);
          if (moveControl == null) {
            throw new RuntimeException("move control was null");
          }
          try {
            register(moveControl, PhysicsMoveSyncCreate.fromMoveControl(moveControl, msg.getSyncObjectId()));
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }.start();
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

  public SynchronizationManager getPhysicsMoveSyncManager() {
    return physicsMoveSyncManager;
  }

  public void setMoveSyncManager(SynchronizationManager physicsMoveSyncManager) {
    this.physicsMoveSyncManager = physicsMoveSyncManager;
  }

}
