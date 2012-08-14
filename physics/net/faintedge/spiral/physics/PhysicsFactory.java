package net.faintedge.spiral.physics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class PhysicsFactory {

  public static Physics createPhysicsCircle(BodyType type, float radius, float ptm) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = type;
    
    CircleShape circle = new CircleShape();
    circle.m_radius = radius / ptm;
    
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.density = 1.0f;
    fixtureDef.shape = circle;
    
    return new Physics(bodyDef, fixtureDef, ptm);
  }
  
  public static Physics createPhysicsRectangle(BodyType type, float width, float height, float ptm) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = type;
    
    PolygonShape box = new PolygonShape();
    box.setAsBox(width * 0.5f / ptm, height * 0.5f/ptm);
    
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.density = 1.0f;
    fixtureDef.shape = box;

    return new Physics(bodyDef, fixtureDef, ptm);
  }

}
