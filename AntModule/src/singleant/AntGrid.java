package singleant;

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

  /**
   * Constructor that initializes all the Grid's variables and constants.
   *
   * @param height        int height of the grid
   * @param width         int width of the grid
   * @param configuration boolean[] representing the turning-configuration
   *                      of the Grid's Ant
   */
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

//    boolean steppedOutDown = y >= applyYOffset(currentHeight);
//    boolean steppedOutUp = y < applyYOffset(0);
//    boolean steppedOutLeft = x < applyXOffset(0);
//    boolean steppedOutRight = x >= applyXOffset(currentWidth);
//
//    if (steppedOutUp) {
//      y = applyYOffset(currentHeight - 1);
//      System.out.println("up");
//    } else if (steppedOutDown) {
//      y = applyYOffset(0);
//      System.out.println("down");
//    }
//    if (steppedOutLeft) {
//      x = applyXOffset(currentWidth - 1);
//      System.out.println("left");
//    } else if (steppedOutRight) {
//      x = applyXOffset(0);
//      System.out.println("right");
//    }

    boolean steppedOutDown = y >= currentHeight;
    boolean steppedOutUp = y < 0;
    boolean steppedOutLeft = x < 0;
    boolean steppedOutRight = x >= currentWidth;

    if (steppedOutUp) {
      y = currentHeight - 1;
      //System.out.println("up");
    } else if (steppedOutDown) {
      y = 0;
      //System.out.println("down");
    }
    if (steppedOutLeft) {
      x = currentWidth - 1;
      //System.out.println("left");
    } else if (steppedOutRight) {
      x = 0;
      //System.out.println("right");
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
    return currentWidth;
  }

  /**
   * Returns the height of the Grid.
   *
   * @return int representing the height of the Grid.
   */
  @Override
  public int getHeight() {
    return currentHeight;
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
    List<Cell> lst = new ArrayList<>(currentHeight);

    for (int j = 0; j < currentHeight; j++) {
      int y = applyYOffset(j);
      Cell c = playingField.get(new Coordinate(i, y));
      if (c == null) {
        lst.add(new AntCell(i, y));
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
    List<Cell> lst = new ArrayList<>(currentWidth);

    for (int i = 0; i < currentWidth; i++) {
      int x = applyXOffset(i);
      Cell c = playingField.get(new Coordinate(x, j));
      if (c == null) {
        lst.add(new AntCell(x, j));
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
    if (y < 0) {
      y = currentHeight + y;
    }
    y = y % currentHeight;

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
    if (x < 0) {
      x = currentWidth + x;
    }
    x = x % currentWidth;

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
    int yDiff = currentHeight - rows;
    int xDiff = currentWidth - cols;
    this.yOffset += yDiff / 2;
    this.xOffset += xDiff / 2;
    int oldHeight = currentHeight;
    int oldWidth = currentWidth;
    this.currentHeight = rows;
    this.currentWidth = cols;
    wrapGrid(xDiff / 2, yDiff / 2, oldWidth, oldHeight);
    deleteOutOfBoundsCells();
    deleteOutOfBoundsAnt();
  }

  private void wrapGrid(int numOfColumns, int numOfRows, int oldWidth, int oldHeight) {
//    int oldXOffset = this.xOffset - numOfColumns;
//    int oldYOffset = this.yOffset - numOfRows;

    if (numOfColumns > 0) {
      wrapX(numOfColumns);
    } else {
      unwrapX(numOfColumns, oldWidth);
    }
    if (numOfRows > 0) {
      wrapY(numOfRows);
    } else {
      unwrapY(numOfRows, oldHeight);
    }
    if (numOfColumns > 0 && numOfRows > 0) {
      wrapXY(numOfColumns, numOfRows);
    } else {
      //unwrapXY();
    }
  }

  private void unwrapX(int numOfColumns, int oldWidth) {
    int oldXOffset = this.xOffset - numOfColumns;
    if (numOfColumns > 0 || oldXOffset <= 0) {
      return;
    }

    numOfColumns = - numOfColumns;
    int columnsToMove = Math.max(numOfColumns, oldXOffset);

//    for (int x = 0; x < columnsToMove; x++) {
//      for (int y = 0; y < currentHeight + 1; y++) {
//
//        Cell c = playingField.get(new Coordinate(x, y));
//        if (c != null) {
//          State st = c.getState();
//          playingField.remove(new Coordinate(x, y));
//          if (st.hasAnt()) {
//            ant.reposition(oldWidth + x, y);
//          }
//        }
//        playingField.put(new Coordinate(oldWidth + x , y), c);
//      }
//    }


    for (int i = 0; i < numOfColumns * 2 + 1; i++) {
      for (int x = 0; x < oldXOffset; x++) {
        for (int y = 0; y < currentHeight + 1; y++) {

          Cell c1 = playingField.get(new Coordinate(x, y));
          int newX = x - 1;
          if (newX < 0) {
            newX = oldWidth + i;
          }
          if (c1 != null) {
            State st = c1.getState();
            playingField.remove(new Coordinate(x, y));
            if (st.hasAnt()) {
              ant.reposition(newX, y);
            }
          }
          playingField.put(new Coordinate(newX, y), c1);
        }
      }
    }
  }

  private void unwrapY(int numOfRows, int oldHeight) {
    int oldYOffset = this.yOffset - numOfRows;
    if (numOfRows > 0 || oldYOffset <= 0) {
      return;
    }

    numOfRows = - numOfRows;
    int rowsToMove = Math.max(numOfRows, oldYOffset);
//    for (int y = 0; y < rowsToMove; y++) {
//      for (int x = 0; x < currentWidth + 1; x++) {
//
//        Cell c = playingField.get(new Coordinate(x, y));
//        if (c != null) {
//          State st = c.getState();
//          playingField.remove(new Coordinate(x, y));
//          if (st.hasAnt()) {
//            ant.reposition(x, oldHeight + y);
//          }
//        }
//        playingField.put(new Coordinate(x, oldHeight + y), c);
//      }
//    }

    for (int i = 0; i < numOfRows * 2 + 1; i++) {
      for (int y = 0; y < oldYOffset; y++) {
        for (int x = 0; x < currentWidth + 1; x++) {

          Cell c1 = playingField.get(new Coordinate(x, y));
          int newY = y - 1;
          if (newY < 0) {
            newY = oldHeight + i;
          }
          if (c1 != null) {
            State st = c1.getState();
            playingField.remove(new Coordinate(x, y));
            if (st.hasAnt()) {
              ant.reposition(x, newY);
            }
          }
          playingField.put(new Coordinate(x, newY), c1);
        }
      }
    }

  }

  private void wrapXY(int numOfColumns, int numOfRows) {
    if (numOfColumns <= 0 || numOfRows <= 0) {
      return;
    }
    for (int x = currentWidth; x < currentHeight + numOfColumns; x++) {
      for (int y = currentHeight; y < currentHeight + numOfRows; y++) {
        Cell c = playingField.get(new Coordinate(x, y));
        if (c != null && c.getState().hasAnt()) {
          ant.reposition(x - currentWidth, y - currentHeight);
        }
        playingField.put(new Coordinate(x - currentWidth, y - currentHeight), c);
      }
    }
  }

  private void wrapX(int numOfColumns) {
    if (numOfColumns <= 0) {
      return;
    }
    for (int x = currentWidth; x < currentWidth + numOfColumns; x++) {
      for (int y = 0; y < currentHeight + 1; y++) {
        Cell c = playingField.get(new Coordinate(x, y));
        if (c != null && c.getState().hasAnt()) {
          ant.reposition(x - currentWidth, y);
        }
        playingField.put(new Coordinate(x - currentWidth, y), c);
      }
    }
  }

  private void wrapY(int numOfRows) {
    if (numOfRows <= 0) {
      return;
    }
    for (int y = currentHeight; y < currentHeight + numOfRows; y++) {
      for (int x = 0; x < currentWidth + 1; x++) {
        Cell c = playingField.get(new Coordinate(x, y));
        if (c != null && c.getState().hasAnt()) {
          ant.reposition(x, y - currentHeight);
        }
        playingField.put(new Coordinate(x, y - currentHeight), c);
      }
    }


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
    boolean xOutOfBounds = ant.getX() >= currentWidth;
    boolean yOutOfBounds = ant.getY() >= currentHeight;

    if (xOutOfBounds || yOutOfBounds) {
      /*
      Here it is not necessary to call clearAnts() since Cells that are
      out of bounds will be deleted with a call of deleteOutOfBoundsCells().
      This spares us from iterating over all the Cells.
      Modify accordingly if you want to call this method independently of
      deleteOutOfBoundsCells().
       */
      this.ant = null;
      //clearAnts();
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
      boolean xOutOfBounds = cor.getX() >= applyXOffset(currentWidth)
              || cor.getX() < applyXOffset(0);
      boolean yOutOfBounds = cor.getY() >= applyYOffset(currentHeight)
              || cor.getY() < applyYOffset(0);

      if (xOutOfBounds || yOutOfBounds) {
//        if (playingField.get(cor).getState().hasAnt()) {
//          this.ant = null;
//        }
        outOfBoundsCells.add(cor);
      }
    }
    return outOfBoundsCells;
  }

  private List<Coordinate> getOutOfBoundsCellsB() {

    List<Coordinate> outOfBoundsCells = new ArrayList<>();
    for (Coordinate cor : playingField.keySet()) {
      boolean xOutOfBounds = cor.getX() > currentWidth;
      boolean yOutOfBounds = cor.getY() > currentHeight;

      if (xOutOfBounds || yOutOfBounds) {
//        if (playingField.get(cor).getState().hasAnt()) {
//          this.ant = null;
//        }
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
