package net.faintedge.spiral.physics;

import net.faintedge.spiral.core.MyMath;
import net.faintedge.spiral.core.Spatial;
import net.faintedge.spiral.core.Vector2;
import net.faintedge.spiral.spatial.Oval;
import net.faintedge.spiral.spatial.Polygon;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.newdawn.slick.Color;

public class DebugPhysicsControl extends PhysicsControl {

  private Spatial debugSpatial;
  
  public DebugPhysicsControl(PhysicsSystem system, BodyDef bodyDef, FixtureDef fixtureDef, float ptm) {
    super(system, bodyDef, fixtureDef, ptm);
    if (fixtureDef.shape instanceof CircleShape) {
      CircleShape circle = (CircleShape) fixtureDef.shape;
      debugSpatial = new Oval(Color.gray, circle.m_radius*2 * ptm, circle.m_radius*2  * ptm);
    } else if (fixtureDef.shape instanceof PolygonShape) {
      PolygonShape poly = (PolygonShape) fixtureDef.shape;
      debugSpatial = new Polygon(Color.gray, convertVec2ToVector2(poly.m_vertices));
    }
  }
  
  public Vector2[] convertVec2ToVector2(Vec2[] array) { 
    Vector2[] result = new Vector2[array.length];
    for (int i = 0; i < array.length; i++) {
      result[i] = new Vector2(array[i].x * ptm, array[i].y * ptm);
    }
    return result;
  }

  @Override
  public void update(int delta) {
    super.update(delta);
    if (body != null) {
      // snap the debug spatial's position to this body's position
      float pixelX = body.getPosition().x * ptm;
      float pixelY = body.getPosition().y * ptm;
      debugSpatial.getTranslation().set(pixelX, pixelY);
      debugSpatial.setRotation(body.getAngle() * MyMath.DEGREES_PER_RADIAN);
    }
  }

  public Spatial getDebugSpatial() {
    return debugSpatial;
  }
  
}
