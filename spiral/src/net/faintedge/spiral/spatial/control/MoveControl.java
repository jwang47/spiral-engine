package net.faintedge.spiral.spatial.control;

import net.faintedge.spiral.core.Control;
import net.faintedge.spiral.core.Vector2;

public class MoveControl extends Control {

	private float speed;
	private boolean north, south, west, east;
	private Vector2 movement = new Vector2();
	
	public MoveControl(float speed) {
		this.speed = speed;
	}

	@Override
	public void update(int delta) {
		movement.set(0, 0);
		if(north) movement.add(0, -1);
		if(south) movement.add(0, 1);
		if(west) movement.add(-1, 0);
		if(east) movement.add(1, 0);
		movement.mult(speed * delta);
		owner.getTranslation().add(movement);
	}
	
	public void setValues(boolean north, boolean south, boolean west, boolean east) {
		this.north = north;
		this.south = south;
		this.west = west;
		this.east = east;
	}
	
	public boolean isNorth() {
		return north;
	}

	public void setNorth(boolean north) {
		this.north = north;
	}

	public boolean isSouth() {
		return south;
	}

	public void setSouth(boolean south) {
		this.south = south;
	}

	public boolean isWest() {
		return west;
	}

	public void setWest(boolean west) {
		this.west = west;
	}

	public boolean isEast() {
		return east;
	}

	public void setEast(boolean east) {
		this.east = east;
	}

}
