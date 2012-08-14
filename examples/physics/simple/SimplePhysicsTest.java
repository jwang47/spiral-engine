package physics.simple;

import net.faintedge.spiral.core.component.ControllerContainer;
import net.faintedge.spiral.core.component.Render;
import net.faintedge.spiral.core.component.Transform;
import net.faintedge.spiral.core.component.render.Oval;
import net.faintedge.spiral.core.component.render.Rectangle;
import net.faintedge.spiral.core.system.ControllerSystem;
import net.faintedge.spiral.core.system.RenderSystem;
import net.faintedge.spiral.physics.Physics;
import net.faintedge.spiral.physics.PhysicsFactory;
import net.faintedge.spiral.physics.system.PhysicsSystem;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.SystemManager;
import com.artemis.World;
import common.physics.DebugPhysicsMover;

public class SimplePhysicsTest extends BasicGame {

  private static final float PTM = 10.0f;
  private World world;

  private EntitySystem renderSystem;
  private EntitySystem physicsSystem;
  private EntitySystem physicsControllerSystem;
  private Physics physics;

  public SimplePhysicsTest() {
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
    physicsSystem = systemManager.setSystem(new PhysicsSystem(new Vec2(0, 0), PTM));
    physicsControllerSystem = systemManager.setSystem(new ControllerSystem<Physics>(Physics.class,
        (Class<ControllerContainer<Physics>>) (new ControllerContainer<Physics>(null)).getClass()));
    systemManager.initializeAll();

    Entity e = world.createEntity();
    e.addComponent(new Transform(0, 0, 0));
    e.addComponent(new Render(new Oval(Color.red, 16, 16)));
    Physics physics = PhysicsFactory.createPhysicsCircle(BodyType.DYNAMIC, 8, PTM);
    physics.getBodyDef().fixedRotation = true;
    e.addComponent(physics);
    e.addComponent(new ControllerContainer<Physics>(new DebugPhysicsMover()));
    e.refresh();

    e = world.createEntity();
    e.addComponent(new Transform(0, 0, 0));
    e.addComponent(new Render(new Rectangle(Color.blue, 15, 150)));
    e.addComponent(PhysicsFactory.createPhysicsRectangle(BodyType.DYNAMIC, 15, 150, PTM));
    e.refresh();
    
    e = world.createEntity();
    e.addComponent(new Transform(0, 0, 0));
    e.addComponent(new Render(new Rectangle(Color.blue, 15, 150)));
    e.addComponent(PhysicsFactory.createPhysicsRectangle(BodyType.DYNAMIC, 15, 150, PTM));
    e.refresh();
    
    e = world.createEntity();
    e.addComponent(new Transform(0, 0, 0));
    e.addComponent(new Render(new Rectangle(Color.blue, 15, 150)));
    e.addComponent(PhysicsFactory.createPhysicsRectangle(BodyType.DYNAMIC, 15, 150, PTM));
    e.refresh();

    spawnWall(-400, 0, 15, 600);
    spawnWall(400, 0, 15, 600);
    spawnWall(0, -300, 800, 15);
    spawnWall(0, 300, 800, 15);
  }
  
  private void spawnWall(float x, float y, float width, float height) {
    Entity e = world.createEntity();
    e.addComponent(new Transform(x, y, 0));
    e.addComponent(new Render(new Rectangle(Color.red, width, height)));
    e.addComponent(PhysicsFactory.createPhysicsRectangle(BodyType.STATIC, width, height, PTM));
    e.refresh();
  }

  @Override
  public void update(GameContainer container, int delta) throws SlickException {
    world.loopStart();
    world.setDelta(delta);
    physicsSystem.process();
    physicsControllerSystem.process();
  }

  public static void main(String[] args) {
    try {
      AppGameContainer container = new AppGameContainer(new SimplePhysicsTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }

}
