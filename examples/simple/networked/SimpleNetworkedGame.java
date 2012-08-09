package simple.networked;

import java.io.IOException;
import java.net.InetAddress;

import net.faintedge.spiral.core.WorldManager;
import net.faintedge.spiral.core.component.ControllerContainer;
import net.faintedge.spiral.core.component.Render;
import net.faintedge.spiral.core.component.Transform;
import net.faintedge.spiral.core.component.render.Rectangle;
import net.faintedge.spiral.core.system.ControllerSystem;
import net.faintedge.spiral.core.system.RenderSystem;
import net.faintedge.spiral.networked.SyncManager;
import net.faintedge.spiral.networked.SyncObject;
import net.faintedge.spiral.networked.SyncSystem;
import net.faintedge.spiral.networked.handlers.transform.TransformSyncCreate;
import net.faintedge.spiral.networked.handlers.transform.TransformSyncUpdate;
import net.faintedge.spiral.networked.msg.SyncCreate;
import net.faintedge.spiral.networked.msg.SyncDestroy;
import net.faintedge.spiral.networked.msg.SyncObjectIdRequest;
import net.faintedge.spiral.networked.msg.SyncUpdate;

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
import common.DebugTransformMover;
import common.Mode;

public class SimpleNetworkedGame extends BasicGame {

  private World world;

  private EntitySystem renderSystem;
  private EntitySystem transformMutatorSystem;
  private EntitySystem transformSyncSystem;
  private SyncManager<Transform> transformSyncManager;

  private Mode mode;
  private Client client;
  private Server server;

  private WorldManager worldManager;

  public SimpleNetworkedGame(Mode mode) {
    super("simple test");
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

    SystemManager systemManager = world.getSystemManager();
    renderSystem = systemManager.setSystem(new RenderSystem(container.getGraphics()));
    transformMutatorSystem = systemManager.setSystem(new ControllerSystem<Transform>(Transform.class,
        (Class<ControllerContainer<Transform>>) (new ControllerContainer<Transform>(null)).getClass()));
    transformSyncManager = new SyncManager<Transform>(new SimpleTransformSyncHandler(worldManager), (short) 1);
    transformSyncSystem = systemManager.setSystem(new SyncSystem<Transform, SyncObject<Transform>>(Transform.class,
        (Class<SyncObject<Transform>>) (new SyncObject()).getClass(), transformSyncManager));
    systemManager.initializeAll();

    // start server or connect client
    try {
      if (mode == Mode.CLIENT) {
        client = new Client();
        registerMessages(client.getKryo());
        client.start();
        client.connect(5000, InetAddress.getLocalHost(), 54555, 54777);
        transformSyncManager.setClient(client);
      } else if (mode == Mode.SERVER) {
        server = new Server();
        registerMessages(server.getKryo());
        server.start();
        server.bind(54555, 54777);
        transformSyncManager.setServer(server);
      }
    } catch (IOException e) {

    }

    Entity e = world.createEntity();
    e.addComponent(new Transform(300, 300, 0));
    e.addComponent(new Render(new Rectangle(Color.red, 15, 15)));
    e.addComponent(new SyncObject<Transform>());
    e.addComponent(new ControllerContainer<Transform>(new DebugTransformMover()));
    e.refresh();
  }

  private void registerMessages(Kryo kryo) {
    kryo.register(SyncCreate.class);
    kryo.register(SyncUpdate.class);
    kryo.register(SyncDestroy.class);
    kryo.register(SyncObjectIdRequest.class);
    kryo.register(TransformSyncCreate.class);
    kryo.register(TransformSyncUpdate.class);
  }

  @Override
  public void update(GameContainer container, int delta) throws SlickException {
    worldManager.process();
    world.loopStart();
    world.setDelta(delta);
    transformMutatorSystem.process();
    transformSyncSystem.process();
  }

}
