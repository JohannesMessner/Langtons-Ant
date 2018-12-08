import java.util.HashMap;
import java.util.Objects;

public class Coordinate {

  private int x;
  private int y;

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public Coordinate(int x, int y){
    this.x = x;
    this.y = y;
  }

  @Override
  public int hashCode(){
    return Objects.hash(x,y);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Coordinate)){
      return false;
    }
    Coordinate c = (Coordinate) obj;
    return c.getX() == this.getX() && c.getY() == this.getY();
  }
}
