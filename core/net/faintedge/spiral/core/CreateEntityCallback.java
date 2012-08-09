package net.faintedge.spiral.core;

import com.artemis.Entity;
import com.artemis.World;

public interface CreateEntityCallback {

  public Entity createEntity(World world);
  
}
