package singleant;

import antconst.Const;

/**
 * Class representing an Ant that can be placed on a Grid.
 * Has an orientation and a position.
 */
public class Ant {

  private Direction orientation;
  private int xcor;
  private int ycor;

  /**
   * Constructor that assigns a default direction and position.
   */
  public Ant() {
    this.orientation = Const.DEFAULT_ANT_DIRECTION;
    reposition(Const.DEFAUL_ANT_POSITION);
  }

  public Ant(Direction dir, Coordinate cor) {
    this.orientation = dir;
    reposition(cor);
  }

  /**
   * repositions the ant.
   *
   * @param x int of new x-coordinate
   * @param y int of new y-coordinate
   */
  void reposition(int x, int y) {
    this.xcor = x;
    this.ycor = y;
  }

  /**
   * repostions the ant.
   *
   * @param cor new Coordinate
   */
  void reposition(Coordinate cor) {
    this.xcor = cor.getX();
    this.ycor = cor.getY();
  }

  /**
   * rotates the Ant in a given direction.
   *
   * @param turnDir boolean representing a left ot right turn
   *                according to the convention defined in antconst.Const
   */
  void rotate(boolean turnDir) {
    if (turnDir == Const.LEFT) {
      turnLeft();
    } else if (turnDir == Const.RIGHT) {
      turnRight();
    }
  }

  private void turnLeft() {
    orientation = orientation.turnLeft();
  }

  private void turnRight() {
    orientation = orientation.turnRight();
  }

  /**
   * returns the Direction of the ant.
   *
   * @return Direction of the ant
   */
  public Direction getOrientation() {
    return orientation;
  }

  void stepForward() {
    if (orientation == Direction.UP) {
      ycor--;
    } else if (orientation == Direction.RIGHT) {
      xcor++;
    } else if (orientation == Direction.DOWN) {
      ycor++;
    } else if (orientation == Direction.LEFT) {
      xcor--;
    }
  }

  /**
   * returns the coordinates of the ant.
   *
   * @return Coordinate of the ant.
   */
  Coordinate getCoordinates() {
    return new Coordinate(this.xcor, this.ycor);
  }

  int getX() {
    return this.xcor;
  }

  int getY() {
    return this.ycor;
  }
}
