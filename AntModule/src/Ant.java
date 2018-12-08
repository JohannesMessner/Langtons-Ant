public class Ant {

  private static final Ant ant = new Ant();
  private Direction orientation;
  private int x;
  private int y;

  private Ant() {
    this.orientation = Constants.DEFAULT_ANT_DIRECTION;
    reposition(Constants.DEFAUL_ANT_POSITION);
  }

  public void reposition(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void reposition(Coordinate cor) {
    this.x = cor.getX();
    this.y = cor.getY();
  }

  public static Ant getInstance() {
    return ant;
  }

  public void rotate(boolean turnDir) {
    if (turnDir == Constants.LEFT) {
      turnLeft();
    } else if (turnDir == Constants.RIGHT) {
      turnRight();
    }
  }

  public void turnLeft() {
    orientation = orientation.turnLeft();
  }

  public void turnRight() {
    orientation = orientation.turnRight();
  }

  public Direction getOrientation() {
    return orientation;
  }

  public void stepForward() {
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

  public Coordinate getCoordinates() {
    return new Coordinate(this.x, this.y);
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }
}
