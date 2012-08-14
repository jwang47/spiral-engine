package net.faintedge.spiral.core.component;


import net.faintedge.spiral.core.Vector2;
import net.faintedge.spiral.core.system.RenderSystem;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Input;

public class DebugShipMouseTransformMover implements Controller<Transform> {

  private float speed = 0.2f;
  private RenderSystem render;
  
  public DebugShipMouseTransformMover(RenderSystem render) {
    this.render = render;
  }
  

  private Vector2 temp = new Vector2();
  @Override
  public void apply(Transform transform, int delta) {
    float distance = speed * delta;
    
    temp.set(render.getWorldMouseX(), render.getWorldMouseY()).sub(transform.getTranslation());
    transform.setRotation(temp.toDegrees());
    
    if (Keyboard.isKeyDown(Input.KEY_W)) {
      temp.fromDegrees(transform.getRotation()).mult(distance);
      transform.getTranslation().add(temp);
    }
    if (Keyboard.isKeyDown(Input.KEY_S)) {
      temp.fromDegrees(transform.getRotation()).mult(-distance);
      transform.getTranslation().add(temp);
    }
    if (Keyboard.isKeyDown(Input.KEY_A)) {
      temp.fromDegrees(transform.getRotation() + 90).mult(-distance);
      transform.getTranslation().add(temp);
    }
    if (Keyboard.isKeyDown(Input.KEY_D)) {
      temp.fromDegrees(transform.getRotation() - 90).mult(-distance);
      transform.getTranslation().add(temp);
    }
  }

}
