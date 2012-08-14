package net.faintedge.spiral.physics.component;


import net.faintedge.spiral.core.Vector2;
import net.faintedge.spiral.core.component.Controller;
import net.faintedge.spiral.physics.Physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Input;

public class DebugShipPhysicsMover implements Controller<Physics> {

  private float speed = 10f;
  
  public DebugShipPhysicsMover() {
    
  }
  

  private Vector2 temp = new Vector2();
  private Vec2 temp2 = new Vec2();
  @Override
  public void apply(Physics physics, int delta) {
    if (physics.getBody() == null) return;
    
    Body body = physics.getBody();
    float distance = speed * delta;
    float rotDistance = 0.01f * delta;
    float angle = body.getAngle();
    
    if (Keyboard.isKeyDown(Input.KEY_W)) {
      temp.fromAngle(angle).mult(distance);
      temp2.x = temp.getX();
      temp2.y = temp.getY();
      physics.getBody().applyForce(temp2, body.getWorldCenter());
    }
    
    if (Keyboard.isKeyDown(Input.KEY_S)) {
      temp.fromAngle(angle).mult(-distance);
      temp2.x = temp.getX();
      temp2.y = temp.getY();
      physics.getBody().applyForce(temp2, body.getWorldCenter());
    }
    
    if (Keyboard.isKeyDown(Input.KEY_A)) {
      body.setTransform(body.getPosition(), angle - rotDistance);
    }
    
    if (Keyboard.isKeyDown(Input.KEY_D)) {
      body.setTransform(body.getPosition(), angle + rotDistance);
    }
    
    float maxSpeed = 10f;
    
  }

}
