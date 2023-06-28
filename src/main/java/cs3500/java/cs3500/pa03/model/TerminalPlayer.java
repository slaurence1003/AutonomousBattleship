package cs3500.java.cs3500.pa03.model;

import cs3500.java.cs3500.pa03.view.GameView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents a single human terminal player in a game of BattleSalvo
 */
public class TerminalPlayer implements Player {
  private final GameView view;
  private List<Ship> ships;

  /**
   *
   * @param name the player name
   * @param view an inputted view to use
   */
  public TerminalPlayer(String name, GameView view) {
    this.view = view;
    this.ships = new ArrayList<>();
  }

  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  @Override
  public String name() {
    return view.getName();
  }

  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board.
   *
   * @param height the height of the board, range: [6, 15] inclusive
   * @param width the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  @Override
  public List<Ship> setup(int height, int width, HashMap<ShipType, Integer> specifications) {
    List<Ship> shipsList = new ArrayList<>();

    Random rand = new Random();

    for (Map.Entry<ShipType, Integer> entry : specifications.entrySet()) {
      ShipType shipType = entry.getKey();
      int shipCount = entry.getValue();

      for (int i = 0; i < shipCount; i++) {
        boolean placed = false;
        while (!placed) {
          int startX = rand.nextInt(width);
          int startY = rand.nextInt(height);
          ShipOrientation orientation = rand.nextBoolean() ? ShipOrientation.VERTICAL :
              ShipOrientation.HORIZONTAL;

          List<Coord> shipCoords = generateShipCoords(startX, startY, shipType, orientation);
          Ship ship = new Ship(shipType, shipCoords, orientation);

          if (isValidPlacement(height, width, ship, shipsList)) {
            shipsList.add(ship);
            placed = true;
          }
        }
      }
    }

    this.ships = shipsList;
    return shipsList;
  }

  /**
   *
   * @param startX where the x coord should start
   * @param startY where the y coord should start
   * @param shipType the type of the ship
   * @param orientation the direction the ship will be orientated
   * @return a list of ships
   */
  private List<Coord> generateShipCoords(int startX, int startY, ShipType shipType,
                                         ShipOrientation orientation) {
    List<Coord> coords = new ArrayList<>();
    int size;
    CoordState coordState;
    if (shipType.equals(ShipType.CARRIER)) {
      size = 6;
      coordState = CoordState.CARRIER;
    } else if (shipType.equals(ShipType.BATTLESHIP)) {
      size = 5;
      coordState = CoordState.BATTLESHIP;
    } else if (shipType.equals(ShipType.DESTROYER)) {
      size = 4;
      coordState = CoordState.DESTROYER;
    } else {
      size = 3;
      coordState = CoordState.SUBMARINE;
    }

    if (orientation == ShipOrientation.HORIZONTAL) {
      for (int x = startX; x < startX + size; x++) {
        Coord shipCoord = new Coord(x, startY);
        shipCoord.changeState(coordState);
        coords.add(shipCoord);
      }
    } else {
      for (int y = startY; y < startY + size; y++) {
        Coord shipCoord = new Coord(startX, y);
        shipCoord.changeState(coordState);
        coords.add(shipCoord);
      }
    }

    return coords;
  }

  /**
   *
   * @param height the height of the board
   * @param width the width of the board
   * @param ship the ship in question
   * @param ships the list of already placed ships
   * @return whether this is a valid place to put the ship
   */
  private boolean isValidPlacement(int height, int width, Ship ship, List<Ship> ships) {
    for (Coord coord : ship.getCoords()) {
      int x = coord.getX();
      int y = coord.getY();

      if (x < 0 || x >= width || y < 0 || y >= height) {
        return false;
      }

      for (Ship existingShip : ships) {
        for (Coord existingCoord : existingShip.getCoords()) {
          if (existingCoord.getX() == x && existingCoord.getY() == y) {
            return false;
          }
        }
      }
    }

    return true;
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    return view.getShots(ships.size());
  }


  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that contain all locations of shots that hit a
   *      ship on this board
   */
  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    List<Coord> hitShips = new ArrayList<>();
    for (Ship currShip : ships) {
      for (Coord currCoord : opponentShotsOnBoard) {
        for (Coord shipCoord : currShip.getCoords()) {
          if (shipCoord.getX() == currCoord.getX() && shipCoord.getY()
              == currCoord.getY()) {
            hitShips.add(currCoord);
            shipCoord.changeState(CoordState.HIT);
          }
        }
      }
    }
    updatePlayerShips();
    return hitShips;
  }

  private void updatePlayerShips() {
    List<Ship> updatedShips = new ArrayList<>();

    for (Ship ship : this.ships) {
      boolean sunk = true;

      for (Coord shipCoord : ship.getCoords()) {
        if (shipCoord.isShip()) {
          sunk = false;
          break;
        }
      }

      if (!sunk) {
        updatedShips.add(ship);
      }
    }

    this.ships = updatedShips;
  }



  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    view.displaySuccessfulHits(shotsThatHitOpponentShips);
  }

  /**
   * Notifies the player that the game is over.
   * Win, lose, and draw should all be supported
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  @Override
  public void endGame(GameResult result, String reason) {

  }
}
