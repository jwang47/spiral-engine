package net.faintedge.spiral.physics;

import net.faintedge.spiral.core.Control;
import net.faintedge.spiral.core.MyMath;
import net.faintedge.spiral.core.Spatial;
import net.faintedge.spiral.core.Vector2;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

public class PhysicsControl extends Control {
  
  public static long CURR_ID = 0;
  
  private long id = CURR_ID++;
  /**
   * The PhysicsSystem that this control is a part of
   */
  private transient PhysicsSystem system;
  private BodyDef bodyDef;
  private FixtureDef fixtureDef;
  /**
   * The physical body that this control uses
   */
  protected transient Body body;
  /**
   * Pixels to meters conversion rate
   */
  protected float ptm;
  private boolean applyRotation;
  private float maxSpeed = Float.MAX_VALUE;
  
  public PhysicsControl(PhysicsSystem system, BodyDef bodyDef, FixtureDef fixtureDef, float ptm) {
    this.system = system;
    this.bodyDef = bodyDef;
    this.fixtureDef = fixtureDef;
    this.ptm = ptm;
    applyRotation = true;
  }
  
  public void setApplyRotation(boolean applyRotation) {
    this.applyRotation = applyRotation;
  }

  @Override
  public void update(int delta) {
    if (body != null) {
      // snap the target's position to this body's position
      float pixelX = body.getPosition().x * ptm;
      float pixelY = body.getPosition().y * ptm;
      owner.getTranslation().set(pixelX, pixelY);
      
      // also the rotation
      if (applyRotation) {
        owner.setRotation(body.getAngle() * MyMath.DEGREES_PER_RADIAN);
      }
      
      // limit the speed (TODO: don't bother with large max speed)
      float maxSpeedSquared = maxSpeed * maxSpeed;
      if (maxSpeedSquared < Float.MAX_VALUE) {
        float speedSquared = body.getLinearVelocity().lengthSquared();
        if (speedSquared > maxSpeedSquared) {
          float speed = body.getLinearVelocity().length();
          body.setLinearVelocity(body.getLinearVelocity().mul(maxSpeed / speed));
        }
      }
    }
  }

  @Override
  public void onAdded(Spatial target) {
    super.onAdded(target);
    Vector2 translation = target.getTranslation();
    bodyDef.position = new Vec2(translation.getX()/ptm, translation.getY()/ptm);
    system.add(this);
  }

  @Override
  public void reset() {
    system.remove(this);
    super.reset();
  }

  public BodyDef getBodyDef() {
    return bodyDef;
  }
  
  public long getId() {
    return id;
  }

  public FixtureDef getFixtureDef() {
    return fixtureDef;
  }

  public void setBody(Body body) {
    this.body = body;
  }

  public Body getBody() {
      return body;
  }

  public float getMaxSpeed() {
    return maxSpeed;
  }

  public void setMaxSpeed(float maxSpeed) {
    this.maxSpeed = maxSpeed;
  }

}
