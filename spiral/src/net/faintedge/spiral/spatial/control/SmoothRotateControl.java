package net.faintedge.spiral.spatial.control;

import net.faintedge.spiral.core.Control;

public class SmoothRotateControl extends Control {

  protected float destRotation;
  private float speed;
  
  public SmoothRotateControl(float speed) {
    this.speed = speed;
  }
  
  public SmoothRotateControl() {
    this(0.25f);
  }
  
  @Override
  public void update(int delta) {
    if(destRotation - owner.getRotation() > 180f) { destRotation -= 360f; }
    else if(destRotation - owner.getRotation() < -180f) { destRotation += 360f; }
    owner.setRotation(owner.getRotation() + (destRotation - owner.getRotation()) * speed);
    if(owner.getRotation() > 360f || owner.getRotation() < -360f) {
      owner.setRotation(owner.getRotation() % 360f);
      destRotation = destRotation % 360f;
    }
  }

  public float getDestRotation() {
    return destRotation;
  }

  public void setDestRotation(float destRotation) {
    this.destRotation = destRotation;
  }

  public float getSpeed() {
    return speed;
  }

  public void setSpeed(float speed) {
    this.speed = speed;
  }
  
}
