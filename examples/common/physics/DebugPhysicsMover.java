package common.physics;

import net.faintedge.spiral.core.component.Controller;
import net.faintedge.spiral.physics.Physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Input;

import util.Log;

public class DebugPhysicsMover implements Controller<Physics> {

  private float speed = 10f;
  
  public DebugPhysicsMover() {
    
  }
  
  private Vec2 force = new Vec2();
  @Override
  public void apply(Physics physics, int delta) {
    Body body = physics.getBody();
    if (body == null) return;
    
    float distance = speed * delta;
    force.setZero();
    if (Keyboard.isKeyDown(Input.KEY_W)) {
      force.addLocal(0, -1);
    }
    if (Keyboard.isKeyDown(Input.KEY_S)) {
      force.addLocal(0, 1);
    }
    if (Keyboard.isKeyDown(Input.KEY_A)) {
      force.addLocal(-1, 0);
    }
    if (Keyboard.isKeyDown(Input.KEY_D)) {
      force.addLocal(1, 0);
    }
    
    force.normalize();
    force.mulLocal(distance);
  }

}
