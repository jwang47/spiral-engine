package net.faintedge.spiral.core.component.render;

import net.faintedge.spiral.core.Renderable;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class ImageSpatial implements Renderable {

  private Image image;

  public ImageSpatial() {
    super();
  }

  public ImageSpatial(Image image) {
    this.image = image;
  }

  public void render(Graphics g) {
    g.setColor(Color.orange);
    g.fillRect(-image.getWidth() / 2, -image.getHeight() / 2, image.getWidth(), image.getHeight());
    g.drawImage(image, -image.getWidth() / 2, -image.getHeight() / 2);
  }

}
