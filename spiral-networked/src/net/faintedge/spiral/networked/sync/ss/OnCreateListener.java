package net.faintedge.spiral.networked.sync.ss;

import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;

public interface OnCreateListener {
  
  void onCreate(SynchronizeCreateMessage message, Object object);

}
