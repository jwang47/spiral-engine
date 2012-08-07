package net.faintedge.spiral.networked;

import net.faintedge.spiral.networked.sync.control.PhysicsMoveSync;
import net.faintedge.spiral.physics.control.PhysicsMoveControl;

import com.captiveimagination.jgn.synchronization.GraphicalController;
import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeRemoveMessage;

public class PhysicsMoveGraphicalController implements GraphicalController<PhysicsMoveControl>{

  @Override
  public float proximity(PhysicsMoveControl object, short playerId) {
    return 1;
  }

  @Override
  public SynchronizeMessage createSynchronizationMessage(PhysicsMoveControl object) {
    PhysicsMoveSync msg = new PhysicsMoveSync();
    msg.setValues(object.isNorth(), object.isSouth(), object.isWest(), object.isEast());
    return msg;
  }

  @Override
  public void applySynchronizationMessage(SynchronizeMessage message, PhysicsMoveControl object) {
    PhysicsMoveSync msg = (PhysicsMoveSync) message;
    object.setValues(msg.isNorth(), msg.isSouth(), msg.isWest(), msg.isEast());
  }

  @Override
  public boolean validateMessage(SynchronizeMessage message, PhysicsMoveControl object) {
    return true;
  }

  @Override
  public boolean validateCreate(SynchronizeCreateMessage message) {
    return true;
  }

  @Override
  public boolean validateRemove(SynchronizeRemoveMessage message) {
    return true;
  }

}
