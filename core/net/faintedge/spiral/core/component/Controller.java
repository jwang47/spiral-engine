package net.faintedge.spiral.core.component;


/**
 * Controls another component
 * @author Jiahua
 * @param <T> the type of component that it controls
 *
 */
public interface Controller<T> {

  void apply(T object, int delta);
  
}
