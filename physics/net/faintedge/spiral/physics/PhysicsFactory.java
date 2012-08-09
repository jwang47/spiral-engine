package net.faintedge.spiral.physics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class PhysicsFactory {

  public static Physics createPhysicsCircle(float radius, float ptm) {
    return createPhysicsCircle(radius, ptm, false);
  }
  public static Physics createPhysicsCircle(float radius, float ptm, boolean debug) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DYNAMIC;
    
    CircleShape circle = new CircleShape();
    circle.m_radius = radius / ptm;
    
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = circle;
    
    return new Physics(bodyDef, fixtureDef, ptm);
  }

  public static Physics createPhysicsRectangle(float width, float height, float ptm) {
    return createPhysicsRectangle(width, height, ptm, false);
  }
  
  public static Physics createPhysicsRectangle(float width, float height, float ptm, boolean debug) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DYNAMIC;
    
    PolygonShape box = new PolygonShape();
    box.setAsBox(width * 0.5f / ptm, height * 0.5f/ptm);
    
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = box;

    return new Physics(bodyDef, fixtureDef, ptm);
  }

}
