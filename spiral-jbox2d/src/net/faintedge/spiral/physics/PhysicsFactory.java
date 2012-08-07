package net.faintedge.spiral.physics;

import net.faintedge.spiral.core.Spatial;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class PhysicsFactory {

  public static PhysicsControl createPhysicsCircle(Spatial spatial, PhysicsSystem system, float radius, float ptm) {
    return createPhysicsCircle(spatial, system, radius, ptm, false);
  }
  public static PhysicsControl createPhysicsCircle(Spatial spatial, PhysicsSystem system, float radius, float ptm, boolean debug) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DYNAMIC;
    
    CircleShape circle = new CircleShape();
    circle.m_radius = radius / ptm;
    
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = circle;
    
    if (debug) {
      return new DebugPhysicsControl(system, bodyDef, fixtureDef, ptm);
    } else {
      return new PhysicsControl(system, bodyDef, fixtureDef, ptm);
    }
  }

  public static PhysicsControl createPhysicsRectangle(Spatial spatial, PhysicsSystem system, float width, float height, float ptm) {
    return createPhysicsRectangle(spatial, system, width, height, ptm, false);
  }
  
  public static PhysicsControl createPhysicsRectangle(Spatial spatial, PhysicsSystem system, float width, float height, float ptm, boolean debug) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DYNAMIC;
    
    PolygonShape box = new PolygonShape();
    box.setAsBox(width * 0.5f / ptm, height * 0.5f/ptm);
    
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = box;
    
    if (debug) {
      return new DebugPhysicsControl(system, bodyDef, fixtureDef, ptm);
    } else {
      return new PhysicsControl(system, bodyDef, fixtureDef, ptm);
    }
  }

}
