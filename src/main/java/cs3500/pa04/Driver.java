package cs3500.pa04;

import cs3500.java.cs3500.pa03.controller.GameController;
import cs3500.java.cs3500.pa03.controller.ProxyController;
import cs3500.java.cs3500.pa03.model.AiPlayer;
import cs3500.java.cs3500.pa03.model.GameModel;
import cs3500.java.cs3500.pa03.model.Player;
import cs3500.java.cs3500.pa03.view.GameView;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * This is the main driver of this project
 */
public class Driver {
  /**
   * Project entry point
   *
   * @param args - no command line args required
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      GameModel model = new GameModel();
      GameView view = new GameView(new InputStreamReader(System.in), System.out);
      GameController controller = new GameController(view, model);
      controller.run();
    } else if (args.length == 3) {
      String host = args[0];
      int port = Integer.parseInt(args[1]);
      String gameMode = args[2];
      Socket socket = null;
      try {
        socket = new Socket(host, port);
      } catch (IOException exception) {
        System.out.println("invalid host or port");
      }

      Player player = new AiPlayer();
      try {
        ProxyController controller = new ProxyController(socket, player, gameMode);
        controller.run();
      } catch (IOException e) {
        System.out.println("invalid socket");
      }
    } else {
      throw new IllegalArgumentException("must be either 0 or 3 command line arguments");
    }
  }
}