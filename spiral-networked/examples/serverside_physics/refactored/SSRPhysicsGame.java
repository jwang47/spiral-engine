package serverside_physics.refactored;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Logger;

import net.faintedge.spiral.core.Dumper;
import net.faintedge.spiral.core.GameContext;
import net.faintedge.spiral.core.Spatial;
import net.faintedge.spiral.networked.Net;
import net.faintedge.spiral.networked.PhysicsSpatialSyncCreate;
import net.faintedge.spiral.networked.SpatialSyncCreate;
import net.faintedge.spiral.networked.sync.ss.OnCreateListener;
import net.faintedge.spiral.networked.sync.ss.node.SSPhysicsClientNode;
import net.faintedge.spiral.networked.sync.ss.node.SSPhysicsServerNode;
import net.faintedge.spiral.physics.PhysicsSystem;
import net.faintedge.spiral.physics.control.DebugPhysicsMoveControl;
import net.faintedge.spiral.spatial.Node;
import net.faintedge.spiral.spatial.Oval;

import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.dynamics.BodyType;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;

import common.Mode;

public class SSRPhysicsGame extends BasicGame implements OnCreateListener {
 
  private static final Logger logger = Logger.getLogger(SSRPhysicsGame.class.getName());
  
  public static final float PTM = 10.0f;
  private final PhysicsSystem physics = new PhysicsSystem();
  private GameContext context;
  private Mode mode;
  private Node world;
  
  private SSPhysicsServerNode server;
  private SSPhysicsClientNode client;

  private Spatial ship;
  private int registerShipRequestId = -1;
  
  public SSRPhysicsGame(Mode mode) {
    super(mode == Mode.SERVER ? "test: physics server" : "test: physics client");
    this.mode = mode;
    world = new Node("world");
  }
  
  public Node getRootNode() {
    return world;
  }

  @Override
  public void init(GameContainer container) throws SlickException {
    context = new GameContext(container);
  
    try {
      if (mode == Mode.SERVER) {
        server = new SSPhysicsServerNode("server node", physics, PTM);
        server.listen(Net.TCP_PORT, Net.UDP_PORT);
        
        PhysicsSpatialSyncCreate create = genCreate("server ship");
        Spatial ship = PhysicsSpatialSyncCreate.createPhysicsSpatial(create, false, physics, PTM);
        ship.addControl(new DebugPhysicsMoveControl(ship, 1000f));
        server.addForSync(ship);
        
        context.getRootNode().add(server);
      } else {
        client = new SSPhysicsClientNode("client node", physics, PTM);
        client.connect(InetAddress.getLocalHost(), Net.TCP_PORT, Net.UDP_PORT);
  
        PhysicsSpatialSyncCreate create = genCreate("client ship");
        registerShipRequestId  = client.addForSync(create, true, this);
        
        System.out.println("waiting for server to respond...");
        while(ship == null) {
          Thread.yield();
        }
        System.out.println("ready!");
        
        context.getRootNode().add(client);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onCreate(SynchronizeCreateMessage message, Object object) {
    if (message instanceof PhysicsSpatialSyncCreate) {
      PhysicsSpatialSyncCreate msg = (PhysicsSpatialSyncCreate) message;
      Spatial spatial = (Spatial) object;
      if (msg.getRequestId() == registerShipRequestId) {
        ship = spatial;
        ship.addControl(new DebugPhysicsMoveControl(ship, 1000f));
      }
    }
  }

  @Override
  public void render(GameContainer container, Graphics g) throws SlickException {
    context.render(container, g);
    g.setColor(Color.orange);
    if (ship != null) {
      g.drawString("ship position: " + ship.getTranslation(), 15, 15);
    }
  }
  
  @Override
  public void update(GameContainer container, int delta) throws SlickException {
    physics.update(delta);
    context.update(container, delta);
  }

  private PhysicsSpatialSyncCreate genCreate(String name) {
    float width = 10;
    float height = 10;
    
    PhysicsSpatialSyncCreate create = new PhysicsSpatialSyncCreate();
    
    SpatialSyncCreate spatialCreate = new SpatialSyncCreate();
    spatialCreate.setName(name);
    spatialCreate.setType(Oval.class);
    spatialCreate.setParam("color", Color.red);
    spatialCreate.setParam("width", width);
    spatialCreate.setParam("height", height);
    create.setSpatial(spatialCreate);
    
    create.setLinearDamping(0.5f);
    create.setMoveSpeed(1000f);
    create.setBodyType(BodyType.DYNAMIC);
    create.setShapeType(ShapeType.CIRCLE);
    create.setRadius(width * 0.5f / PTM);
    return create;
  }

  @Override
  public void keyPressed(int key, char c) {
    if (key == Input.KEY_F) {
      Dumper.dump(server);
      Dumper.dump(client);
    }
  }

}
