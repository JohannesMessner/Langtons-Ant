package singleant;

/**
 * Class representing the state of a Cell.
 * Saves the number of times the Cell has been visited by an Ant
 * and if an Ant is currently on the Cell.
 */
public class State {
  private boolean hasAnt;
  private int timesVisited;

  /**
   * Constructor initializing the instance-variables with default values.
   */
  public State() {
    this.hasAnt = false;
    this.timesVisited = 0;
  }

  /**
   * Constructor setting hasAnt and positionInCycle with custom values.
   *
   * @param hasAnt         boolean indicating the presence of an Ant
   * @param positonInCycle int of numbers the Cell has been visited by an Ant
   */
  State(boolean hasAnt, int positonInCycle) {
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
