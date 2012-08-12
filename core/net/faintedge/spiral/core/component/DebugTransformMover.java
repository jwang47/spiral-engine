package net.faintedge.spiral.core.component;


import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Input;

public class DebugTransformMover implements Controller<Transform> {

  private float speed = 0.2f;
  
  public DebugTransformMover() {
    
  }
  
  @Override
  public void apply(Transform transform, int delta) {
    float distance = speed * delta;
    if (Keyboard.isKeyDown(Input.KEY_W)) {
      transform.getTranslation().add(0, -distance);
    }
    if (Keyboard.isKeyDown(Input.KEY_S)) {
      transform.getTranslation().add(0, distance);
    }
    if (Keyboard.isKeyDown(Input.KEY_A)) {
      transform.getTranslation().add(-distance, 0);
    }
    if (Keyboard.isKeyDown(Input.KEY_D)) {
      transform.getTranslation().add(distance, 0);
    }
  }

}
