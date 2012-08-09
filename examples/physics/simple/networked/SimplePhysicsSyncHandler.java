package physics.simple.networked;

import java.util.HashMap;
import java.util.Map;

import net.faintedge.spiral.core.ProcessCallback;
import net.faintedge.spiral.core.WorldManager;
import net.faintedge.spiral.core.component.Render;
import net.faintedge.spiral.core.component.Transform;
import net.faintedge.spiral.core.component.render.Rectangle;
import net.faintedge.spiral.networked.handlers.physics.PhysicsSyncHandler;
import net.faintedge.spiral.networked.msg.SyncCreate;
import net.faintedge.spiral.physics.Physics;

import org.newdawn.slick.Color;

import com.artemis.Entity;
import com.artemis.World;

public class SimplePhysicsSyncHandler extends PhysicsSyncHandler {

  private WorldManager world;
  private Map<Physics, Entity> physicsToEntity = new HashMap<Physics, Entity>();
  
  public SimplePhysicsSyncHandler(WorldManager world) {
    this.world = world;
  }
  
  @Override
  public Physics create(SyncCreate<Physics> message) {
    final Physics physics = super.create(message);
    world.addProcessCallback(new ProcessCallback() {
      @Override
      public Entity createEntity(World world) {
        Entity e = world.createEntity();
        e.addComponent(new Render(new Rectangle(Color.cyan, 25, 25)));
        e.addComponent(new Transform(0, 0, 0));
        e.addComponent(physics);
        e.refresh();
        System.out.println("created remote entity");
        return e;
      }
    });
    return physics;
  }

  @Override
  public void destroy(Physics object) {
    final Entity e = physicsToEntity.remove(object);
    world.addProcessCallback(new ProcessCallback() {
      @Override
      public Entity createEntity(World world) {
        world.deleteEntity(e);
        System.out.println("destroyed entity");
        return e;
      }
    });
  }
  
}
