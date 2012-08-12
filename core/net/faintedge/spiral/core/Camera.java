package net.faintedge.spiral.core;


public class Camera {
  
  private Vector2 position;
  private float scale;
  
  public Camera() {
    position = new Vector2();
    scale = 1;
  }

  public Vector2 getPosition() {
    return position;
  }

  public float getScale() {
    return scale;
  }

  public void setScale(float scale) {
    this.scale = scale;
  }
  
}
