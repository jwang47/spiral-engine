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
import net.faintedge.spiral.networked.sync.SyncManager;
import net.faintedge.spiral.networked.sync.SyncObject;
import net.faintedge.spiral.networked.sync.SyncObjectDebugRender;
import net.faintedge.spiral.networked.sync.SyncSystem;
import net.faintedge.spiral.networked.sync.handlers.transform.TransformSyncCreate;
import net.faintedge.spiral.networked.sync.handlers.transform.TransformSyncUpdate;
import net.faintedge.spiral.networked.sync.msg.SyncCreate;
import net.faintedge.spiral.networked.sync.msg.SyncDestroy;
import net.faintedge.spiral.networked.sync.msg.SyncObjectIdRequest;
import net.faintedge.spiral.networked.sync.msg.SyncUpdate;

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
    super("simple networked test " + mode);
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
    renderSystem = systemManager.setSystem(new RenderSystem(container.getGraphics()));
    transformMutatorSystem = systemManager.setSystem(new ControllerSystem<Transform>(Transform.class,
        (Class<ControllerContainer<Transform>>) (new ControllerContainer<Transform>(null)).getClass()));
    transformSyncManager = new SyncManager<Transform>(new SimpleTransformSyncHandler(kryo, worldManager), (short) 1);
    transformSyncSystem = systemManager.setSystem(new SyncSystem<Transform, SyncObject<Transform>>(Transform.class,
        (Class<SyncObject<Transform>>) (new SyncObject()).getClass(), transformSyncManager, kryo));
    systemManager.initializeAll();

    // start server or connect client
    try {
      if (mode == Mode.CLIENT) {
        client.start();
        client.connect(5000, InetAddress.getLocalHost(), 54555, 54777);
        transformSyncManager.setClient(client);
      } else if (mode == Mode.SERVER) {
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
  
  @Override
  public void update(GameContainer container, int delta) throws SlickException {
    worldManager.process();
    world.loopStart();
    world.setDelta(delta);
    transformMutatorSystem.process();
    transformSyncSystem.process();
  }

}
