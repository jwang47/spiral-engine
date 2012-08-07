package net.faintedge.spiral.core;

import java.util.Stack;

import org.newdawn.slick.Color;

public class Props {

  private Stack<Float> floats;
  private Stack<Boolean> bools;
  private Stack<Color> colors;
  private Stack<Integer> ints;
  private Stack<String> strings;
  
  public Props() {
    
  }

  public boolean hasFloats() {
    return floats != null && !floats.empty();
  }

  public Stack<Float> getFloats() {
    if (floats == null) {
      floats = new Stack<Float>();
    }
    return floats;
  }

  public boolean hasBools() {
    return bools != null && !bools.empty();
  }

  public Stack<Boolean> getBools() {
    if (bools == null) {
      bools = new Stack<Boolean>();
    }
    return bools;
  }

  public boolean hasColors() {
    return colors != null && !colors.empty();
  }
  
  public Stack<Color> getColors() {
    if (colors == null) {
      colors = new Stack<Color>();
    }
    return colors;
  }

  public boolean hasInts() {
    return ints != null && !ints.empty();
  }
  
  public Stack<Integer> getInts() {
    if (ints == null) {
      ints = new Stack<Integer>();
    }
    return ints;
  }

  public boolean hasStrings() {
    return strings != null && !strings.empty();
  }
  
  public Stack<String> getStrings() {
    if (strings == null) {
      strings = new Stack<String>();
    }
    return strings;
  }

  public void reset() {
    if (floats != null) floats.clear();
    if (bools != null) bools.clear();
    if (colors != null) colors.clear();
    if (ints != null) ints.clear();
    if (strings != null) strings.clear();
  }

}
