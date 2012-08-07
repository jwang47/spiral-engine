package net.faintedge.spiral.spatial.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.faintedge.spiral.core.Control;

public class TagControl extends Control {

  private List<Object> tags;
  
  public TagControl() {
    tags = new ArrayList<Object>();
  }
  
  public TagControl(Object...tags) {
    this.tags = Arrays.asList(tags);
  }
  
  public void addTag(Object tag) {
    tags.add(tag);
  }
  
  public void removeTag(Object tag) {
    tags.remove(tag);
  }
  
  public boolean hasTag(Object tag) {
    return tags.contains(tag);
  }
  
}
