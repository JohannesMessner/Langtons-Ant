public class Game {

  private Grid grid;
  private boolean[] configuration;

  public Game(int height, int width, boolean[] configuration){
    this.configuration = configuration;
    this.grid = new AntGrid(height, width, configuration);
  }

  public int getConfigurationLength(){
    return configuration.length;
  }
}
