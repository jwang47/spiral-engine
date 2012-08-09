package net.faintedge.spiral.core.component.render;

import net.faintedge.spiral.core.Renderable;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Text implements Renderable {
  
  private Color color;
  private String text;

  public Text(String text, Color color) {
    this.color = color;
    this.text = text;
  }

  @Override
  public void render(Graphics g) {
    g.setColor(color);
    g.drawString(text, 0, 0);
  }

}
