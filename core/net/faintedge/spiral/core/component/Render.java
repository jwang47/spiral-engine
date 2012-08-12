package net.faintedge.spiral.core.component;

import net.faintedge.spiral.core.Renderable;

import com.artemis.Component;


public class Render extends Component {
  
  private Renderable[] renderables;
  
  public Render(Renderable...renderables) {
    this.renderables = renderables;
  }

  public Renderable[] getRenderables() {
    return renderables;
  }

}