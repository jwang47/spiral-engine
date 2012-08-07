package serverside_physics;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.faintedge.spiral.core.Dumper;
import net.faintedge.spiral.core.GameContext;
import net.faintedge.spiral.core.Spatial;
import net.faintedge.spiral.networked.Net;
import net.faintedge.spiral.networked.PhysicsSpatialSyncCreate;
import net.faintedge.spiral.networked.SpatialSyncCreate;
import net.faintedge.spiral.networked.sync.ss.RegisterSpatialRequest;
import net.faintedge.spiral.networked.sync.ss.SSPhysicsMoveObjectManager;
import net.faintedge.spiral.networked.sync.ss.SSPhysicsSpatialObjectManager;
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

import com.captiveimagination.jgn.clientserver.JGNClient;
import common.Mode;

public class SSPhysicsGame extends BasicGame {
  
  private static final Logger logger = Logger.getLogger(SSPhysicsGame.class.getName());
  
  public static final float PTM = 10.0f;
  public static final PhysicsSystem physics = new PhysicsSystem();
  private Mode mode;
  private GameContext context;
  private Spatial ship;
  private SSPhysicsSpatialObjectManager physicsManager;
  private JGNClient client;
  private Node world;
  
  public SSPhysicsGame(Mode mode) {
    super(mode == Mode.SERVER ? "test: physics server" : "test: physics client");
    this.mode = mode;
    world = new Node("world");
  }
  
  public Spatial getPlayer() {
    return ship;
  }
  
  public Node getRootNode() {
    return world;
  }

  @Override
  public void render(GameContainer container, Graphics g) throws SlickException {
    context.render(container, g);
    g.setColor(Color.orange);
    g.drawString("timesCalledRegister: " + SSPhysicsSpatialObjectManager.timesCalledRegisterSpatial, 15, 15);
    g.drawString("timesCalledRegisterMove: " + SSPhysicsMoveObjectManager.timesCalledRegisterMove, 15, 25);
    g.drawString("timesCalledCreate: " + SSPhysicsSpatialObjectManager.timesCalledCreate, 15, 35);
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
  public void init(GameContainer container) throws SlickException {
    context = new GameContext(container);

    float ptm = 10.0f;
    if (mode == Mode.SERVER) {
      PhysicsSpatialSyncCreate create = genCreate("server ship");
      ship = PhysicsSpatialSyncCreate.createPhysicsSpatial(create, false, physics, ptm);
      ship.addControl(new DebugPhysicsMoveControl(ship, 1000f));
      world.add(ship);
      try {
        physicsManager.register(ship, create);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      while (client == null) {
        Thread.yield();
      }
      
      try {
        InetSocketAddress serverReliable = new InetSocketAddress(InetAddress.getLocalHost(), Net.TCP_PORT);
        InetSocketAddress serverFast = new InetSocketAddress(InetAddress.getLocalHost(), Net.UDP_PORT);
        client.connectAndWait(serverReliable, serverFast, 1000);
      } catch (Exception e) {
        e.printStackTrace();
        System.exit(1);
      }
      
      physicsManager.setControlledRequestId(1);
      RegisterSpatialRequest request = new RegisterSpatialRequest();
      logger.log(Level.INFO, "sending request to register client ship");
      request.setRequestId(1);
      request.setClientControlled(true);
      request.setCreate(genCreate("client ship"));
      client.sendToServer(request);
    }
    
    context.getRootNode().add(world);
  }

  @Override
  public void keyPressed(int key, char c) {
    if (key == Input.KEY_F) {
      Dumper.dump(world);
    }
  }

  @Override
  public void update(GameContainer container, int delta) throws SlickException {
    physics.update(delta);
    context.update(container, delta);
  }

  public JGNClient getClient() {
    return client;
  }

  public void setClient(JGNClient client) {
    this.client = client;
  }

  public SSPhysicsSpatialObjectManager getPhysicsManager() {
    return physicsManager;
  }

  public void setObjectManager(SSPhysicsSpatialObjectManager physicsManager) {
    this.physicsManager = physicsManager;
  }

}
