package net.faintedge.spiral.physics.control;

import net.faintedge.spiral.core.Spatial;
import net.faintedge.spiral.physics.PhysicsControl;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Input;

public class DebugPhysicsMoveControl extends PhysicsMoveControl {

  private int north = Input.KEY_W;
  private int south = Input.KEY_S;
  private int west = Input.KEY_A;
  private int east = Input.KEY_D;

  public DebugPhysicsMoveControl(Spatial spatial, float speed) {
    this((PhysicsControl) spatial.getControl(PhysicsControl.class), speed);
  }

  public DebugPhysicsMoveControl(PhysicsControl control, float speed) {
    super(control, speed);
  }

  public void update(int delta) {
    super.setValues(Keyboard.isKeyDown(north), Keyboard.isKeyDown(south), Keyboard.isKeyDown(west), Keyboard.isKeyDown(east));
    super.update(delta);
  }

  @Override
  public void setActive(boolean active) {
    // TODO Auto-generated method stub
    super.setActive(active);
  }

}
