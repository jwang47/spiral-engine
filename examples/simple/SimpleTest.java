package simple;

import net.faintedge.spiral.core.component.ControllerContainer;
import net.faintedge.spiral.core.component.DebugTransformMover;
import net.faintedge.spiral.core.component.Render;
import net.faintedge.spiral.core.component.Transform;
import net.faintedge.spiral.core.component.render.Rectangle;
import net.faintedge.spiral.core.component.render.ext.Graph;
import net.faintedge.spiral.core.system.ControllerSystem;
import net.faintedge.spiral.core.system.RenderSystem;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.SystemManager;
import com.artemis.World;

public class SimpleTest extends BasicGame {

  private World world;

  private EntitySystem renderSystem;
  private EntitySystem transformMutatorSystem;

  public SimpleTest() {
    super("simple test");
  }

  @Override
  public void render(GameContainer container, Graphics g) throws SlickException {
    renderSystem.process();
  }

  @Override
  public void init(GameContainer container) throws SlickException {
    world = new World();

    SystemManager systemManager = world.getSystemManager();
    renderSystem = systemManager.setSystem(new RenderSystem(container));
    transformMutatorSystem = systemManager.setSystem(new ControllerSystem<Transform>(Transform.class,
        (Class<ControllerContainer<Transform>>) (new ControllerContainer<Transform>(null)).getClass()));
    systemManager.initializeAll();

    Entity e = world.createEntity();
    e.addComponent(new Transform(300, 300, 0));
    e.addComponent(new Render(new Rectangle(Color.red, 15, 15)));
    e.addComponent(new ControllerContainer<Transform>(new DebugTransformMover()));
    e.refresh();
  }

  @Override
  public void update(GameContainer container, int delta) throws SlickException {
    world.loopStart();
    world.setDelta(delta);
    transformMutatorSystem.process();
  }

  public static void main(String[] args) {
    try {
      AppGameContainer container = new AppGameContainer(new SimpleTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }

}
