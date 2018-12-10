public class AntCell implements Cell {

  private boolean hasAnt;
  private int timesVisited;
  private Coordinate cor;

  public AntCell(int x, int y) {
    this.hasAnt = false;
    this.timesVisited = 0;
    cor = new Coordinate(x, y);
  }

  public AntCell(Coordinate cor) {
    this.hasAnt = false;
    this.timesVisited = 0;
    this.cor = new Coordinate(cor.getX(), cor.getY());
  }

  public AntCell(boolean hasAnt, Coordinate cor) {
    this(cor);
    this.hasAnt = hasAnt;
  }

  public AntCell(boolean hasAnt, int x, int y) {
    this(x, y);
    this.hasAnt = hasAnt;
  }

  public Coordinate getCoordinates() {
    return this.cor;
  }


  @Override
  public State getState() {
    return new State(hasAnt, timesVisited);
  }

  public void updateState() {
    if (this.hasAnt) {
      timesVisited++;
    }
    this.hasAnt = !this.hasAnt;
  }

  public void revertState() {
    if (!this.hasAnt) {
      timesVisited--;
    }
    this.hasAnt = !this.hasAnt;
  }

  public void setAnt(boolean hasAnt) {
    this.hasAnt = hasAnt;
  }
}
