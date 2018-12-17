package singleant;

/**
 * Class representing a Cell that can be component of a Grid.
 * Has a coordinate, counts the number it has been visited by an Ant
 * and can indicate whether and Ant is currently on the Cell.
 */
public class AntCell implements Cell {

  private boolean hasAnt;
  private int timesVisited;
  private Coordinate cor;

  /**
   * Constructor assigning a Coordinate and no Ant.
   *
   * @param x int x-coordinate
   * @param y int y-coordinate
   */
  AntCell(int x, int y) {
    this.hasAnt = false;
    this.timesVisited = 0;
    cor = new Coordinate(x, y);
  }

  /**
   * Constructor assigning a Coordinate and no Ant.
   *
   * @param cor Coordinate of the cell
   */
  AntCell(Coordinate cor) {
    this.hasAnt = false;
    this.timesVisited = 0;
    this.cor = new Coordinate(cor.getX(), cor.getY());
  }

  /**
   * Constructor assigning a Coordinate and whether the cell has an ant.
   *
   * @param hasAnt boolean indicating the presence of an ant
   * @param cor    Coordinate of the cell
   */
  AntCell(boolean hasAnt, Coordinate cor) {
    this(cor);
    this.hasAnt = hasAnt;
  }

  /**
   * Constructor assigning a Coordinate and whether the cell has an ant.
   *
   * @param hasAnt boolean indicating the presence of an ant
   * @param x      int x-coordinate of the cell
   * @param y      int y-coordinate of the cell
   */
  public AntCell(boolean hasAnt, int x, int y) {
    this(x, y);
    this.hasAnt = hasAnt;
  }

  /**
   * returns the Cell's Coordinate.
   *
   * @return Coordinate of the cell
   */
  Coordinate getCoordinates() {
    return this.cor;
  }

  /**
   * returns the Cell's State.
   *
   * @return State of the Cell
   */
  @Override
  public State getState() {
    return new State(hasAnt, timesVisited);
  }

  /**
   * Updates the Cell's hasAnt and timesVisited values.
   * Should be called when an Ant steps onto or off of the Cell.
   */
  void updateState() {
    if (this.hasAnt) {
      timesVisited++;
    }
    this.hasAnt = !this.hasAnt;
  }

  /**
   * Reverts the Cell's hasAnt and timesVisited values.
   * Should be called when resetting the grid.
   */
  void revertState() {
    if (!this.hasAnt) {
      timesVisited--;
    }
    this.hasAnt = !this.hasAnt;
  }

  /**
   * Changes whether the Cell has an Ant or not.
   *
   * @param hasAnt boolean indicating the presence of an Ant
   */
  void setAnt(boolean hasAnt) {
    this.hasAnt = hasAnt;
  }
}
