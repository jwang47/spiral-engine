package net.faintedge.spiral.spatial;

import net.faintedge.spiral.core.Spatial;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class ImageSpatial extends Spatial {

  private Image image;

  public ImageSpatial() {
    super();
  }

  public ImageSpatial(Image image) {
    this.image = image;
  }

  protected void subRender(Graphics g) {
    g.setColor(Color.orange);
    g.fillRect(-image.getWidth() / 2, -image.getHeight() / 2, image.getWidth(), image.getHeight());
    g.drawImage(image, -image.getWidth() / 2, -image.getHeight() / 2);
  }

}
