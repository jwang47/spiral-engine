package net.faintedge.spiral.core;

public class Vector2i {
  
  private int x;
  private int y;
  
  public Vector2i() {
    this.x = 0;
    this.y = 0;
  }

  public Vector2i(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Vector2i setX(int x) {
    this.x = x;
    return this;
  }

  public Vector2i setY(int y) {
    this.y = y;
    return this;
  }

  public Vector2i add(int x, int y) {
    this.x += x;
    this.y += y;
    return this;
  }

  public Vector2i set(Vector2i other) {
    this.x = other.x;
    this.y = other.y;
    return this;
  }

  public Vector2i set(int x, int y) {
    this.x = x;
    this.y = y;
    return this;
  }

  public Vector2i add(Vector2i other) {
    this.x += other.x;
    this.y += other.y;
    return this;
  }
  
  public Vector2i sub(int x, int y) {
    this.x -= x;
    this.y -= y;
    return this;
  }

  public Vector2i add(Vector2i other, int multiplier) {
    this.x += other.x * multiplier;
    this.y += other.y * multiplier;
    return this;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public Vector2i limitToMaxAndMin(Vector2i limit) {
    limitToMax(limit, 1);
    limitToMin(limit, -1);
    return this;
  }
  
  public Vector2i limitToMax(Vector2i limit, int multiplier) {
    this.x = Math.min(x, multiplier * limit.getX());
    this.y = Math.min(y, multiplier * limit.getY());
    return this;
  }
  
  public Vector2i limitToMin(Vector2i limit, int multiplier) {
    this.x = Math.max(x, multiplier * limit.getX());
    this.y = Math.max(y, multiplier * limit.getY());
    return this;
  }

  public Vector2i mult(int multiplier) {
    this.x *= multiplier;
    this.y *= multiplier;
    return this;
  }

  public Vector2i sub(Vector2i other) {
    this.x -= other.x;
    this.y -= other.y;
    return this;
  }
}
