package singleant;

import antconst.Const;

public class Ant {

  //private static final Ant ant = new Ant();
  private Direction orientation;
  private int x;
  private int y;

  public Ant() {
    this.orientation = Const.DEFAULT_ANT_DIRECTION;
    reposition(Const.DEFAUL_ANT_POSITION);
  }

  void reposition(int x, int y) {
    this.x = x;
    this.y = y;
  }

  void reposition(Coordinate cor) {
    this.x = cor.getX();
    this.y = cor.getY();
  }

//  public static Ant getInstance() {
//    return ant;
//  }

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

  public Direction getOrientation() {
    return orientation;
  }

  void stepForward() {
    if (orientation == Direction.UP) {
      y--;
    } else if (orientation == Direction.RIGHT) {
      x++;
    } else if (orientation == Direction.DOWN) {
      y++;
    } else if (orientation == Direction.LEFT) {
      x--;
    }
  }

  Coordinate getCoordinates() {
    return new Coordinate(this.x, this.y);
  }

  int getX() {
    return this.x;
  }

  int getY() {
    return this.y;
  }
}
