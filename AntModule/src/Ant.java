public class Ant {

  private static final Ant ant = new Ant();
  private Direction orientation;
  private Coordinate cor;

  private Ant() {
    this.orientation = Direction.LEFT;
    this.cor = new Coordinate(0, 0);
  }

  public void reposition(int x, int y) {
    this.cor = new Coordinate(x, y);
  }

  public static Ant getInstance() {
    return ant;
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
}
