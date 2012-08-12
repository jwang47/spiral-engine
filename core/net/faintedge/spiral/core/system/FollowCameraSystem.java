package net.faintedge.spiral.core.system;

import net.faintedge.spiral.core.Camera;
import net.faintedge.spiral.core.component.Transform;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;

public class FollowCameraSystem {
  
  private ComponentMapper<Transform> transformMapper;
  private Camera camera;
  
  private Transform followMe = null;
  
  public FollowCameraSystem(Camera camera, World world) {
    this.camera = camera;
    transformMapper = new ComponentMapper<Transform>(Transform.class, world);
  }
  
  public void followEntity(Entity e) {
    followMe = transformMapper.get(e);
  }
  
  public void process() {
    if (followMe != null) {
      camera.getPosition().set(followMe.getTranslation());
    }
  }

}
