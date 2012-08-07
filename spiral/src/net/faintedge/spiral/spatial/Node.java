package net.faintedge.spiral.spatial;

import java.util.LinkedList;
import java.util.Queue;

import net.faintedge.spiral.core.SafeArrayList;
import net.faintedge.spiral.core.Spatial;

import org.newdawn.slick.Graphics;

public class Node extends Spatial {
  
  private SafeArrayList<Spatial> spatials;
  protected transient Queue<Spatial> toAdd;
  protected transient Queue<Spatial> toRemove;

  public Node(String name) {
    super(name);
    spatials = new SafeArrayList<Spatial>(Spatial.class);
    toAdd = new LinkedList<Spatial>();
    toRemove = new LinkedList<Spatial>();
  }
  
  public Node() {
    super();
    spatials = new SafeArrayList<Spatial>(Spatial.class);
    toAdd = new LinkedList<Spatial>();
    toRemove = new LinkedList<Spatial>();
  }
  
  @Override
  public String toString() {
    String spatialsText = "";
    for (Spatial spatial : spatials) {
      spatialsText += spatial.toString() + ", ";
    }
    return String.format("Node(%s) = [%s]", name, spatialsText);
  }
  
  public void add(Spatial spatial) {
    toAdd.add(spatial);
  }
  
  public void remove(Spatial spatial) {
    toRemove.add(spatial);
  }
  
  @Override
  protected void subRender(Graphics g) {
    for (Spatial spatial : spatials) {
      spatial.render(g);
    }
  }
  
  @Override
  protected void subUpdate(int delta) {
    while (!toAdd.isEmpty()) {
      doAdd(toAdd.poll());
    }
    while (!toRemove.isEmpty()) {
      doRemove(toRemove.poll());
    }
    for (Spatial spatial : spatials) {
      spatial.update(delta);
    }
  }

  protected void doRemove(Spatial spatial) {
    if (spatials.remove(spatial)) {
      spatial.onRemoved(this);
    }
  }

  protected void doAdd(Spatial spatial) {
    spatials.add(spatial);
    spatial.onAdded(this);
  }

  public boolean isDetached() {
    return parent == null;
  }

  public SafeArrayList<Spatial> getChildren() {
    return spatials;
  }
  
}
