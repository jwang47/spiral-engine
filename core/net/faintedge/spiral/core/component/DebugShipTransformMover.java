package net.faintedge.spiral.core.component;


import net.faintedge.spiral.core.Vector2;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Input;

public class DebugShipTransformMover implements Controller<Transform> {

  private float speed = 0.2f;
  
  public DebugShipTransformMover() {
    
  }
  
  
  private Vector2 temp = new Vector2();
  @Override
  public void apply(Transform transform, int delta) {
    float distance = speed * delta;
    float rotDistance = 0.2f * delta;
    if (Keyboard.isKeyDown(Input.KEY_W)) {
      temp.fromDegrees(transform.getRotation()).mult(distance);
      transform.getTranslation().add(temp);
    }
    if (Keyboard.isKeyDown(Input.KEY_S)) {
      temp.fromDegrees(transform.getRotation()).mult(-distance);
      transform.getTranslation().add(temp);
    }
    if (Keyboard.isKeyDown(Input.KEY_A)) {
      transform.setRotation(transform.getRotation() - rotDistance);
    }
    if (Keyboard.isKeyDown(Input.KEY_D)) {
      transform.setRotation(transform.getRotation() + rotDistance);
    }
  }

}
