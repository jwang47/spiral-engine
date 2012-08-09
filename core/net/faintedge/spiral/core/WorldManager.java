package net.faintedge.spiral.core;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.artemis.World;


public class WorldManager {
  
  private Queue<CreateEntityCallback> createCallbacks = new ConcurrentLinkedQueue<CreateEntityCallback>();
  private World world;

  public WorldManager(World world) {
    this.world = world;
  }
  
  public void process() {
    while (!createCallbacks.isEmpty()) {
      createCallbacks.poll().createEntity(world);
    }
  }
  
  public void addCreateCallback(CreateEntityCallback callback) {
    createCallbacks.add(callback);
  }

}
