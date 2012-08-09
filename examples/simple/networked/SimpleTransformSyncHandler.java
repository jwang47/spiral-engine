package simple.networked;

import net.faintedge.spiral.core.CreateEntityCallback;
import net.faintedge.spiral.core.WorldManager;
import net.faintedge.spiral.core.component.Render;
import net.faintedge.spiral.core.component.Transform;
import net.faintedge.spiral.core.component.render.Rectangle;
import net.faintedge.spiral.networked.handlers.transform.TransformSyncHandler;
import net.faintedge.spiral.networked.msg.SyncCreate;

import org.newdawn.slick.Color;

import com.artemis.Entity;
import com.artemis.World;

public class SimpleTransformSyncHandler extends TransformSyncHandler {

  public WorldManager world;
  
  public SimpleTransformSyncHandler(WorldManager world) {
    this.world = world;
  }
  
  @Override
  public Transform create(SyncCreate<Transform> message) {
    final Transform transform = new Transform(300, 300, 0);
    world.addCreateCallback(new CreateEntityCallback() {

      @Override
      public Entity createEntity(World world) {
        Entity e = world.createEntity();
        e.addComponent(new Render(new Rectangle(Color.red, 15, 15)));
        e.addComponent(transform);
        e.refresh();
        return e;
      }
      
    });
    return transform;
  }

}
