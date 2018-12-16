package singleant;

import java.util.*;

public class AntGrid implements Grid {

  private HashMap<Coordinate, Cell> playingField;
  private Ant ant;
  private int firstX;
  private int lastX;
  private int firstY;
  private int lastY;
  private final boolean[] configuration;
  private int xOffset;
  private int yOffset;
  private int stepCount;
  private List<Cell> cellHistory;

  /**
   * Constructor that initializes all the Grid's variables and constants.
   *
   * @param height        int height of the grid
   * @param width         int width of the grid
   * @param configuration boolean[] representing the turning-configuration
   *                      of the Grid's Ant
   */
  public AntGrid(int height, int width, boolean[] configuration) {
    this.firstX = 0;
    this.lastX = width - 1;
    this.firstY = 0;
    this.lastY = height - 1;
    this.configuration = Arrays.copyOf(configuration, configuration.length);
    this.playingField = new LinkedHashMap<>();
    this.cellHistory = new LinkedList<>();
    this.xOffset = 0;
    this.yOffset = 0;
    this.stepCount = 0;
  }

  /**
   * Puts an Ant on the Grid.
   *
   * @param object Ant that will be put on the Grid
   * @param col    int column(x)-index of the Ant's position on the Grid
   * @param row    int row(y)-index of the Ant's position on the Grid
   */
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

  /**
   * Returns all the Ants present on the Grid.
   * In this implementation this will always be a single Ant.
   *
   * @return Map of the Ants and their Coordinates
   */
  @Override
  public Map<Coordinate, Ant> getAnts() {
    Map<Coordinate, Ant> m = new HashMap<>();
    if (ant == null) {
      m.put(null, null);
      return m;
    }
    m.put(ant.getCoordinates(), ant);
    return m;
  }

  /**
   * Deletes all the Ants present on the Grid.
   */
  @Override
  public void clearAnts() {
    this.ant = null;
    List<Cell> cellList = new ArrayList<>(playingField.values());
    for (Cell c : cellList) {
      ((AntCell) c).setAnt(false);
    }
  }

