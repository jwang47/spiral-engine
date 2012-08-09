package net.faintedge.spiral.core.component;

import net.faintedge.spiral.core.Vector2;

import com.artemis.Component;

public class Transform extends Component {

  private Vector2 translation;
  private float rotation;
  
  public Transform() {
    this.translation = new Vector2();
    this.rotation = 0;
  }

  public Transform(float x, float y, float rotation) {
    this.translation = new Vector2(x, y);
    this.rotation = rotation;
  }

  public Vector2 getTranslation() {
    return translation;
  }

  public float getRotation() {
    return rotation;
  }

  public void setRotation(float rotation) {
    this.rotation = rotation;
  }
  
}
