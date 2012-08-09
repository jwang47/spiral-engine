package net.faintedge.spiral.core.system;

import net.faintedge.spiral.core.component.ControllerContainer;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;

public class ControllerSystem<T extends Component> extends EntityProcessingSystem {

  private Class<T> type;
  private Class<ControllerContainer<T>> controllerType;
  
  private ComponentMapper<T> objectMapper;
  private ComponentMapper<ControllerContainer<T>> controllerMapper;

  public ControllerSystem(Class<T> type, Class<ControllerContainer<T>> controllerType) {
    super(type, controllerType);
    this.type = type;
    this.controllerType = controllerType;
  }

  @Override
  protected void initialize() {
    objectMapper = new ComponentMapper<T>(type, world);
    controllerMapper = new ComponentMapper<ControllerContainer<T>>(controllerType, world);
  }

  @Override
  protected void process(Entity e) {
    T object = objectMapper.get(e);
    ControllerContainer<T> controller = controllerMapper.get(e);
    controller.apply(object, world.getDelta());
  }

}
