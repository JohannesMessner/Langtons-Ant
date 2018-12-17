import antconst.Const;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import singleant.Ant;
import singleant.AntGrid;
import singleant.Cell;
import singleant.Direction;
import singleant.Grid;
import singleant.State;


/**
 * Class handling text-based I/O for a Langton's Ant program.
 * Draws and prints the Grid, including the Ant.
 */
public class Shell {

  private static Grid grid;
  private static Ant ant;
  private static boolean quit;
  private static Scanner sc;
  private static int configLen;

  private static final String PROMPT = "ant> ";

  /**
   * Main-method taking user commands and handling them.
   *
   * @param args Command-line arguments
   * @throws IOException Exception
   */
  public static void main(String[] args) throws IOException {
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
    quit = false;

    while (!quit) {
      System.out.print(PROMPT);

      String inputLine = stdin.readLine();
      if (inputLine == null) {
        quit = true;
      } else {
        handleCommand(inputLine);
      }
    }
  }

  /**
   * Handles commands an transforms them into method-calls of the underlying logic.
   *
   * @param inputLine line of user-input
   */
  private static void handleCommand(String inputLine) {
    sc = new Scanner(inputLine);
    sc.useDelimiter("\\s+");

    String command = "";
    if (sc.hasNext()) {
      command = sc.next();
    }

    int commandId = getCommandId(command);
    switch (commandId) {
      case Const.NEW:
        handleNew();
        break;

      case Const.ANT:
        handleAnt();
        break;

      case Const.UNANT:
        handleUnant();
        break;

      case Const.STEP:
        handleStep();
        break;

      case Const.CLEAR:
        handleClear();
        break;

      case Const.PRINT:
        handlePrint();
        break;

      case Const.RESIZE:
        handleResize();
        break;

      case Const.HELP:
        handleHelp();
        break;

      case Const.QUIT:
        handleQuit();
        break;

      case Const.INVALID_COMMAND:
        handleInvalid();
        break;

      case Const.NO_COMMAND:
        handleNoCommand();
        break;

      default:
        handleInvalid();
        break;
    }
    sc.close();
  }

  private static void handleNew() {
    int x = 0;
    int y = 0;
    boolean[] config;
    if (sc.hasNextInt()) {
      x = sc.nextInt();
    } else {
      System.out.println(Const.NO_WIDTH_ERROR);
      return;
    }
    if (sc.hasNextInt()) {
      y = sc.nextInt();
    } else {
      System.out.println(Const.NO_HEIGHT_ERROR);
    }
    if (sc.hasNext()) {
      config = parseConfigArray(sc.next());
      if (config == null) {
        System.out.println(Const.INVALID_CONFIG_ERROR);
        return;
      }
    } else {
      System.out.println(Const.NO_CONFIG_ERROR);
      return;
    }

    ant = null;
    configLen = config.length;
    grid = new AntGrid(y, x, config);
  }

  private static void handleAnt() {
    int i = 0;
    int j = 0;
    if (sc.hasNextInt()) {
      i = sc.nextInt();
    } else {
      System.out.println(Const.NO_X_ERROR);
      return;
    }
    if (sc.hasNextInt()) {
      j = sc.nextInt();
    } else {
      System.out.println(Const.NO_Y_ERROR);
      return;
    }

    ant = new Ant();
    grid.setAnt(ant, i, j);
  }

  private static void handleUnant() {
    if (ant == null) {
      System.out.println(Const.NO_ANT_ERROR);
      return;
    }
    ant = null;
    grid.clearAnts();
  }

  /**
   * Performs steps an resets.
   * With parameter n > 0: performs n steps.
   * With parameter n = 0: performs no steps.
   * Without parameter: performs one step.
   * With parameter n <= 0: performs abs(n) resets.
   */
  private static void handleStep() {
    if (sc.hasNextInt()) {
      int numOfSteps = sc.nextInt();

      if (numOfSteps >= 1) {
        if (ant == null) {
          System.out.println(Const.NO_ANT_ERROR);
          return;
        }
        grid.performStep(numOfSteps);
      } else if (numOfSteps < 0) {
        grid.reset(-numOfSteps);
        refreshAnt();
      }
      System.out.println(grid.getStepCount());
      return;
    }

    if (ant == null) {
      System.out.println(Const.NO_ANT_ERROR);
      return;
    }

    grid.performStep();
    System.out.println(grid.getStepCount());
    refreshAnt();
  }

  private static void handleClear() {
    grid.clear();
    refreshAnt();
  }

  private static void handlePrint() {
    print();
  }

