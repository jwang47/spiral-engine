package net.faintedge.spiral.networked.handlers.physics;

import net.faintedge.spiral.networked.SyncHandler;
import net.faintedge.spiral.networked.msg.SyncCreate;
import net.faintedge.spiral.networked.msg.SyncUpdate;
import net.faintedge.spiral.physics.Physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class PhysicsSyncHandler extends SyncHandler<Physics> {
  
  @Override
  public SyncCreate<Physics> makeCreateMessage(Physics object) {
    return PhysicsSyncCreate.toMessage(object);
  }

  @Override
  public SyncUpdate<Physics> makeUpdateMessage(Physics object) {
    Body body = object.getBody();
    PhysicsSyncUpdate update = new PhysicsSyncUpdate();
    if (body != null) {
      update.setX(body.getPosition().x);
      update.setY(body.getPosition().y);
      update.setVx(body.getLinearVelocity().x);
      update.setVx(body.getLinearVelocity().y);
      update.setRotation(body.getAngle());
    }
    return update;
  }

  @Override
  public Physics create(SyncCreate<Physics> message) {
    return PhysicsSyncCreate.fromMessage((PhysicsSyncCreate) message);
  }

  @Override
  public void applyUpdate(Physics object, SyncUpdate<Physics> message) {
    PhysicsSyncUpdate update = (PhysicsSyncUpdate) message;
    Body body = object.getBody();
    if (body != null) {
      body.setTransform(new Vec2(update.getX(), update.getY()), update.getRotation());
      body.setLinearVelocity(new Vec2(update.getVx(), update.getVy()));
    }
  }

}
