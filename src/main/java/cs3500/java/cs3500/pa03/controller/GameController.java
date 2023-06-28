package cs3500.java.cs3500.pa03.controller;

import cs3500.java.cs3500.pa03.model.AiPlayer;
import cs3500.java.cs3500.pa03.model.Coord;
import cs3500.java.cs3500.pa03.model.GameModel;
import cs3500.java.cs3500.pa03.model.GameResult;
import cs3500.java.cs3500.pa03.model.Player;
import cs3500.java.cs3500.pa03.model.Ship;
import cs3500.java.cs3500.pa03.model.ShipType;
import cs3500.java.cs3500.pa03.model.TerminalPlayer;
import cs3500.java.cs3500.pa03.view.GameView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * controls the whole game
 */
public class GameController implements ControllerInterface {
  private final GameView view;
  private final GameModel model;

  /**
   *
   * @param view  to use for the game
   * @param model model to use for the game
   */

  public GameController(GameView view, GameModel model) {
    this.view = view;
    this.model = model;
  }

  /**
   * runs the entire game
   */
  @Override
  public void run() {
    String player1Name = view.getName();

    List<Integer> dimensions = view.getBoardSize(player1Name);
    while (!model.validDimensions(dimensions)) {
      dimensions = view.invalidDimensions();
    }

    int height = dimensions.get(0);
    int width = dimensions.get(1);
    int max = Math.min(height, width);

    HashMap<ShipType, Integer> fleet = view.getFleet(max);
    while (model.invalidFleet(fleet, max)) {
      fleet = view.invalidFleet(max);
    }

    Player player1 = new TerminalPlayer(player1Name, view);
    Player player2 = new AiPlayer();
    List<Ship> player1Ships = player1.setup(height, width, fleet);
    List<Ship> player2Ships = player2.setup(height, width, fleet);

    ArrayList<ArrayList<Coord>> player1Board = model.setupBoard(height, width, player1Ships);
    ArrayList<ArrayList<Coord>> player2Board = model.setupBoard(height, width, player2Ships);

    List<Ship> updatedPlayer1Ships = player1Ships;
    List<Ship> updatedPlayer2Ships = player2Ships;

    while (! model.gameOver(updatedPlayer1Ships, updatedPlayer2Ships)) {

      view.displayOpponentBoard(player2Board);
      view.displayUserBoard(player1Board);

      List<Coord> player1shots = player1.takeShots();
      List<Coord> player2shots = player2.takeShots();

      while (model.invalidShots(player1shots, height - 1, width - 1)) {
        view.invalidShots(height - 1, width - 1);
        player1shots = player1.takeShots();
      }

      List<Coord> player2HitsOnPlayer1 = player1.reportDamage(player2shots);
      List<Coord> player1HitsOnPlayer2 = player2.reportDamage(player1shots);

      player1.successfulHits(player1HitsOnPlayer2);
      player2.successfulHits(player2HitsOnPlayer1);

      player1Board = model.updateBoard(player1Board, player2shots);
      player2Board = model.updateBoard(player2Board, player1shots);

      updatedPlayer1Ships = model.updateShips(player1Ships, player1Board);
      updatedPlayer2Ships = model.updateShips(player2Ships, player2Board);

      if (! (updatedPlayer1Ships.size() == player1Ships.size())) {
        List<Ship> sunkShips = model.getSunkShip(player1Ships, updatedPlayer1Ships);
        view.displayUserSunkShips(sunkShips);
      }

      if (! (updatedPlayer2Ships.size() == player2Ships.size())) {
        List<Ship> sunkShips = model.getSunkShip(player2Ships, updatedPlayer2Ships);
        view.displayOppSunkShips(sunkShips);
      }
    }

    GameResult result = model.checkResult(updatedPlayer1Ships, updatedPlayer2Ships);
    view.endScreen(result, "");
  }
}
