package antconst;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import singleant.Coordinate;
import singleant.Direction;


/**
 * Class for constants of the whole Langton's-Ant-program.
 */
public final class Const {
  public static final boolean LEFT = true;
  public static final boolean RIGHT = false;
  public static final Direction DEFAULT_ANT_DIRECTION = Direction.LEFT;
  public static final Coordinate DEFAUL_ANT_POSITION = new Coordinate(0, 0);
  public static final int MAX_CONFIGS = 12;

  public static final List<String> COMMANDS = Collections.unmodifiableList(new ArrayList<String>() {
    {
      add("new");
      add("ant");
      add("unant");
      add("step");
      add("print");
      add("clear");
      add("resize");
      add("help");
      add("quit");
    }
  });

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
  public static final String NO_HEIGHT_ERROR = "Error! Your need to specify "
          + "a height for the grid";
  public static final String NO_CONFIG_ERROR = "Error! Your need to specify "
          + "a configuration for the grid";
  public static final String INVALID_CONFIG_ERROR = "Error! Configuration must "
          + "only consist of 'L' and 'R'";
  public static final String NO_X_ERROR = "Error! You must specify a x-coordinate for the ant";
  public static final String NO_Y_ERROR = "Error! You must specify a y-coordinate for the ant";
  public static final String INVALID_COMMAND_ERROR = "Error! The command is invalid";
  public static final String NO_COMMAND_ERROR = "Error! You must specify a command";
  public static final String NO_ANT_ERROR = "Error! There is no ant on the grid";

  public static final String HELP_MESSAGE = "This is Langton's Ant!\n"
          + "You can use the following commands:\n"
          + "NEW x y c: creates a new grid of size x*y with a configuration c\n"
          + "c must consist of the letters L and R, to indicate turning directions\n"
          + "ANT i j: places an ant on the cell with coordinates (i,j)\n"
          + "UNANT: deletes the ant from the grid\n"
          + "STEP [n]: performs n steps, or one step if no n is given\n"
          + "PRINT: prints the grid\n"
          + "CLEAR: clears the grid\n"
          + "RESIZE x y: resizes the grid to the format x*y\n"
          + "HELP: prints this message\n"
          + "QUIT: quits the program";

}
