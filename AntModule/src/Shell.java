import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

public class Shell {

  private static Grid grid;
  private static Ant ant;
  private static boolean quit;
  private static Scanner sc;
  private static int configLen;

  private static final String PROMPT = "ant> ";

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

//    boolean[] config = new boolean[2];
//    config[0] = false;
//    config[1] = true;
//    game = new Game(120, 120, config);
//    game.setAnt(50, 50);
//    grid = game.getGrid();
//
//    game.step(20000);
//    print();
//    System.out.println(-1 % 3);

  }

  private static void handleCommand(String inputLine) {
    sc = new Scanner(inputLine);
    sc.useDelimiter("\\s+");

    String command = "";
    if (sc.hasNext()) {
      command = sc.next();
    }

    int commandID = getCommandID(command);
    switch (commandID) {
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
      config = toConfigArray(sc.next());
      if (config == null) {
        System.out.println(Const.INVALID_CONFIG_ERROR);
        return;
      }
    } else {
      System.out.println(Const.NO_CONFIG_ERROR);
      return;
    }

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
    }
    if (sc.hasNextInt()) {
      j = sc.nextInt();
    } else {
      System.out.println(Const.NO_Y_ERROR);
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

  private static void handleStep() {
    if (ant == null) {
      System.out.println(Const.NO_ANT_ERROR);
      return;
    }
    if (sc.hasNextInt()) {
      grid.performStep(sc.nextInt());
      System.out.println(grid.getStepCount());
      return;
    }
    grid.performStep();
    System.out.println(grid.getStepCount());
  }

  private static void handleClear() {
    grid.clear();
  }

  private static void handlePrint() {
    print();
  }

  private static void handleResize() {
    int x = 0;
    int y = 0;

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

  private static boolean[] toConfigArray(String str) {
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

  private static int getCommandID(String command) {
    if (command.equals("")) {
      return Const.NO_COMMAND;
    }
    if (!command.matches("[a-zA-Z]+")) {
      return Const.INVALID_COMMAND;
    }
    command = command.toLowerCase();
    for (int i = 0; i < Const.COMMANDS.length; i++) {
      if (Const.COMMANDS[i].startsWith(command)) {
        return i;
      }
    }
    return Const.INVALID_COMMAND;
  }

  private static void print() {
    System.out.println("");
    for (int i = 0; i < grid.getHeight(); i++) {
      List<Cell> lst = grid.getRow(i);
      for (Object o : lst) {
        Cell c = (Cell) o;
        State st = c.getState();
        String str = "";
        str = str + getColorSequence(st.getPositionInCycle());

        if (st.hasAnt()) {
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
          str = str + getColorSymbol(st.getPositionInCycle());
        }
        str = str + Const.ANSI_RESET;
        System.out.print(str);
      }
      System.out.print("\n");
    }
  }

  private static String getColorSequence(int positionInCycle){
    positionInCycle = (positionInCycle % configLen);

    switch (positionInCycle){
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
    }
    return Const.COLOR_0;
  }

  private static String getColorSymbol(int positionInCycle){
    positionInCycle = (positionInCycle % configLen);
    if (positionInCycle == 10) {
      return "A";
    }
    if (positionInCycle == 11) {
      return "B";
    }
    return "" + positionInCycle;
  }
}
