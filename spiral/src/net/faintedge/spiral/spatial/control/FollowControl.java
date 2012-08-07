package net.faintedge.spiral.spatial.control;

import net.faintedge.spiral.core.Control;
import net.faintedge.spiral.core.Spatial;

public class FollowControl extends Control {

	private Spatial target;

	public FollowControl(Spatial target) {
		this.target = target;
	}
	
	@Override
	public void update(int delta) {
		owner.getTranslation().set(target.getTranslation());
	}
	
}
