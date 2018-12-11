package singleant;

public class AntCell implements Cell {

  private boolean hasAnt;
  private int timesVisited;
  private Coordinate cor;

  AntCell(int x, int y) {
    this.hasAnt = false;
    this.timesVisited = 0;
    cor = new Coordinate(x, y);
  }

  AntCell(Coordinate cor) {
    this.hasAnt = false;
    this.timesVisited = 0;
    this.cor = new Coordinate(cor.getX(), cor.getY());
  }

  AntCell(boolean hasAnt, Coordinate cor) {
    this(cor);
    this.hasAnt = hasAnt;
  }

  public AntCell(boolean hasAnt, int x, int y) {
    this(x, y);
    this.hasAnt = hasAnt;
  }

  Coordinate getCoordinates() {
    return this.cor;
  }


  @Override
  public State getState() {
    return new State(hasAnt, timesVisited);
  }

  void updateState() {
    if (this.hasAnt) {
      timesVisited++;
    }
    this.hasAnt = !this.hasAnt;
  }

  void revertState() {
    if (!this.hasAnt) {
      timesVisited--;
    }
    this.hasAnt = !this.hasAnt;
  }

  void setAnt(boolean hasAnt) {
    this.hasAnt = hasAnt;
  }
}
