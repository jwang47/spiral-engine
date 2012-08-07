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
import com.captiveimagination.jgn.clientserver.JGNServer;
import com.captiveimagination.jgn.synchronization.SynchronizationManager;
import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;
import common.Mode;

public class TestSimpleServer {
  
  private static final Logger logger = Logger.getLogger(TestSimpleServer.class.getName());

  public static void main(String[] args) throws IOException, SlickException {
    JGN.register(SpatialSync.class);
    
    SimpleGame game = new SimpleGame(Mode.SERVER);
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
    JGNServer server = new JGNServer(serverReliable, serverFast);
    SynchronizationManager serverSyncManager = new SynchronizationManager(server, controller);
    serverSyncManager.addSyncObjectManager(new SimpleManager(game.getRootNode()));
    game.setSyncManager(serverSyncManager);
    JGN.createThread(server, serverSyncManager).start();
    
    // Register our client object with the synchronization manager
    while(game.getPlayer() == null) {
      Thread.yield();
    }
    try {
      logger.info("registering player");
      serverSyncManager.register(game.getPlayer(), new SynchronizeCreateMessage(), 10);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
}
