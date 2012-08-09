package simple.networked;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import common.Mode;

public class SimpleNetworkedServerTest {

  public static void main(String[] args) {
    try {
      AppGameContainer container = new AppGameContainer(new SimpleNetworkedGame(Mode.SERVER));
      container.setAlwaysRender(true);
      container.setUpdateOnlyWhenVisible(false);
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }
}
