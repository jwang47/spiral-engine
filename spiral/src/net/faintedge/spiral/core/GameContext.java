package net.faintedge.spiral.core;

import net.faintedge.spiral.spatial.Node;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class GameContext {
  
  private Node rootNode;
  private Camera camera;
  private GameContainer container;
  
  public GameContext(GameContainer container) {
    this.container = container;
    rootNode = new Node();
    camera = new Camera();
  }

  public void render(GameContainer container, Graphics g) throws SlickException {
    g.translate(-camera.getTranslation().getX() + container.getWidth()/2, -camera.getTranslation().getY() + container.getHeight()/2);
    rootNode.render(g);
    g.translate(camera.getTranslation().getX() - container.getWidth()/2, camera.getTranslation().getY() - container.getHeight()/2);
  }
  
  public void init(GameContainer container) throws SlickException {
    this.container = container;
  }

  public void update(GameContainer container, int delta) throws SlickException {
    camera.update(delta);
    rootNode.update(delta);
  }
  
  public Camera getCamera() {
    return camera;
  }

  public Node getRootNode() {
    return rootNode;
  }

  public GameContainer getContainer() {
    return container;
  }

  public void setContainer(GameContainer container) {
    this.container = container;
  }

  public float screenToWorldX(float x) {
    return camera.getTranslation().getX() + x - container.getWidth()/2;
  }
  
  public float screenToWorldY(float y) {
    return camera.getTranslation().getY() - y + container.getHeight()/2;
  }

}
