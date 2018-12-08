import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AntGrid implements Grid {

  private HashMap<Coordinate, Cell> playingField;
  private Ant ant;
  private int currentHeight;
  private int currentWidth;
  private final boolean[] configuration;
  private int xOffset;
  private int yOffset;
  private int stepCount;


  public AntGrid(int height, int width, boolean[] configuration) {
    this.currentHeight = height;
    this.currentWidth = width;
    this.configuration = configuration;
    this.playingField = new HashMap<>();
    this.xOffset = 0;
    this.yOffset = 0;
    this.stepCount = 0;
  }

  @Override
  public void setAnt(Ant object, int col, int row) {
    if (object == null) {
      if (this.ant != null) {
        ((AntCell) playingField.get(this.ant.getCoordinates())).updateState();
        this.ant = null;
      }
      return;
    }
    col = applyYOffset(col);
    row = applyXOffset(row);
    this.ant = object;
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
    AntCell oldCell = (AntCell) playingField.get(ant.getCoordinates());
    oldCell.updateState();

    ant.stepForward();
    int antX = ant.getX() % currentWidth;
    int antY = ant.getY() % currentHeight;
    ant.reposition(antX, antY);

    Coordinate cor = ant.getCoordinates();
    AntCell newCell = (AntCell) playingField.get(cor);
    if (newCell == null) {
      newCell = new AntCell();
      ant.rotate(getRotationDir(newCell.getState().getPositionInCycle()));
      newCell.updateState();
      playingField.put(cor, newCell);
    } else {
      ant.rotate(getRotationDir(newCell.getState().getPositionInCycle()));
      newCell.updateState();
    }
    stepCount++;
  }

  private boolean getRotationDir(int i) {
    return configuration[i % configuration.length];
  }

  @Override
  public void performStep(int number) {
    for (int i = 0; i < number; i++) {
      performStep();
    }
  }

  @Override
  public void reset(int number) {

  }

  @Override
  public int getWidth() {
    return currentWidth;
  }

  @Override
  public int getHeight() {
    return currentHeight;
  }

  @Override
  public List getColumn(int i) {
    i = applyXOffset(i);
    List<Cell> lst = new ArrayList<Cell>(currentHeight);

    for (int j = 0; j < currentHeight; j++) {
      Cell c = playingField.get(new Coordinate(i, j));
      if (c == null) {
        lst.add(new AntCell());
      } else {
        lst.add(c);
      }
    }
    return lst;
  }

  @Override
  public List getRow(int j) {
    j = applyYOffset(j);
    List<Cell> lst = new ArrayList<Cell>(currentHeight);

    for (int i = 0; i < currentHeight; i++) {
      Cell c = playingField.get(new Coordinate(i, j));
      if (c == null) {
        lst.add(new AntCell());
      } else {
        lst.add(c);
      }
    }
    return lst;
  }

  private int applyYOffset(int y){
    y -= yOffset;
    if (y < 0){
      y = currentHeight + y;
    }
    return y;
  }

  private int applyXOffset(int x){
    x -= xOffset;
    if (x < 0){
      x = currentWidth + x;
    }
    return x;
  }

  @Override
  public void resize(int cols, int rows) {
    int yDiff = currentHeight - rows;
    int xDiff = currentWidth - cols;
    this.yOffset += yDiff / 2;
    this.xOffset += xDiff / 2;
    this.currentHeight = rows;
    this.currentWidth = cols;
  }

  @Override
  public void clear() {
    for (int i = 0; i < currentWidth; i++){
      for (int j = 0; j < currentHeight; j++){
        Coordinate cor = new Coordinate(i, j);
        Cell cell = playingField.get(cor);
        if (cell != null){
          playingField.put(cor, new AntCell());
        }
      }
    }
    this.ant = null;
    this.stepCount = 0;
  }

  @Override
  public int getStepCount() {
    return stepCount;
  }
}
