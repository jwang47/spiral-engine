package net.faintedge.spiral.physics;

import net.faintedge.spiral.core.Spatial;

public interface PhysicsContactListener {

  public void onContactBegin(Spatial a, Spatial b);
  public void onContactEnd(Spatial a, Spatial b);
  
}