  /**
   * Resizes the Grid to new dimensions.
   */
  private static void handleResize() {
    int x;
    int y;

    if (sc.hasNextInt()) {
      x = sc.nextInt();
    } else {
      System.out.println(Const.NO_WIDTH_ERROR);
      return;
    }
    if (sc.hasNextInt()) {
      y = sc.nextInt();
    } else {
      System.out.println(Const.NO_HEIGHT_ERROR);
      return;
    }
    grid.resize(x, y);
    refreshAnt();
  }

  private static void handleHelp() {
    System.out.println(Const.HELP_MESSAGE);
  }

  private static void handleQuit() {
    sc.close();
    quit = true;
  }

  private static void handleInvalid() {
    System.out.println(Const.INVALID_COMMAND_ERROR);
  }

  private static void handleNoCommand() {
    System.out.println(Const.NO_COMMAND_ERROR);
  }

  /**
   * Parses the String representing Configuration to a boolean[].
   * Each boolean represents either 'left' or 'right',
   * based on the convention stated in Const
   *
   * @param str String representing a configuration
   * @return a boolean[] representing the same configuration
   */
  private static boolean[] parseConfigArray(String str) {
    int strLen = str.length();
    if (strLen > Const.MAX_CONFIGS || strLen < 2) {
      return null;
    }

    boolean[] configArray = new boolean[strLen];
    for (int i = 0; i < strLen; i++) {
      char c = str.charAt(i);
      if (c == 'L') {
        configArray[i] = Const.LEFT;
      } else if (c == 'R') {
        configArray[i] = Const.RIGHT;
      } else {
        return null;
      }
    }
    return configArray;
  }

  private static int getCommandId(String command) {
    if (command.equals("")) {
      return Const.NO_COMMAND;
    }
    if (!command.matches("[a-zA-Z]+")) {
      return Const.INVALID_COMMAND;
    }
    command = command.toLowerCase();
    for (int i = 0; i < Const.COMMANDS.size(); i++) {
      if (Const.COMMANDS.get(i).startsWith(command)) {
        return i;
      }
    }
    return Const.INVALID_COMMAND;
  }

  /**
   * Sets the Shell's ant to be the Grid's ant.
   * Reflects all changes that occur on the Grid's ant.
   */
  private static void refreshAnt() {
    ant = new ArrayList<Ant>(grid.getAnts().values()).get(0);
  }

  /**
   * Prints the Grid to the command-line.
   * Indicates the position and direction of the Ant
   * and indicates the Cell's states by colors.
   */
  private static void print() {
    System.out.println("");
    for (int i = 0; i < grid.getHeight(); i++) {
      List<Cell> lst = grid.getRow(i);
      for (Cell c : lst) {
        State st = c.getState();
        String str = "";
        str = str + getColorSequence(st.getTimesVisited());

        if (st.hasAnt() && ant != null) {
          if (ant.getOrientation() == Direction.UP) {
            str = str + "^";
          } else if (ant.getOrientation() == Direction.RIGHT) {
            str = str + ">";
          } else if (ant.getOrientation() == Direction.DOWN) {
            str = str + "v";
          } else if (ant.getOrientation() == Direction.LEFT) {
            str = str + "<";
          }
        } else {
          str = str + getColorSymbol(st.getTimesVisited());
        }
        str = str + Const.ANSI_RESET;
        System.out.print(str);
      }
      System.out.print("\n");
    }
  }

  /**
   * Maps a Cell's state to a color.
   *
   * @param timesVisited int number of times a Cell has been visited by the ant
   * @return String color
   */
  private static String getColorSequence(int timesVisited) {
    int positionInCycle = (timesVisited % configLen);

    switch (positionInCycle) {
      case 0:
        return Const.COLOR_0;
      case 1:
        return Const.COLOR_1;
      case 2:
        return Const.COLOR_2;
      case 3:
        return Const.COLOR_3;
      case 4:
        return Const.COLOR_4;
      case 5:
        return Const.COLOR_5;
      case 6:
        return Const.COLOR_6;
      case 7:
        return Const.COLOR_7;
      case 8:
        return Const.COLOR_8;
      case 9:
        return Const.COLOR_9;
      case 10:
        return Const.COLOR_10;
      case 11:
        return Const.COLOR_11;
      default:
        return Const.COLOR_0;
    }
  }

  /**
   * Maps a Cell's state to a symbol representing it.
   *
   * @param timesVisited int number of times a Cell has been visited by the ant
   * @return String representig the Cells's state
   */
  private static String getColorSymbol(int timesVisited) {
    int positionInCycle = (timesVisited % configLen);
    if (positionInCycle == 10) {
      return "A";
    }
    if (positionInCycle == 11) {
      return "B";
    }
    return "" + positionInCycle;
  }
}
