package net.faintedge.spiral.networked.handlers.physics;

import net.faintedge.spiral.networked.msg.SyncUpdate;
import net.faintedge.spiral.physics.Physics;


public class PhysicsSyncUpdate extends SyncUpdate<Physics> {
  
  private float x;
  private float y;
  private float vx;
  private float vy;
  private float rotation;
  
  public float getX() {
    return x;
  }
  public void setX(float x) {
    this.x = x;
  }
  public float getY() {
    return y;
  }
  public void setY(float y) {
    this.y = y;
  }
  public float getVx() {
    return vx;
  }
  public void setVx(float vx) {
    this.vx = vx;
  }
  public float getVy() {
    return vy;
  }
  public void setVy(float vy) {
    this.vy = vy;
  }
  public float getRotation() {
    return rotation;
  }
  public void setRotation(float rotation) {
    this.rotation = rotation;
  }
  
}