package simple.networked;

import java.util.HashMap;
import java.util.Map;

import net.faintedge.spiral.core.ProcessCallback;
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

  private WorldManager world;
  private Map<Transform, Entity> transformToEntity = new HashMap<Transform, Entity>();
  
  public SimpleTransformSyncHandler(WorldManager world) {
    this.world = world;
  }
  
  @Override
  public Transform create(SyncCreate<Transform> message) {
    final Transform transform = new Transform(300, 300, 0);
    world.addProcessCallback(new ProcessCallback() {

      @Override
      public Entity createEntity(World world) {
        Entity e = world.createEntity();
        e.addComponent(new Render(new Rectangle(Color.red, 15, 15)));
        e.addComponent(transform);
        e.refresh();
        transformToEntity.put(transform, e);
        return e;
      }
      
    });
    return transform;
  }

  @Override
  public void destroy(Transform object) {
    Entity e = transformToEntity.remove(object);
    world.getWorld().deleteEntity(e);
  }

}
