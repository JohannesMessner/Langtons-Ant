import java.util.List;

public class Shell {

  static Grid grid;
  static Game game;

  public static void main(String[] args) {
    boolean[] config = new boolean[2];
    config[0] = true;
    config[1] = false;
    game = new Game(10, 10, config);
    game.setAnt(3,3);
    grid = game.getGrid();
    print();

    game.step();
    print();

    game.step();
    print();

    game.step(10000);
    print();

  }

  private static void print(){
    System.out.println("");
    for (int i = 0; i < 10; i++){
      List lst = grid.getRow(i);
      for (Object o : lst){
        Cell c = (Cell) o;
        State st = c.getState();
        String str = "" + st.getPositionInCycle();
        if (st.hasAnt()){
          str = "x";
        }
        System.out.print(str);
      }
      System.out.print("\n");
    }
  }
}
