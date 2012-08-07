package net.faintedge.spiral.spatial.control;

import net.faintedge.spiral.core.Camera;
import net.faintedge.spiral.core.Vector2;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;


public class LookAtMouseControl extends SmoothRotateControl {

	private Camera camera;
	private GameContainer container;
	private Vector2 temp = new Vector2();
	
	public LookAtMouseControl(Camera camera, GameContainer container) {
	  this.container = container;
		this.camera = camera;
	}

	@Override
	public void update(int delta) {
		// offset = (cam - container/2 + mouse) + (-target.x, target)
		temp.set(camera.getTranslation())
			.sub(container.getWidth()/2, container.getHeight()/2)
			.add(Mouse.getX(), Mouse.getY())
			.sub(owner.getTranslation());
		destRotation = (float) (Math.atan2(-temp.getY(), temp.getX()) * 180/Math.PI);
		super.update(delta);
	}

}
