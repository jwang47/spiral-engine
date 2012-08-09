package physics.simple.networked;

import net.faintedge.spiral.core.CreateEntityCallback;
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

  public WorldManager world;
  
  public SimplePhysicsSyncHandler(WorldManager world) {
    this.world = world;
  }
  
  @Override
  public Physics create(SyncCreate<Physics> message) {
    final Physics physics = super.create(message);
    world.addCreateCallback(new CreateEntityCallback() {

      @Override
      public Entity createEntity(World world) {
        Entity e = world.createEntity();
        e.addComponent(new Render(new Rectangle(Color.red, 15, 15)));
        e.addComponent(new Transform(0, 0, 0));
        e.refresh();
        return e;
      }
      
    });
    return physics;
  }

}
