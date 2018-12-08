public class State {
  private boolean hasAnt;
  private int positionInCycle;

  public State() {
    this.hasAnt = false;
    this.positionInCycle = 0;
  }

  public State(boolean hasAnt, int positonInCycle) {
    this.hasAnt = hasAnt;
    this.positionInCycle = positonInCycle;
  }

  public boolean hasAnt() {
    return hasAnt;
  }

  public int getPositionInCycle() {
    return positionInCycle;
  }
}
