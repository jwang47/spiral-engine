package simple;

import net.faintedge.spiral.core.GameContext;
import net.faintedge.spiral.core.Spatial;
import net.faintedge.spiral.spatial.Node;
import net.faintedge.spiral.spatial.Rectangle;
import net.faintedge.spiral.spatial.control.DebugMoveControl;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.captiveimagination.jgn.synchronization.SynchronizationManager;
import common.Mode;

public class SimpleGame extends BasicGame {

  private Mode mode;
  private GameContext context;
  private Spatial ship;
  private SynchronizationManager syncManager;
  private Node world;
  
  public SimpleGame(Mode mode) {
    super(mode == Mode.SERVER ? "test: simple server" : "test: simple client");
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
    
    ship = new Rectangle(Color.green, 20, 20);
    ship.addControl(new DebugMoveControl(0.1f));
    world.add(ship);
    
    context.getRootNode().add(world);
  }

  @Override
  public void update(GameContainer container, int delta) throws SlickException {
    context.update(container, delta);
  }

  public void setSyncManager(SynchronizationManager syncManager) {
    this.syncManager = syncManager;
  }

}
