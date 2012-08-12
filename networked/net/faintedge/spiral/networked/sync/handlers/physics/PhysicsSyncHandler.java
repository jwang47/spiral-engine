package net.faintedge.spiral.networked.sync.handlers.physics;

import net.faintedge.spiral.networked.sync.SyncHandler;
import net.faintedge.spiral.networked.sync.msg.SyncCreate;
import net.faintedge.spiral.networked.sync.msg.SyncDestroy;
import net.faintedge.spiral.networked.sync.msg.SyncUpdate;
import net.faintedge.spiral.physics.Physics;

import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;

import com.esotericsoftware.kryo.Kryo;

public class PhysicsSyncHandler extends SyncHandler<Physics> {

  public PhysicsSyncHandler(Kryo kryo) {
    super(kryo);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void registerMessages(Kryo kryo) {
    kryo.register(PhysicsSyncCreate.class);
    kryo.register(PhysicsSyncUpdate.class);
    kryo.register(PhysicsSyncDestroy.class);
    kryo.register(BodyType.class);
    kryo.register(ShapeType.class);
    kryo.register(float[].class);
    kryo.register(float[][].class);
  }
  
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
      update.setVy(body.getLinearVelocity().y);
      update.setRotation(body.getAngle());
    }
    return update;
  }

  @Override
  public SyncDestroy<Physics> makeDestroyMessage(Physics object) {
    return new PhysicsSyncDestroy();
  }

  @Override
  public Physics create(SyncCreate<Physics> message) {
    return PhysicsSyncCreate.fromMessage((PhysicsSyncCreate) message);
  }

  @Override
  public void destroy(Physics object) {
    
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
