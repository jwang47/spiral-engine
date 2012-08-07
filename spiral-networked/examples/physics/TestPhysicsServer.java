package physics;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import net.faintedge.spiral.networked.Net;
import net.faintedge.spiral.networked.PhysicsSpatialGraphicalController;
import net.faintedge.spiral.networked.PhysicsSpatialSyncCreate;
import net.faintedge.spiral.networked.SpatialSync;
import net.faintedge.spiral.physics.PhysicsControl;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import com.captiveimagination.jgn.JGN;
import com.captiveimagination.jgn.clientserver.JGNServer;
import com.captiveimagination.jgn.synchronization.SynchronizationManager;
import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;

import common.CoolFormatter;
import common.Mode;

public class TestPhysicsServer {
  
  private static final Logger logger = Logger.getLogger(TestPhysicsServer.class.getName());

  public static void main(String[] args) throws IOException, SlickException {
//    CoolFormatter.init();
    Net.init();
    
    PhysicsGame game = new PhysicsGame(Mode.SERVER);
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

    PhysicsSpatialGraphicalController controller = new PhysicsSpatialGraphicalController(PhysicsGame.physics);
    InetSocketAddress serverReliable = new InetSocketAddress(InetAddress.getLocalHost(), Net.TCP_PORT);
    InetSocketAddress serverFast = new InetSocketAddress(InetAddress.getLocalHost(), Net.UDP_PORT);
    JGNServer server = new JGNServer(serverReliable, serverFast);
    SynchronizationManager serverSyncManager = new SynchronizationManager(server, controller);
    serverSyncManager.addSyncObjectManager(new PhysicsManager(game.getRootNode(), PhysicsGame.physics));
    game.setSyncManager(serverSyncManager);
    JGN.createThread(server, serverSyncManager).start();
    
    // Register our client object with the synchronization manager
    while (game.getPlayer() == null || game.getPlayer().getControl(PhysicsControl.class) == null) {
      Thread.yield();
    }
    try {
      logger.info("registering player");
      serverSyncManager.register(game.getPlayer(), PhysicsSpatialSyncCreate.fromSpatial(game.getPlayer()), 10);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
}
