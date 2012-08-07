package net.faintedge.spiral.spatial;

import org.newdawn.slick.Color;

public class ColorWrapper extends Color {
  
  public ColorWrapper() {
    super(0, 0, 0);
  }
  
  public ColorWrapper(Color color) {
    super(color);
  }

  public ColorWrapper(float r, float g, float b) {
    super(r, g, b);
  }

  
  
}
