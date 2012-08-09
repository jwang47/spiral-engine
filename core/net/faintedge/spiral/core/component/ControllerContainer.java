package net.faintedge.spiral.core.component;

import com.artemis.Component;

public class ControllerContainer<T> extends Component implements Controller<T> {

  private Controller<T> impl;

  public ControllerContainer(Controller<T> impl) {
    this.impl = impl;
  }
  
  @Override
  public void apply(T object, int delta) {
    impl.apply(object, delta);
  }

}
