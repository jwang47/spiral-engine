package net.faintedge.spiral.networked.handlers.transform;

import net.faintedge.spiral.core.component.Transform;
import net.faintedge.spiral.networked.msg.SyncUpdate;


public class TransformSyncUpdate extends SyncUpdate<Transform> {
  
  private float x;
  private float y;
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
  public float getRotation() {
    return rotation;
  }
  public void setRotation(float rotation) {
    this.rotation = rotation;
  }
  
}