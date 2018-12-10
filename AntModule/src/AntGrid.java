import java.util.*;

public class AntGrid implements Grid {

  private HashMap<Coordinate, Cell> playingField;
  private Ant ant;
  private int currentHeight;
  private int currentWidth;
  private final boolean[] configuration;
  private int xOffset;
  private int yOffset;
  private int stepCount;
  private List<Cell> cellHistory;


  public AntGrid(int height, int width, boolean[] configuration) {
    this.currentHeight = height;
    this.currentWidth = width;
    this.configuration = Arrays.copyOf(configuration, configuration.length);
    this.playingField = new LinkedHashMap<>();
    this.cellHistory = new LinkedList<>();
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

    this.ant = object;
    col = applyYOffset(col);
    row = applyXOffset(row);
    ant.reposition(col, row);
    AntCell cell = new AntCell(true, ant.getCoordinates());
    playingField.put(ant.getCoordinates(), cell);
    cellHistory.add(0, cell);
  }

  @Override
  public Map<Coordinate, Ant> getAnts() {
    Map<Coordinate, Ant> m = new HashMap<>();
    m.put(ant.getCoordinates(), ant);
    return m;
  }

  @Override
  public void clearAnts() {
    this.ant = null;
    List<Cell> cellList = new ArrayList<Cell>(playingField.values());
    for (Cell c : cellList) {
      ((AntCell) c).setAnt(false);
    }
  }

  @Override
  public void performStep() {
    AntCell oldCell = (AntCell) playingField.get(ant.getCoordinates());
    oldCell.updateState();
    //cellHistory.add(0, oldCell);
    ant.stepForward();
    putBackOnGrid(ant);

    Coordinate cor = ant.getCoordinates();
    AntCell newCell = (AntCell) playingField.get(cor);
    if (newCell == null) {
      newCell = new AntCell(cor);
      ant.rotate(getRotationDir(newCell.getState().getTimesVisited()));
      newCell.updateState();
      playingField.put(cor, newCell);
    } else {
      ant.rotate(getRotationDir(newCell.getState().getTimesVisited()));
      newCell.updateState();
    }
    cellHistory.add(0, newCell);
    stepCount++;
  }

  private void putBackOnGrid(Ant ant) {
    int antX = (ant.getX() % currentWidth);
    int antY = (ant.getY() % currentHeight);
    if (antX < 0) {
      antX = currentWidth + antX;
    }
    if (antY < 0) {
      antY = currentHeight + antY;
    }
    ant.reposition(antX, antY);
  }

  private boolean getRotationDir(int i) {
    return configuration[i % configuration.length];
  }

  private boolean getInvertedRotationDir(int i) {
    return !getRotationDir(i);
  }

  @Override
  public void performStep(int number) {
    for (int i = 0; i < number; i++) {
      performStep();
    }
  }

  @Override
  public void reset(int number) {

    for (int i = 0; i < number; i++) {
      if (cellHistory.size() <= 1) {
        break;
      }

      AntCell lastCell = (AntCell) cellHistory.get(0);
      int lastCellVisited = lastCell.getState().getTimesVisited();
      ant.rotate(getInvertedRotationDir(lastCellVisited));
      lastCell.revertState();
      lastCellVisited = lastCell.getState().getTimesVisited();
      if (lastCellVisited == 0) {
        playingField.remove(lastCell.getCoordinates());
      }

      if (cellHistory.size() >= 2) {
        AntCell secondToLastCell = (AntCell) cellHistory.get(1);
        secondToLastCell.revertState();
        ant.reposition(secondToLastCell.getCoordinates());
      }

      cellHistory.remove(0);
      stepCount--;
    }

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
  public List<Cell> getColumn(int i) {
    i = applyXOffset(i);
    List<Cell> lst = new ArrayList<Cell>(currentHeight);

    for (int j = 0; j < currentHeight; j++) {
      Cell c = playingField.get(new Coordinate(i, j));
      if (c == null) {
        lst.add(new AntCell(i, j));
      } else {
        lst.add(c);
      }
    }
    return lst;
  }

  @Override
  public List<Cell> getRow(int j) {
    j = applyYOffset(j);
    List<Cell> lst = new ArrayList<Cell>(currentHeight);

    for (int i = 0; i < currentHeight; i++) {
      Cell c = playingField.get(new Coordinate(i, j));
      if (c == null) {
        lst.add(new AntCell(i, j));
      } else {
        lst.add(c);
      }
    }
    return lst;
  }

  private int applyYOffset(int y) {
    y -= yOffset;
    if (y < 0) {
      y = currentHeight + y;
    }
    return y;
  }

  private int applyXOffset(int x) {
    x -= xOffset;
    if (x < 0) {
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
    for (int i = 0; i < currentWidth; i++) {
      for (int j = 0; j < currentHeight; j++) {
        Coordinate cor = new Coordinate(i, j);
        Cell cell = playingField.get(cor);
        if (cell != null) {
          playingField.remove(cor);
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
