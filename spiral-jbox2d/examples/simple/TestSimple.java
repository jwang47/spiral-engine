package simple;

import java.io.IOException;

import net.faintedge.spiral.core.GameContext;
import net.faintedge.spiral.physics.PhysicsControl;
import net.faintedge.spiral.physics.PhysicsFactory;
import net.faintedge.spiral.physics.PhysicsSystem;
import net.faintedge.spiral.physics.control.DebugPhysicsMoveControl;
import net.faintedge.spiral.spatial.Rectangle;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class TestSimple extends BasicGame {

  private GameContext context;
  private PhysicsSystem system;
  private PhysicsControl physics;
  
  public TestSimple() {
    super("simple");
  }

  @Override
  public void render(GameContainer container, Graphics g) throws SlickException {
    context.render(container, g);
  }

  @Override
  public void init(GameContainer container) throws SlickException {
    system = new PhysicsSystem();
    system.init();
    
    context = new GameContext(container);
    
    float ptm = 10.0f;
    Rectangle player = new Rectangle(Color.red, 20, 20);
    physics = PhysicsFactory.createPhysicsRectangle(player, system, 20, 20, ptm);
    physics.getBodyDef().fixedRotation = true;
    physics.getBodyDef().linearDamping = 0.5f;
    player.addControl(physics);
    player.addControl(new DebugPhysicsMoveControl(physics, 1000f));
    context.getRootNode().add(player);
  }

  @Override
  public void update(GameContainer container, int delta) throws SlickException {
    system.update(delta);
    context.update(container, delta);
  }
  
  public static void main(String[] args) throws IOException {
    try {
      AppGameContainer container = new AppGameContainer(new TestSimple());
      container.setDisplayMode(800, 600, false);
      container.setAlwaysRender(true);
      container.setShowFPS(true);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }

}
