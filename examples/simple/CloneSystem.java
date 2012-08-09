package simple;

import net.faintedge.spiral.core.component.ControllerContainer;
import net.faintedge.spiral.core.component.Render;
import net.faintedge.spiral.core.component.Transform;
import net.faintedge.spiral.core.component.render.Rectangle;

import org.newdawn.slick.Color;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import common.DebugTransformMover;

public class CloneSystem extends EntitySystem {
  
  private ComponentMapper<Transform> transformMapper;
  
  private Bag<Entity> cloneMe = new Bag<Entity>();
  
  public CloneSystem() {
    super(Transform.class, Render.class, CloneMe.class);
    new Thread() {
      public void run() {
        while (true) {
          for (int i = 0; i < cloneMe.size(); i++) {
            Entity e = cloneMe.get(i);
            Entity clone = world.createEntity();
            Transform transform = transformMapper.get(e);
            if (transform != null) {
              clone.addComponent(new Transform(transform.getTranslation().getX(), transform.getTranslation().getY(), 0));
              clone.addComponent(new Render(new Rectangle(Color.red, 15, 15)));
              clone.addComponent(new ControllerContainer<Transform>(new DebugTransformMover()));
              clone.refresh();
              System.out.println("added clone");
            }
          }
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }.start();
  }

  @Override
  protected void initialize() {
    transformMapper = new ComponentMapper<Transform>(Transform.class, world);
  }

  @Override
  protected void added(Entity e) {
    super.added(e);
    cloneMe.add(e);
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entities) {
    // TODO Auto-generated method stub
    
  }

  @Override
  protected boolean checkProcessing() {
    // TODO Auto-generated method stub
    return false;
  }

}
