package net.faintedge.spiral.physics;

import java.util.HashMap;
import java.util.Map;

import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

import com.artemis.Component;

public class Physics extends Component {

  /**
   * The physical body that this control uses
   */
  protected transient Body body;
  /**
   * Pixels to meters conversion rate
   */
  protected float ptm;
  
  // various options
  private BodyDef bodyDef;
  private FixtureDef fixtureDef;
  private boolean applyRotation;
  private float maxSpeed = Float.MAX_VALUE;
  
  // runtime stuff
  private Map<Physics, Manifold> collisions = new HashMap<Physics, Manifold>();

  public Physics(BodyDef bodyDef, FixtureDef fixtureDef, float ptm) {
    this.bodyDef = bodyDef;
    this.fixtureDef = fixtureDef;
    this.ptm = ptm;
  }

  public boolean isApplyRotation() {
    return applyRotation;
  }

  public void setApplyRotation(boolean applyRotation) {
    this.applyRotation = applyRotation;
  }

  public float getMaxSpeed() {
    return maxSpeed;
  }

  public void setMaxSpeed(float maxSpeed) {
    this.maxSpeed = maxSpeed;
  }

  public BodyDef getBodyDef() {
    return bodyDef;
  }

  public FixtureDef getFixtureDef() {
    return fixtureDef;
  }

  public Body getBody() {
    return body;
  }

  public void setBody(Body body) {
    this.body = body;
  }

  public void addCollision(Physics other, Manifold manifold) {
    collisions.put(other, manifold);
  }
  
  public void removeCollision(Physics other) {
    collisions.remove(other);
  }

  public float getPtm() {
    return ptm;
  }
}
