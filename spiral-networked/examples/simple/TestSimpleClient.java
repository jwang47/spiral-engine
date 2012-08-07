package simple;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import net.faintedge.spiral.networked.SpatialGraphicalController;
import net.faintedge.spiral.networked.SpatialSync;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import com.captiveimagination.jgn.JGN;
import com.captiveimagination.jgn.Updatable;
import com.captiveimagination.jgn.clientserver.JGNClient;
import com.captiveimagination.jgn.synchronization.SynchronizationManager;
import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;
import common.Mode;

public class TestSimpleClient {
  
  private static final Logger logger = Logger.getLogger(TestSimpleClient.class.getName());

  public static void main(String[] args) throws IOException, SlickException, InterruptedException {
    JGN.register(SpatialSync.class);
    
    SimpleGame game = new SimpleGame(Mode.CLIENT);
    final AppGameContainer container = new AppGameContainer(game);
    container.setDisplayMode(800, 600, false);
    container.setAlwaysRender(true);
    container.setShowFPS(true);
    Runnable gameRunnable = new Runnable() {
      @Override
      public void run() {
        try {
          container.start();
        } catch (SlickException e) {
          e.printStackTrace();
        }
      }
    };
    new Thread(gameRunnable).start();
    
    SpatialGraphicalController controller = new SpatialGraphicalController();
    InetSocketAddress serverReliable = new InetSocketAddress(InetAddress.getLocalHost(), 12388);
    InetSocketAddress serverFast = new InetSocketAddress(InetAddress.getLocalHost(), 12389);
    JGNClient client = new JGNClient(new InetSocketAddress(InetAddress.getLocalHost(), 0), new InetSocketAddress(InetAddress.getLocalHost(), 0));
    SynchronizationManager clientSyncManager = new SynchronizationManager(client, controller);
    clientSyncManager.addSyncObjectManager(new SimpleManager(game.getRootNode()));
    game.setSyncManager(clientSyncManager);
    JGN.createThread(client, clientSyncManager).start();
    client.connectAndWait(serverReliable, serverFast, 5000);
    
    // Register our client object with the synchronization manager
    while (game.getPlayer() == null) {
      Thread.yield();
    }
    try {
      logger.info("registering player");
      clientSyncManager.register(game.getPlayer(), new SynchronizeCreateMessage(), 10);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
