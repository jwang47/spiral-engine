package physics.simple.networked;

import java.io.IOException;
import java.net.InetAddress;

import net.faintedge.spiral.core.WorldManager;
import net.faintedge.spiral.core.component.ControllerContainer;
import net.faintedge.spiral.core.component.Render;
import net.faintedge.spiral.core.component.Transform;
import net.faintedge.spiral.core.component.render.Rectangle;
import net.faintedge.spiral.core.system.ControllerSystem;
import net.faintedge.spiral.core.system.RenderSystem;
import net.faintedge.spiral.networked.sync.SyncManager;
import net.faintedge.spiral.networked.sync.SyncObject;
import net.faintedge.spiral.networked.sync.SyncSystem;
import net.faintedge.spiral.networked.sync.handlers.physics.PhysicsSyncCreate;
import net.faintedge.spiral.networked.sync.handlers.physics.PhysicsSyncUpdate;
import net.faintedge.spiral.networked.sync.handlers.transform.TransformSyncCreate;
import net.faintedge.spiral.networked.sync.handlers.transform.TransformSyncUpdate;
import net.faintedge.spiral.networked.sync.msg.SyncCreate;
import net.faintedge.spiral.networked.sync.msg.SyncDestroy;
import net.faintedge.spiral.networked.sync.msg.SyncObjectIdRequest;
import net.faintedge.spiral.networked.sync.msg.SyncUpdate;
import net.faintedge.spiral.physics.Physics;
import net.faintedge.spiral.physics.PhysicsFactory;
import net.faintedge.spiral.physics.system.PhysicsSystem;

import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.SystemManager;
import com.artemis.World;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import common.Mode;
import common.physics.DebugPhysicsMover;

public class SimplePhysicsNetworkedGame extends BasicGame {

  private static final float PTM = 10.0f;
  private World world;

  private EntitySystem renderSystem;
  private EntitySystem physicsSystem;
  private EntitySystem physicsControllerSystem;
  private EntitySystem physicsSyncSystem;

  private Mode mode;
  private Client client;
  private Server server;

  private WorldManager worldManager;

  public SimplePhysicsNetworkedGame(Mode mode) {
    super("simple physics networked test " + mode);
    this.mode = mode;
  }

  @Override
  public void render(GameContainer container, Graphics g) throws SlickException {
    renderSystem.process();
  }

  @Override
  public void init(GameContainer container) throws SlickException {
    world = new World();
    worldManager = new WorldManager(world);

    Kryo kryo = null;
    if (mode == Mode.CLIENT) {
      client = new Client();
      kryo = client.getKryo();
    } else if (mode == Mode.SERVER) {
      server = new Server();
      kryo = server.getKryo();
    }

    SystemManager systemManager = world.getSystemManager();
    renderSystem = systemManager.setSystem(new RenderSystem(container));
    physicsSystem = systemManager.setSystem(new PhysicsSystem(new Vec2(0, 0), PTM));
    physicsControllerSystem = systemManager.setSystem(new ControllerSystem<Physics>(Physics.class,
        (Class<ControllerContainer<Physics>>) (new ControllerContainer<Physics>(null)).getClass()));
    SyncManager<Physics> physicsSyncManager = new SyncManager<Physics>(new SimplePhysicsSyncHandler(kryo, worldManager), (short) 1);
    physicsSyncSystem = systemManager.setSystem(new SyncSystem<Physics, SyncObject<Physics>>(Physics.class,
        (Class<SyncObject<Physics>>) (new SyncObject()).getClass(), physicsSyncManager, kryo));
    systemManager.initializeAll();

    // start server or connect client
    try {
      if (mode == Mode.CLIENT) {
        client.start();
        client.connect(5000, InetAddress.getLocalHost(), 54555, 54777);
        physicsSyncManager.setClient(client);
      } else if (mode == Mode.SERVER) {
        server.start();
        server.bind(54555, 54777);
        physicsSyncManager.setServer(server);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    Entity e = world.createEntity();
    e.addComponent(new Transform(300, 300, 0));
    e.addComponent(new Render(new Rectangle(Color.red, 25, 25)));
    Physics physics = PhysicsFactory.createPhysicsRectangle(BodyType.DYNAMIC, 25, 25, PTM);
    physics.getBodyDef().linearDamping = 1.0f;
    e.addComponent(physics);
    e.addComponent(new SyncObject<Physics>());
    e.addComponent(new ControllerContainer<Physics>(new DebugPhysicsMover()));
    e.refresh();
  }

  @Override
  public void update(GameContainer container, int delta) throws SlickException {
    worldManager.process();
    world.loopStart();
    world.setDelta(delta);
    physicsSystem.process();
    physicsControllerSystem.process();
    physicsSyncSystem.process();
  }

}
