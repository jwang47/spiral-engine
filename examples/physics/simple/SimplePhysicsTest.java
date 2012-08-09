package physics.simple;

import net.faintedge.spiral.core.component.ControllerContainer;
import net.faintedge.spiral.core.component.Render;
import net.faintedge.spiral.core.component.Transform;
import net.faintedge.spiral.core.component.render.Rectangle;
import net.faintedge.spiral.core.system.ControllerSystem;
import net.faintedge.spiral.core.system.RenderSystem;
import net.faintedge.spiral.physics.Physics;
import net.faintedge.spiral.physics.system.PhysicsSystem;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
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
    renderSystem = systemManager.setSystem(new RenderSystem(container.getGraphics()));
    physicsSystem = systemManager.setSystem(new PhysicsSystem(new Vec2(0, 0), PTM));
    physicsControllerSystem = systemManager.setSystem(new ControllerSystem<Physics>(Physics.class,
        (Class<ControllerContainer<Physics>>) (new ControllerContainer<Physics>(null)).getClass()));
    systemManager.initializeAll();

    Entity e = world.createEntity();
    e.addComponent(new Transform(300, 300, 0));
    e.addComponent(new Render(new Rectangle(Color.red, 15, 15)));
    physics = generatePhysicsComponent(BodyType.DYNAMIC, 15, 15);
    e.addComponent(physics);
    e.addComponent(new ControllerContainer<Physics>(new DebugPhysicsMover()));
    e.refresh();
  }

  private Physics generatePhysicsComponent(BodyType bodyType, float width, float height) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = bodyType;
    bodyDef.linearDamping = 1f;
    FixtureDef fixtureDef = new FixtureDef();
    PolygonShape polygon = new PolygonShape();
    polygon.setAsBox(width * 0.5f / PTM, height * 0.5f / PTM);
    fixtureDef.shape = polygon;
    return new Physics(bodyDef, fixtureDef, PTM);
  }

  @Override
  public void update(GameContainer container, int delta) throws SlickException {
    world.loopStart();
    world.setDelta(delta);
    physicsSystem.process();
    physicsControllerSystem.process();
    if (physics == null || physics.getBody() == null) {
      System.out.println("wtf");
    }
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
