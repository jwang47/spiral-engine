package net.faintedge.spiral.core.system;

import net.faintedge.spiral.core.Renderable;
import net.faintedge.spiral.core.Vector2;
import net.faintedge.spiral.core.component.Render;
import net.faintedge.spiral.core.component.Transform;

import org.newdawn.slick.Graphics;

import util.Assert;
import util.Log;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;

public class RenderSystem extends EntitySystem {
  
  private ComponentMapper<Transform> transformMapper;
  private ComponentMapper<Render> renderMapper;
  
  private Graphics graphics;

  public RenderSystem(Graphics graphics) {
    super(Transform.class, Render.class);
    this.graphics = graphics;
  }

  @Override
  protected void initialize() {
    transformMapper = new ComponentMapper<Transform>(Transform.class, world);
    renderMapper = new ComponentMapper<Render>(Render.class, world);
  }

  @Override
  protected final void processEntities(ImmutableBag<Entity> entities) {
      for (int i = 0, s = entities.size(); s > i; i++) {
        Entity e = entities.get(i);
        final Transform transform = transformMapper.get(e);
        final Render render = renderMapper.get(e);
        
        if (render == null) {
          Log.warn("render was null for " + e.getId());
          continue;
        }
        
        Assert.isTrue(render != null);
        Assert.isTrue(render.getRenderables() != null);
        
        Vector2 translation = transform.getTranslation();
        float rotation = transform.getRotation();

        graphics.rotate(translation.getX(), translation.getY(), rotation);
        graphics.translate(translation.getX(), translation.getY());
        for (Renderable renderable : render.getRenderables()) {
          renderable.render(graphics);
        }
        graphics.translate(-translation.getX(), -translation.getY());
        graphics.rotate(translation.getX(), translation.getY(), -rotation);
      }
  }
  
  @Override
  protected boolean checkProcessing() {
      return true;
  }

  public Graphics getGraphics() {
    return graphics;
  }

  public void setGraphics(Graphics graphics) {
    this.graphics = graphics;
  }

}
