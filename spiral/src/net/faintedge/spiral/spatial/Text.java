package net.faintedge.spiral.spatial;

import net.faintedge.spiral.core.Spatial;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Text extends Spatial {
  
  private Color color;
  private String text;

  public Text(String text, Color color) {
    this.color = color;
    this.text = text;
  }

  @Override
  protected void subRender(Graphics g) {
    g.setColor(color);
    g.drawString(text, 0, 0);
  }

}
