package serverside_physics.refactored;

import java.io.IOException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import common.Mode;

public class TestSSRPhysicsServer {

  public static void main(String[] args) throws IOException, SlickException {
    SSRPhysicsGame game = new SSRPhysicsGame(Mode.SERVER);
    final AppGameContainer container = new AppGameContainer(game);
    container.setDisplayMode(800, 600, false);
    container.setAlwaysRender(true);
    container.setShowFPS(true);
    container.start();
  }
}
