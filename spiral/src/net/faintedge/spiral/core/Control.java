package net.faintedge.spiral.core;


public abstract class Control {
  
  protected Spatial owner;
  private boolean active;
  
  public Control() {
    this.owner = null;
    this.active = true;
  }
  
  public void update(int delta) {
    
  }
  
  protected void onAdded(Spatial owner) {
    
  }
  
  protected void onRemoved(Spatial owner) {
    
  }
  
  public void reset() {
    this.owner = null;
  }
  
  protected void onActivate() {
    
  }
  
  protected void onDeactivate() {
    
  }
  
  public void setOwner(Spatial owner) {
    this.owner = owner;
    if (owner != null) {
      onAdded(owner);
    } else {
      onRemoved(owner);
    }
  }

  public Spatial getOwner() {
    return owner;
  }
  
  public void setActive(boolean active) {
    this.active = active;
    if (active) {
      onActivate();
    } else {
      onDeactivate();
    }
  }

  public boolean isActive() {
    return active;
  }

}
