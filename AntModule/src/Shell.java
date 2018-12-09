import java.util.List;

public class Shell {

  static Grid grid;
  static Game game;

  public static void main(String[] args) {
    boolean[] config = new boolean[2];
    config[0] = false;
    config[1] = true;
    game = new Game(120, 120, config);
    game.setAnt(50, 50);
    grid = game.getGrid();
    //print();

//    game.step();
//    print();
//
//    game.step();
//    print();

    game.step(20000);
    print();
    System.out.println(-1 % 3);

  }

  private static void print() {
    System.out.println("");
    for (int i = 0; i < 120; i++) {
      List lst = grid.getRow(i);
      for (Object o : lst) {
        Cell c = (Cell) o;
        State st = c.getState();
        String str = "" + (st.getPositionInCycle() % 2);
        if (st.hasAnt()) {
          str = "x";
        }
        System.out.print(str);
      }
      System.out.print("\n");
    }
  }
}
