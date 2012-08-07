package net.faintedge.spiral.networked;

import com.captiveimagination.jgn.synchronization.message.SynchronizeMessage;

public class SpatialSync extends SynchronizeMessage {

  private boolean syncPosition;
  private boolean syncRotation;
  private float x, y;
  private float rotation;
  
  public SpatialSync() {
    syncPosition = true;
    syncRotation = true;
  }
  
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

  public boolean isSyncPosition() {
    return syncPosition;
  }

  public void setSyncPosition(boolean syncPosition) {
    this.syncPosition = syncPosition;
  }

  public boolean isSyncRotation() {
    return syncRotation;
  }

  public void setSyncRotation(boolean syncRotation) {
    this.syncRotation = syncRotation;
  }
  
}
