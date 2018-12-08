public class AntCell implements Cell {

  private boolean hasAnt;
  private int positionInCycle;

  public AntCell() {
    this.hasAnt = false;
    this.positionInCycle = 0;
  }

  @Override
  public State getState() {
    return new State(hasAnt, positionInCycle);
  }

  public void updateState() {
    if (!this.hasAnt) {
      positionInCycle++;
    }
    this.hasAnt = !this.hasAnt;
  }
}
