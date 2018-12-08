public class Ant {

  private Direction orientation;

  public Ant(){
    this.orientation = Direction.LEFT;
  }

  public void turnLeft(){
    orientation = orientation.turnLeft();
  }

  public void turnRight(){
    orientation = orientation.turnRight();
  }

  public Direction getOrientation() {
    return orientation;
  }
}
