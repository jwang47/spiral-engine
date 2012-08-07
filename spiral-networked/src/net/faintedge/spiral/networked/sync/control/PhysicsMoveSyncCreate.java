package net.faintedge.spiral.networked.sync.control;

import net.faintedge.spiral.physics.PhysicsControl;
import net.faintedge.spiral.physics.control.DebugPhysicsMoveControl;
import net.faintedge.spiral.physics.control.PhysicsMoveControl;


public class PhysicsMoveSyncCreate extends ControlSyncCreate {
  
  private float moveSpeed;

  public float getMoveSpeed() {
    return moveSpeed;
  }

  public void setMoveSpeed(float moveSpeed) {
    this.moveSpeed = moveSpeed;
  }

  public static PhysicsMoveControl create(PhysicsMoveSyncCreate moveCreate, PhysicsControl physics) {
    return new PhysicsMoveControl(physics, moveCreate.getMoveSpeed());
  }
  
  public static PhysicsMoveControl createDebug(PhysicsMoveSyncCreate moveCreate, PhysicsControl physics) {
    return new DebugPhysicsMoveControl(physics, moveCreate.getMoveSpeed());
  }

  public static PhysicsMoveSyncCreate fromMoveControl(PhysicsMoveControl moveControl, short parentSyncObjectId) {
    PhysicsMoveSyncCreate msg = new PhysicsMoveSyncCreate();
    msg.setParentSyncObjectId(parentSyncObjectId);
    msg.moveSpeed = moveControl.getSpeed();
    return msg;
  }

}
