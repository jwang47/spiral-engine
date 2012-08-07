package physics;

import net.faintedge.spiral.core.GameContext;
import net.faintedge.spiral.core.Spatial;
import net.faintedge.spiral.networked.Net;
import net.faintedge.spiral.physics.PhysicsControl;
import net.faintedge.spiral.physics.PhysicsFactory;
import net.faintedge.spiral.physics.PhysicsSystem;
import net.faintedge.spiral.physics.control.DebugPhysicsMoveControl;
import net.faintedge.spiral.spatial.Node;
import net.faintedge.spiral.spatial.Rectangle;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.captiveimagination.jgn.synchronization.SynchronizationManager;

import common.CoolFormatter;
import common.Mode;

public class PhysicsGame extends BasicGame {

  public static final PhysicsSystem physics = new PhysicsSystem();
  
  private Mode mode;
  private GameContext context;
  private Spatial ship;
  private SynchronizationManager syncManager;
  private Node world;
  
  public PhysicsGame(Mode mode) {
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
  }

  @Override
  public void init(GameContainer container) throws SlickException {
    context = new GameContext(container);
    
    float ptm = 10.0f;
    ship = new Rectangle(Color.green, 20, 20);
    PhysicsControl control = PhysicsFactory.createPhysicsRectangle(ship, physics, 20, 20, ptm);
    control.getBodyDef().linearDamping = 0.5f;
    ship.addControl(control);
    ship.addControl(new DebugPhysicsMoveControl(ship, 1000f));
    world.add(ship);
    
    context.getRootNode().add(world);
  }

  @Override
  public void update(GameContainer container, int delta) throws SlickException {
    physics.update(delta);
    context.update(container, delta);
  }

  public void setSyncManager(SynchronizationManager syncManager) {
    this.syncManager = syncManager;
  }

}
