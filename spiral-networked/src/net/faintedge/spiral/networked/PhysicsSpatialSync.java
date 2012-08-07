package net.faintedge.spiral.networked;

import com.captiveimagination.jgn.synchronization.message.SynchronizeMessage;


public class PhysicsSpatialSync extends SynchronizeMessage {
  
  private float x, y;
  private float vx, vy;
  private float rotation;
  private byte direction;

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
  
  public byte getDirection() {
    return direction;
  }
  public void setDirection(byte direction) {
    this.direction = direction;
  }
  public void setMoveInfo(byte direction) {
    this.direction = direction;
  }

}
