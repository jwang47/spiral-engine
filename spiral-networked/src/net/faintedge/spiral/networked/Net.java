package net.faintedge.spiral.networked;

import java.util.HashMap;

import net.faintedge.spiral.networked.sync.control.ControlSync;
import net.faintedge.spiral.networked.sync.control.ControlSyncCreate;
import net.faintedge.spiral.networked.sync.control.PhysicsMoveSync;
import net.faintedge.spiral.networked.sync.control.PhysicsMoveSyncCreate;
import net.faintedge.spiral.networked.sync.ss.RegisterSpatialReply;
import net.faintedge.spiral.networked.sync.ss.RegisterSpatialRequest;

import com.captiveimagination.jgn.JGN;

public class Net {
  
  public static final int TCP_PORT = 12398;
  public static final int UDP_PORT = 12399;
  
  public static void init() {
    JGN.register(HashMap.class);
    
    JGN.register(SpatialSync.class);
    JGN.register(SpatialSyncCreate.class);
    JGN.register(PhysicsSpatialSync.class);
    JGN.register(PhysicsSpatialSyncCreate.class);
    JGN.register(PhysicsSpatialSyncRemove.class);
    
    JGN.register(ControlSync.class);
    JGN.register(ControlSyncCreate.class);
    JGN.register(PhysicsMoveSync.class);
    JGN.register(PhysicsMoveSyncCreate.class);
    
    JGN.register(RegisterSpatialReply.class);
    JGN.register(RegisterSpatialRequest.class);
  }

}
