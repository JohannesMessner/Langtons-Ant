package singleant;

import java.util.Objects;

/**
 * Class representing a coordinate on the Grid.
 */
public class Coordinate {

  private int xcor;
  private int ycor;

  int getX() {
    return xcor;
  }

  int getY() {
    return ycor;
  }

  /**
   * Constructor setting a x ans y coordinate.
   *
   * @param x int x-coordinate
   * @param y int y-coordinate
   */
  public Coordinate(int x, int y) {
    this.xcor = x;
    this.ycor = y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(xcor, ycor);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Coordinate)) {
      return false;
    }
    Coordinate c = (Coordinate) obj;
    return c.getX() == this.getX() && c.getY() == this.getY();
  }
}