  /**
   * Performs a step with the Ant present on the Grid.
   * All of the Grid's Cells will be updated accordingly.
   */
  @Override
  public void performStep() {
    AntCell oldCell = (AntCell) playingField.get(ant.getCoordinates());
    oldCell.updateState();
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

  /**
   * Repositions the Ant if it has "fallen off" the grid.
   * Simulates a torus-shaped Grid.
   *
   * @param ant Ant to be put back on the grid
   */
  private void putBackOnGrid(Ant ant) {

    int x = ant.getX();
    int y = ant.getY();

    boolean steppedOutDown = y > lastY;
    boolean steppedOutUp = y < firstY;
    boolean steppedOutLeft = x < firstX;
    boolean steppedOutRight = x > lastX;

    if (steppedOutUp) {
      y = lastY;
    } else if (steppedOutDown) {
      y = firstY;
    }
    if (steppedOutLeft) {
      x = lastX;
    } else if (steppedOutRight) {
      x = firstX;
    }
    ant.reposition(x, y);
  }

  /**
   * Returns the rotation-direction for the Ant.
   * Based on the configuration and a Cells timesVisited.
   *
   * @param i int indicating how many times a Cell has been visited by the Ant.
   * @return boolean representing the rotation
   * based on the Convention defined in antconst.Const
   */
  private boolean getRotationDir(int i) {
    return configuration[i % configuration.length];
  }

  /**
   * Returns the inverted rotation-direction for the Ant.
   * Should be called only when resetting the Grid.
   *
   * @param i int indicating how many times a Cell has been visited by the Ant.
   * @return boolean representing the rotation
   * based on the Convention defined in antconst.Const
   */
  private boolean getInvertedRotationDir(int i) {
    return !getRotationDir(i);
  }

  /**
   * Perfoms multiple steps at once.
   *
   * @param number int number of steps
   */
  @Override
  public void performStep(int number) {
    for (int i = 0; i < number; i++) {
      performStep();
    }
  }

  /**
   * Resets the grid to its state of a given number of steps ago.
   * Updates the Cell's States and the Ant's position on the Grid.
   *
   * @param number int number of steps backwards
   */
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

  /**
   * Returns the width of the Grid.
   *
   * @return int representing the width of the Grid.
   */
  @Override
  public int getWidth() {
    return lastX - firstX + 1;
  }

  /**
   * Returns the height of the Grid.
   *
   * @return int representing the height of the Grid.
   */
  @Override
  public int getHeight() {
    return lastY - firstY + 1;
  }

  /**
   * Returns a column of the Grid by a given index.
   *
   * @param i index of the column
   * @return List<Cell> of Cells in the column
   */
  @Override
  public List<Cell> getColumn(int i) {
    i = applyXOffset(i);
    List<Cell> lst = new ArrayList<>(getHeight());

    for (int j = firstY; j < lastY + 1; j++) {
      Cell c = playingField.get(new Coordinate(i, j));
      if (c == null) {
        lst.add(new AntCell(i, j));
      } else {
        lst.add(c);
      }
    }
    return lst;
  }

  /**
   * Returns a row of the Grid by a given index.
   *
   * @param j index of the row
   * @return List<Cell> of Cells in the row
   */
  @Override
  public List<Cell> getRow(int j) {
    j = applyYOffset(j);
    List<Cell> lst = new ArrayList<>(getWidth());

    for (int i = firstX; i < lastX + 1; i++) {
      Cell c = playingField.get(new Coordinate(i, j));
      if (c == null) {
        lst.add(new AntCell(i, j));
      } else {
        lst.add(c);
      }
    }
    return lst;
  }

  /**
   * Applies an offset to a y-coordinate.
   * Used to simulate a symmetrical Grid after resizing it.
   *
   * @param y int of the y-coordinate to be transformed
   * @return int transformed y-coordinate
   */
  private int applyYOffset(int y) {
    y += yOffset;
    return y;
  }

  /**
   * Applies an offset to an x-coordinate.
   * Used to simulate a symmetrical Grid after resizing it.
   *
   * @param x int of the x-coordinate to be transformed
   * @return int transformed x-coordinate
   */
  private int applyXOffset(int x) {
    x += xOffset;
    return x;
  }

  /**
   * Resizes the Grid.
   *
   * @param cols int new width of the grid
   * @param rows int new height of the grid.
   */
  @Override
  public void resize(int cols, int rows) {
    int yDiff = getHeight() - rows;
    int xDiff = getWidth() - cols;

    this.firstY = firstY + yDiff / 2;
    this.lastY = calculateLastY(yDiff);

    this.firstX = firstX + (xDiff / 2);
    this.lastX = calculateLastX(xDiff);

    this.yOffset += yDiff / 2;
    this.xOffset += xDiff / 2;

    deleteOutOfBoundsCells();
    deleteOutOfBoundsAnt();
  }

  private int calculateLastY(int yDiff) {
    int y;
    if (yDiff % 2 == 0) {
      y = lastY - yDiff / 2;
    } else {
      y = lastY - (yDiff / 2);
      if (yDiff < 0) {
        y++;
      } else if (yDiff > 0) {
        y--;
      }
    }
    return y;
  }

  private int calculateLastX(int xDiff) {
    int x;
    if (xDiff % 2 == 0) {
      x = lastX - xDiff / 2;
    } else {
      x = lastX - (xDiff / 2);
      if (xDiff < 0) {
        x++;
      } else if (xDiff > 0) {
        x--;
      }
    }
    return x;
  }


  /**
   * Deletes the Ant if it has "fallen off" the Grid.
   * <p>
   * Method is optimized to be called after a call of deleteOutOfBoundsCells().
   * Calling this Method without calling deleteOutOfBoundsCells() will result
   * in unwanted behaviour.
   * If you want to call this Method without calling deleteOutOfBoundsCells(),
   * you need to modify it slightly.
   */
  private void deleteOutOfBoundsAnt() {
    if (this.ant == null) {
      return;
    }
    boolean xOutOfBounds = ant.getX() > lastX || ant.getX() < firstX;
    boolean yOutOfBounds = ant.getY() > lastY || ant.getY() < firstY;

    if (xOutOfBounds || yOutOfBounds) {
      /*
      Here it is not necessary to call clearAnts() since Cells that are
      out of bounds will be deleted with a call of deleteOutOfBoundsCells().
      This spares us from iterating over all the Cells.
      Modify accordingly if you want to call this method independently of
      deleteOutOfBoundsCells().
       */
      this.ant = null;
    }
  }

  /**
   * Deletes all cells that have "fallen off" the Grid after a resize.
   */
  private void deleteOutOfBoundsCells() {

    List<Coordinate> outOfBoundsCells = getOutOfBoundsCellsB();

    for (Coordinate cor : outOfBoundsCells) {
      playingField.remove(cor);
    }
  }

  /**
   * Computes all the Cells that are out of bounds in regard to the current Grid.
   *
   * @return List of Coordinates of the out of bounds Cells
   */
  private List<Coordinate> getOutOfBoundsCells() {

    List<Coordinate> outOfBoundsCells = new ArrayList<>();
    for (Coordinate cor : playingField.keySet()) {
      boolean xOutOfBounds = cor.getX() >= applyXOffset(getWidth())
              || cor.getX() < applyXOffset(0);
      boolean yOutOfBounds = cor.getY() >= applyYOffset(getHeight())
              || cor.getY() < applyYOffset(0);

      if (xOutOfBounds || yOutOfBounds) {
        outOfBoundsCells.add(cor);
      }
    }
    return outOfBoundsCells;
  }

  private List<Coordinate> getOutOfBoundsCellsB() {

    List<Coordinate> outOfBoundsCells = new ArrayList<>();
    for (Coordinate cor : playingField.keySet()) {
      boolean xOutOfBounds = cor.getX() > lastX || cor.getX() < firstX;
      boolean yOutOfBounds = cor.getY() > lastY || cor.getY() < firstY;

      if (xOutOfBounds || yOutOfBounds) {
        outOfBoundsCells.add(cor);
      }
    }
    return outOfBoundsCells;
  }

  /**
   * Clears all Ants from the Grid and resets the Cells.
   */
  @Override
  public void clear() {
    for (int i = firstX; i < lastX + 1; i++) {
      for (int j = firstY; j < lastY + 1; j++) {
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

  /**
   * Returns the number of (forward) steps taken so far.
   *
   * @return int number of steps taken
   */
  @Override
  public int getStepCount() {
    return stepCount;
  }
}
