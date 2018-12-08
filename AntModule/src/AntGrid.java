import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AntGrid implements Grid {

  private HashMap<Coordinate, Cell> PlayingField;
  private Ant ant;
  private int currentHeight;
  private int currentWidth;
  private final boolean[] configuration;

  public AntGrid(int height, int width, boolean[] configuration){
    this.currentHeight = height;
    this.currentWidth = width;
    this.configuration = configuration;
  }

  @Override
  public void setAnt(Ant object, int col, int row) {
    ant = object;
    ant.reposition(col, row);
  }

  @Override
  public Map<Coordinate, Ant> getAnts() {
    return null;
  }

  @Override
  public void clearAnts() {

  }

  @Override
  public void performStep() {
    ant.stepForward();
    int antX = ant.getX() % currentWidth;
    int antY = ant.getY() % currentHeight;
    ant.reposition(antX, antY);

    Coordinate cor = ant.getCoordinates();
    AntCell cell = (AntCell) PlayingField.get(cor);
    if (cell == null){
      PlayingField.put(cor, new AntCell());
    }else {
      ant.rotate(getRotationDir(cell.getState().getPositionInCycle()));
      cell.updateState();
    }
  }

  private boolean getRotationDir(int i){
    return configuration[i % configuration.length];
  }

  @Override
  public void performStep(int number) {

  }

  @Override
  public void reset(int number) {

  }

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public int getHeight() {
    return 0;
  }

  @Override
  public List getColumn(int i) {
    return null;
  }

  @Override
  public List getRow(int j) {
    return null;
  }

  @Override
  public void resize(int cols, int rows) {

  }

  @Override
  public void clear() {

  }

  @Override
  public int getStepCount() {
    return 0;
  }
}
