package net.faintedge.spiral.physics.control;

import net.faintedge.spiral.core.Control;
import net.faintedge.spiral.core.Spatial;
import net.faintedge.spiral.physics.PhysicsControl;

import org.jbox2d.common.Vec2;

public class PhysicsMoveControl extends Control {

  public static final byte NORTH = 0x01;
  public static final byte SOUTH = 0x02;
  public static final byte WEST = 0x04;
  public static final byte EAST = 0x08;
  
	private float speed;
	private byte direction = 0;
  private transient Vec2 movement = new Vec2();
  private transient Vec2 center = new Vec2();
  private transient PhysicsControl physics;
	
	public PhysicsMoveControl(PhysicsControl physics, float speed) {
	  this.physics = physics;
		this.speed = speed;
	}

  @Override
	public void update(int delta) {
    if (physics.getBody() != null) {
  		movement.set(0, 0);
  		if((direction & NORTH) != 0) movement.addLocal(0, -1);
  		if((direction & SOUTH) != 0) movement.addLocal(0, 1);
  		if((direction & WEST) != 0) movement.addLocal(-1, 0);
  		if((direction & EAST) != 0) movement.addLocal(1, 0);
      movement.mulLocal(speed * delta/1000.0f);
  		physics.getBody().applyForce(movement, center);
    }
	}
	
	public void setValues(boolean north, boolean south, boolean west, boolean east) {
	  direction = 0;
		if (north) direction |= NORTH;
		if (south) direction |= SOUTH;
		if (west) direction |= WEST;
		if (east) direction |= EAST;
	}
	
	public boolean isNorth() {
		return (direction & NORTH) != 0;
	}

	public boolean isSouth() {
		return (direction & SOUTH) != 0;
	}

	public boolean isWest() {
		return (direction & WEST) != 0;
	}

	public boolean isEast() {
		return (direction & EAST) != 0;
	}

  public byte getDirection() {
    return direction;
  }

  public void setDirection(byte direction) {
    this.direction = direction;
  }

  public float getSpeed() {
    return speed;
  }

  public void setSpeed(float speed) {
    this.speed = speed;
  }

}
