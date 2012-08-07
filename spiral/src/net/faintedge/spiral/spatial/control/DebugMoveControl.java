package net.faintedge.spiral.spatial.control;


import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Input;

public class DebugMoveControl extends MoveControl {

	private int north = Input.KEY_W;
	private int south = Input.KEY_S;
	private int west = Input.KEY_A;
	private int east = Input.KEY_D;
	
	public DebugMoveControl(float speed) {
		super(speed);
	}
	
	public void update(int delta) {
		super.setValues(Keyboard.isKeyDown(north),
				Keyboard.isKeyDown(south),
				Keyboard.isKeyDown(west),
				Keyboard.isKeyDown(east));
		super.update(delta);
		if(Keyboard.isKeyDown(Input.KEY_J)) {
			owner.setRotation(owner.getRotation() + 0.1f * delta);
			
		}
		if(Keyboard.isKeyDown(Input.KEY_K)) {
			owner.setRotation(owner.getRotation() - 0.1f * delta);
			
		}
	}
	
}
