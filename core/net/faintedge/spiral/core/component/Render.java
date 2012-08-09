package net.faintedge.spiral.core.component;

import net.faintedge.spiral.core.Renderable;

import com.artemis.Component;


public class Render extends Component {
  
  private Renderable renderable;
  
  public Render(Renderable renderable) {
    this.renderable = renderable;
  }

  public Renderable getRenderable() {
    return renderable;
  }

}