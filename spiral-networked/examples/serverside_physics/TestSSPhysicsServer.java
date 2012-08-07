package serverside_physics;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import net.faintedge.spiral.networked.Net;
import net.faintedge.spiral.networked.PhysicsMoveGraphicalController;
import net.faintedge.spiral.networked.PhysicsSpatialGraphicalController;
import net.faintedge.spiral.networked.sync.ss.SSPhysicsMoveObjectManager;
import net.faintedge.spiral.networked.sync.ss.SSPhysicsSpatialObjectManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import com.captiveimagination.jgn.JGN;
import com.captiveimagination.jgn.clientserver.JGNServer;
import com.captiveimagination.jgn.synchronization.SynchronizationManager;
import common.Mode;

public class TestSSPhysicsServer {
  
  private static final Logger logger = Logger.getLogger(TestSSPhysicsServer.class.getName());
  private static final float PTM = 10.0f;

  public static void main(String[] args) throws IOException, SlickException {
//    CoolFormatter.init();
    Net.init();
    
    SSPhysicsGame game = new SSPhysicsGame(Mode.SERVER);
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

    InetSocketAddress serverReliable = new InetSocketAddress(InetAddress.getLocalHost(), Net.TCP_PORT);
    InetSocketAddress serverFast = new InetSocketAddress(InetAddress.getLocalHost(), Net.UDP_PORT);
    
    JGNServer server = new JGNServer(serverReliable, serverFast);

    PhysicsSpatialGraphicalController physicsSpatialController = new PhysicsSpatialGraphicalController(SSPhysicsGame.physics);
    SynchronizationManager physicsSpatialSyncManager = new SynchronizationManager(server, physicsSpatialController, (short) 2);
    
    PhysicsMoveGraphicalController physicsMoveController = new PhysicsMoveGraphicalController();
    SynchronizationManager moveSyncManager = new SynchronizationManager(server, physicsMoveController, (short) 3);

    // initialize sync object managers
    SSPhysicsSpatialObjectManager spatialObjectManager = new SSPhysicsSpatialObjectManager(Mode.SERVER, game.getRootNode(), SSPhysicsGame.physics, PTM);
    spatialObjectManager.setSpatialSyncManager(physicsSpatialSyncManager);
    physicsSpatialSyncManager.addSyncObjectManager(spatialObjectManager);
    server.addMessageListener(spatialObjectManager);
    
    SSPhysicsMoveObjectManager moveObjectManager = new SSPhysicsMoveObjectManager(Mode.SERVER);
    moveObjectManager.setMoveSyncManager(moveSyncManager);
    moveSyncManager.addSyncObjectManager(moveObjectManager);
    server.addMessageListener(moveObjectManager);
    
    game.setObjectManager(spatialObjectManager);
    JGN.createThread(server, physicsSpatialSyncManager, moveSyncManager).start();
  }
  
}
