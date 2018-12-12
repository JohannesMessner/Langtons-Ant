package singleant;

/**
 * Enum-class representing the directions the Ant can be facing.
 */
public enum Direction {
  UP{
    @Override
    public Direction turnLeft(){
      return values()[values().length - 1];
    }
  },
  RIGHT,
  DOWN,
  LEFT{
    @Override
    public Direction turnRight(){
      return values()[0];
    }
  };

  public Direction turnRight(){
    return values()[ordinal() + 1];
  }

  public Direction turnLeft(){
    return values()[ordinal() - 1];
  }
}
