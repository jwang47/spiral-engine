package net.faintedge.spiral.networked.sync.control;


public class PhysicsMoveSync extends ControlSync {

  private boolean north, south, west, east;

  public void setValues(boolean north, boolean south, boolean west, boolean east) {
    this.north = north;
    this.south = south;
    this.west = west;
    this.east = east;
  }
  
  public boolean isNorth() {
    return north;
  }

  public void setNorth(boolean north) {
    this.north = north;
  }

  public boolean isSouth() {
    return south;
  }

  public void setSouth(boolean south) {
    this.south = south;
  }

  public boolean isWest() {
    return west;
  }

  public void setWest(boolean west) {
    this.west = west;
  }

  public boolean isEast() {
    return east;
  }

  public void setEast(boolean east) {
    this.east = east;
  }

}
