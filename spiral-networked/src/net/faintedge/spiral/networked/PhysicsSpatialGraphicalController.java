package net.faintedge.spiral.networked;

import net.faintedge.spiral.core.Spatial;
import net.faintedge.spiral.physics.Operation;
import net.faintedge.spiral.physics.PhysicsControl;
import net.faintedge.spiral.physics.PhysicsSystem;
import net.faintedge.spiral.physics.control.PhysicsMoveControl;

import org.jbox2d.common.Vec2;

import com.captiveimagination.jgn.synchronization.GraphicalController;
import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeRemoveMessage;

public class PhysicsSpatialGraphicalController implements GraphicalController<Spatial> {

  private Vec2 position = new Vec2();
  private Vec2 velocity = new Vec2();
  private PhysicsSystem system;
  
  public PhysicsSpatialGraphicalController(PhysicsSystem system) {
    this.system = system;
  }
  
  @Override
  public void applySynchronizationMessage(final SynchronizeMessage msg, final Spatial spatial) {
    final PhysicsSpatialSync sync = (PhysicsSpatialSync) msg;
    final PhysicsControl physics = (PhysicsControl) spatial.getControl(PhysicsControl.class);
    final PhysicsMoveControl move = (PhysicsMoveControl) spatial.getControl(PhysicsMoveControl.class);
    if (physics.getBody() != null) {
      system.enqueuePreUpdateOperation(new Operation() {
        @Override
        public void execute() {
          position.set(sync.getX(), sync.getY());
          velocity.set(sync.getVx(), sync.getVy());
          physics.getBody().setTransform(position, sync.getRotation());
          physics.getBody().setLinearVelocity(velocity);
        }
      });
    }
    if (move != null) {
      move.setDirection(sync.getDirection());
    }
  }

  @Override
  public SynchronizeMessage createSynchronizationMessage(final Spatial spatial) {
    final PhysicsControl physics = (PhysicsControl) spatial.getControl(PhysicsControl.class);
    final PhysicsMoveControl move = (PhysicsMoveControl) spatial.getControl(PhysicsMoveControl.class);
    PhysicsSpatialSync sync = new PhysicsSpatialSync();
    
    if (physics.getBody() != null) {
      Vec2 position = physics.getBody().getPosition();
      Vec2 velocity = physics.getBody().getLinearVelocity();
      float rotation = physics.getBody().getAngle();
      
      sync.setX(position.x);
      sync.setY(position.y);
      sync.setVx(velocity.x);
      sync.setVy(velocity.y);
      sync.setRotation(rotation);
    }
    
    if (move != null) {
      sync.setMoveInfo(move.getDirection());
    }
    return sync;
  }

  @Override
  public float proximity(Spatial spatial, short playerId) {
    return 1.0f;
  }

  @Override
  public boolean validateCreate(SynchronizeCreateMessage msg) {
    return true;
  }

  @Override
  public boolean validateMessage(SynchronizeMessage msg, Spatial spatial) {
    return true;
  }

  @Override
  public boolean validateRemove(SynchronizeRemoveMessage message) {
    return true;
  }

}
