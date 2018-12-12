package antconst;

import singleant.Coordinate;
import singleant.Direction;

/**
 * Class for constants of the whole program.
 */
public final class Const {
  public static final boolean LEFT = true;
  public static final boolean RIGHT = false;
  public static final Direction DEFAULT_ANT_DIRECTION = Direction.LEFT;
  public static final Coordinate DEFAUL_ANT_POSITION = new Coordinate(0, 0);
  public static final int MAX_CONFIGS = 12;

  public static final String[] COMMANDS = new String[]{
          "new", "ant", "unant", "step", "print", "clear", "resize", "help", "quit"
  };
  public static final int NEW = 0;
  public static final int ANT = 1;
  public static final int UNANT = 2;
  public static final int STEP = 3;
  public static final int PRINT = 4;
  public static final int CLEAR = 5;
  public static final int RESIZE = 6;
  public static final int HELP = 7;
  public static final int QUIT = 8;
  public static final int NO_COMMAND = -1;
  public static final int INVALID_COMMAND = -2;

  public static final String ANSI_RESET = "\u001B[0m";
  public static final String COLOR_0 = "\u001B[47m";
  public static final String COLOR_1 = "\u001B[37;40m";
  public static final String COLOR_2 = "\u001B[42m";
  public static final String COLOR_3 = "\u001B[41m";
  public static final String COLOR_4 = "\u001B[37;44m";
  public static final String COLOR_5 = "\u001B[43m";
  public static final String COLOR_6 = "\u001B[46m";
  public static final String COLOR_7 = "\u001B[45m";
  public static final String COLOR_8 = "\u001B[36;41m";
  public static final String COLOR_9 = "\u001B[31;44m";
  public static final String COLOR_10 = "\u001B[34;43m";
  public static final String COLOR_11 = "\u001B[32;45m";

  public static final String NO_WIDTH_ERROR = "Error! Your need to specify a width for the grid";
  public static final String NO_HEIGHT_ERROR = "Error! Your need to specify a height for the grid";
  public static final String NO_CONFIG_ERROR = "Error! Your need to specify a configuration for the grid";
  public static final String INVALID_CONFIG_ERROR = "Error! Configuration must only consist of 'L' and 'R'";
  public static final String NO_X_ERROR = "Error! You must specify a x-coordinate for the ant";
  public static final String NO_Y_ERROR = "Error! You must specify a y-coordinate for the ant";
  public static final String INVALID_COMMAND_ERROR = "Error! The command is invalid";
  public static final String NO_COMMAND_ERROR = "Error! You must specify a command";
  public static final String NO_ANT_ERROR = "Error! There is no ant an the grid";

  public static final String HELP_MESSAGE = "Help message goes here";

}
