package net.faintedge.spiral.spatial.control;

import net.faintedge.spiral.core.Control;

public class TimedLifeControl extends Control {

  private int lifetime;
  private boolean died;

  public TimedLifeControl(int lifetime) {
    this.lifetime = lifetime * 1000;
  }

  @Override
  public void update(int delta) {
    lifetime -= delta;
    if (!died && lifetime <= 0) {
      this.getOwner().getParent().remove(this.owner);
      died = true;
    }
  }
  
}
