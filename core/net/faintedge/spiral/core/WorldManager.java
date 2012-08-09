package net.faintedge.spiral.core;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.artemis.World;


public class WorldManager {
  
  private Queue<ProcessCallback> processCallbacks = new ConcurrentLinkedQueue<ProcessCallback>();
  private World world;

  public WorldManager(World world) {
    this.world = world;
  }
  
  public void process() {
    while (!processCallbacks.isEmpty()) {
      processCallbacks.poll().createEntity(world);
    }
  }
  
  public void addProcessCallback(ProcessCallback callback) {
    processCallbacks.add(callback);
  }

  public World getWorld() {
    return world;
  }

}
