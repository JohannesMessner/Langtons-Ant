public class State {
  private boolean hasAnt;
  private int timesVisited;

  public State() {
    this.hasAnt = false;
    this.timesVisited = 0;
  }

  public State(boolean hasAnt, int positonInCycle) {
    this.hasAnt = hasAnt;
    this.timesVisited = positonInCycle;
  }

  public boolean hasAnt() {
    return hasAnt;
  }

  public int getTimesVisited() {
    return timesVisited;
  }
}
