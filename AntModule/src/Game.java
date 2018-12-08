public class Game {

  private Grid grid;
  private boolean[] configuration;

  public Game(int height, int width, boolean[] configuration){
    this.configuration = configuration;
    this.grid = new AntGrid(height, width, configuration);
  }

  public void setAnt(int x, int y){
    grid.setAnt(new Ant(), x, y);
  }

  public void step(int n){
    if (n >= 0){
      grid.performStep(n);
    }else {
      grid.reset(-n);
    }
  }

  public void step(){
    grid.performStep();
  }

  public void unAnt(){
    grid.setAnt(null, 0, 0);
  }

  public int getConfigurationLength(){
    return configuration.length;
  }
}
