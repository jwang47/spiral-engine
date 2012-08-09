package net.faintedge.spiral.networked.handlers.physics;

import net.faintedge.spiral.networked.msg.SyncCreate;
import net.faintedge.spiral.physics.Physics;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;


public class PhysicsSyncCreate extends SyncCreate<Physics> {
  
  private BodyType type;
  private Shape shape;
  private float linearDamping;
  private float ptm;

  public static Physics fromMessage(PhysicsSyncCreate msg) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = msg.type;
    bodyDef.linearDamping = msg.linearDamping;
    
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = msg.shape;
    
    return new Physics(bodyDef, fixtureDef, msg.ptm);
  }

  public static SyncCreate<Physics> toMessage(Physics object) {
    PhysicsSyncCreate msg = new PhysicsSyncCreate();
    msg.type = object.getBodyDef().type;
    msg.linearDamping = object.getBodyDef().linearDamping;
    msg.shape = object.getFixtureDef().shape;
    msg.ptm = object.getPtm();
    return msg;
  }
  
}