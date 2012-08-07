package net.faintedge.spiral.fx;

public class GameTimer {
  
  private long time;
  
  public String getString() {
    return String.format("%02d:%02d:%02d", getHours(), getMinutes(), getSeconds());
  }
  
  public int getHours() {
    return (int) (time / (1000 * 60 * 60)) % 24;
  }
  
  public int getMinutes() {
    return (int) (time / (1000 * 60)) % 60;
  }
  
  public int getSeconds() {
    return (int) (time / 1000) % 60;
  }
  
  /**
   * @param deltaTime time to advance in milliseconds
   */
  public void advance(int deltaTime) {
    this.time += deltaTime;
  }

  public float getHoursFloat() {
    return (time / (1000.0f * 60.0f * 60.0f)) % 24.0f;
  }

}
