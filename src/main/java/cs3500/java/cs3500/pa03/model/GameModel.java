package cs3500.java.cs3500.pa03.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * another class with more methods the model should take care of
 */
public class GameModel {
  /**
   *
   * @param dimensions the board dimensions
   * @return true or false, whether it is valid
   */
  public boolean validDimensions(List<Integer> dimensions) {
    int height = dimensions.get(0);
    int width = dimensions.get(1);
    return (height >= 6 && height <= 15) && (width >= 6 && width <= 15);
  }

  /**
   *
   * @param hash the specifications for the fleet
   * @param max the max number of ships
   * @return true or false depending on if it is valid
   */
  public boolean invalidFleet(HashMap<ShipType, Integer> hash, int max) {
    boolean invalid = false;
    int ships = 0;
    for (Map.Entry<ShipType, Integer> currShip : hash.entrySet()) {
      if (currShip.getValue() == 0) {
        invalid = true;
      }
      ships += currShip.getValue();
    }
    if (ships > max) {
      invalid = true;
    }
    return invalid;
  }

  /**
   * sets up the board
   *
   * @param height the height of the board
   * @param width the width of the board
   * @param ships ships to place on the board
   * @return the board
   */
  public ArrayList<ArrayList<Coord>> setupBoard(int height, int width, List<Ship> ships) {
    ArrayList<ArrayList<Coord>> board = new ArrayList<>();

    for (int row = 0; row < height; row++) {
      ArrayList<Coord> rowCoords = new ArrayList<>();
      for (int col = 0; col < width; col++) {
        Coord coord = new Coord(col, row);
        rowCoords.add(coord);
      }
      board.add(rowCoords);
    }

    for (Ship ship : ships) {
      for (Coord shipCoord : ship.getCoords()) {
        Coord boardCoord = board.get(shipCoord.getY()).get(shipCoord.getX());
        if (ship.getType().equals(ShipType.CARRIER)) {
          boardCoord.changeState(CoordState.CARRIER);
        } else if (ship.getType().equals(ShipType.BATTLESHIP)) {
          boardCoord.changeState(CoordState.BATTLESHIP);
        } else if (ship.getType().equals(ShipType.DESTROYER)) {
          boardCoord.changeState(CoordState.DESTROYER);
        } else {
          boardCoord.changeState(CoordState.SUBMARINE);
        }
      }
    }
    return board;
  }

  /**
   *
   * @param shots shots taken by the player
   * @param ymax the largest y coord on the board
   * @param xmax the largest x coord on the board
   * @return whether the shots are valid
   */
  public boolean invalidShots(List<Coord> shots, int ymax, int xmax) {
    boolean invalid = false;
    for (Coord c : shots) {
      if (c.getX() > xmax) {
        invalid = true;
      }
      if (c.getY() > ymax) {
        invalid = true;
      }
    }
    return invalid;
  }

  /**
   *
   * @param board the given board
   * @param shots the shots the other player took on the board
   * @return mutates the board
   */
  public ArrayList<ArrayList<Coord>> updateBoard(ArrayList<ArrayList<Coord>> board,
                                                 List<Coord> shots) {
    for (ArrayList<Coord> coords : board) {
      for (Coord coord : coords) {
        for (Coord c : shots) {
          if (coord.getX() == c.getX() && coord.getY() == c.getY()
              && coord.isShip()) {
            coord.changeState(CoordState.HIT);
          } else if (coord.getX() == c.getX() && coord.getY() == c.getY()
              && (coord.getState().equals(CoordState.MISS)
              || coord.getState().equals(CoordState.EMPTY))) {
            coord.changeState(CoordState.MISS);
          }
        }
      }
    }
    return board;
  }

  /**
   *
   * @param ships original ship list
   * @param board the board with the hits on it
   * @return a list of ships with their updated states
   */
  public List<Ship> updateShips(List<Ship> ships, ArrayList<ArrayList<Coord>> board) {
    List<Ship> updatedShips = new ArrayList<>();
    for (Ship ship : ships) {
      int hitCounter = 0;
      for (Coord shipCoord : ship.getCoords()) {
        for (ArrayList<Coord> coords : board) {
          for (Coord boardCoord : coords) {
            if (boardCoord.getX() == shipCoord.getX() && boardCoord.getY()
                == shipCoord.getY()
                && boardCoord.getState().equals(CoordState.HIT)) {
              shipCoord.changeState(CoordState.HIT);
              hitCounter++;
            }
          }
        }
      }
      if (! (ship.shipSize() == hitCounter)) {
        updatedShips.add(ship);
      }
    }
    return updatedShips;
  }

  /**
   *
   * @param originalShips all ships on the board
   * @param updatedShips the remaining ships
   * @return the ship(s) that have been sunk
   */
  public List<Ship> getSunkShip(List<Ship> originalShips, List<Ship> updatedShips) {
    List<Ship> sunkShips = new ArrayList<>();
    for (Ship ogShip : originalShips) {
      if (!updatedShips.contains(ogShip)) {
        sunkShips.add(ogShip);
      }
    }
    return sunkShips;
  }

  /**
   *
   * @param player1Ships list of their ships
   * @param player2Ships list of their ships
   * @return if the game is over if either of their ship lists are empty
   */
  public boolean gameOver(List<Ship> player1Ships, List<Ship> player2Ships) {
    return (player1Ships.isEmpty() || player2Ships.isEmpty());
  }

  /**
   *
   * @param player1Ships list of their ships
   * @param player2Ships list of their ships
   * @return if the terminal player won, lost, or tied
   */
  public GameResult checkResult(List<Ship> player1Ships, List<Ship> player2Ships) {
    GameResult result;
    if (player2Ships.isEmpty() && player1Ships.isEmpty()) {
      result = GameResult.DRAW;
    } else if (player1Ships.isEmpty()) {
      result = GameResult.LOSE;
    } else {
      result = GameResult.WIN;
    }
    return result;
  }
}
