package net.faintedge.spiral.core;

import net.faintedge.spiral.spatial.Node;

import org.newdawn.slick.Graphics;

public abstract class Spatial {

  protected transient Node parent;
  protected String name;
  private Vector2 translation;
  private float rotation;
  
  private transient SafeArrayList<Control> controls;

  public Spatial(String name) {
    this.name = name;
    parent = null;
    translation = new Vector2();
    controls = new SafeArrayList<Control>(Control.class);
  }
  
  public Spatial() {
    this.name = null;
    parent = null;
    translation = new Vector2();
    controls = new SafeArrayList<Control>(Control.class);
  }
  
  public String toString() {
    return String.format("%s(%s)", this.getClass().getSimpleName().toString(), name);
  }

  public String getName() {
    return name;
  }
  
  protected void subRender(Graphics g) {
    
  }
  
  protected void subUpdate(int delta) {
    
  }
  
  public <T> Control getControl(Class<T> type) {
    for (Control control : controls) {
      if (type.isAssignableFrom(control.getClass())) {
        return control;
      }
    }
    return null;
  }
  
  /**
   * @param delta time passed since last update in milliseconds
   */
  public final void update(int delta) {
    for (Control control : controls) {
      if (control.isActive()) {
        control.update(delta);
      }
    }
    subUpdate(delta);
  }
  
  public final void render(Graphics g) {
    g.rotate(translation.getX(), translation.getY(), rotation);
    g.translate(translation.getX(), translation.getY());
    subRender(g);
    g.translate(-translation.getX(), -translation.getY());
    g.rotate(translation.getX(), translation.getY(), -rotation);
  }
  
  public void addControl(Control control) {
    control.setOwner(this);
    controls.add(control);
  }
  
  public Vector2 getTranslation() {
    return translation;
  }

  /**
   * @return the rotation of this spatial in degrees
   */
  public float getRotation() {
    return rotation;
  }

  /**
   * @param rotation rotation of this spatial in degrees
   */
  public void setRotation(float rotation) {
    this.rotation = rotation;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void onRemoved(Node parent) {
    // reset all controls of this spatial first
    for (Control control : controls) {
      control.setOwner(null);
      control.reset();
    }
    controls.clear();
    
    this.parent = null;
  }
  
  public void onAdded(Node parent) {
    this.parent = parent;
  }

  public Node getParent() {
    return parent;
  }

  public void detach() {
    if (this.parent != null) {
      this.parent.remove(this);
    }
  }

  public SafeArrayList<Control> getControls() {
    return controls;
  }
  
}
