package cs3500.java.cs3500.pa03.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents a single AI/computer player in a game of BattleSalvo
 */
public class AiPlayer implements Player {
  private final String name;
  private List<Ship> ships;
  private  int boardHeight;
  private  int boardWidth;
  private List<Coord> shots;
  private ArrayList<ArrayList<Coord>> enemyBoard;
  private ArrayList<Coord> targets;

  /**
   * constructor for the AiPlayer
   * Initializes the name, the ships, the shots, the height, width, enemyBoard, and targets for
   * the ai firing system
   */
  public AiPlayer() {
    this.name = name();
    this.ships = new ArrayList<>();
    this.shots = new ArrayList<>();
    boardHeight = 0;
    boardWidth = 0;
    this.enemyBoard = new ArrayList<>();
    this.targets = new ArrayList<>();
  }

  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  @Override
  public String name() {
    return "slaurence1003";
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
    List<Ship> ships = new ArrayList<>();
    this.boardHeight = height;
    this.boardWidth = width;
    System.out.println("board height: " + boardHeight);
    System.out.println("board width: " + boardWidth);

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

          if (isValidPlacement(height, width, ship, ships)) {
            ships.add(ship);
            placed = true;
          }
        }
      }
    }

    setupBoard();
    this.ships = ships;
    return ships;
  }


  private void setupBoard() {
    ArrayList<ArrayList<Coord>> board = new ArrayList<>();

    for (int row = 0; row < boardWidth; row++) {
      ArrayList<Coord> rowCoords = new ArrayList<>();
      for (int col = 0; col < boardHeight; col++) {
        Coord coord = new Coord(row, col);
        rowCoords.add(coord);
      }
      board.add(rowCoords);
    }

    this.enemyBoard = board;
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
    System.out.println(ships.size());
    List<Coord> currShots = new ArrayList<>();
    Coord shotCoord;

    int numShotsLeft = ships.size();
    for (Coord c : targets) {
      System.out.println("TargetX:" + c.getX() + " TargetY:" + c.getY());
    }

    ArrayList<Coord> temp = this.targets;
    int num = Math.min(targets.size(), numShotsLeft);

    for (int i = 0; i < num; i++) {
      currShots.add(temp.get(0));
      shots.add(temp.get(0));
      temp.remove(temp.get(0));
      numShotsLeft--;
    }

    targets = temp;

    for (int i = 0; i < numShotsLeft; i++) {

      shotCoord = generateRandomCoord();
      while (containsCoord(shotCoord, this.shots) || containsCoord(shotCoord, currShots)) {
        shotCoord = generateRandomCoord();
      }
      this.shots.add(shotCoord);
      shotCoord.changeState(CoordState.MISS);
      currShots.add(shotCoord);
    }
    return currShots;
  }

  /**
   * generates a random coord on the board
   *
   * @return a coord
   */
  private Coord generateRandomCoord() {
    Random rand = new Random();

    int x = rand.nextInt(boardWidth);
    int y = rand.nextInt(boardHeight);

    return new Coord(x, y);
  }

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that contain all locations of shots that hit a
   *     ship on this board
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
    for (Coord c : shotsThatHitOpponentShips) {
      int x = c.getX();
      int y = c.getY();

      Coord boardCoord = enemyBoard.get(x).get(y);
      boardCoord.changeState(CoordState.HIT);

      if (y + 1 < boardHeight && !containsCoord(enemyBoard.get(x).get(y + 1), shots)
          && !containsCoord(enemyBoard.get(x).get(y + 1), targets)) {
        targets.add(enemyBoard.get(x).get(y + 1));
      }
      if (y > 0 && !containsCoord(enemyBoard.get(x).get(y - 1), shots)
          && !containsCoord(enemyBoard.get(x).get(y - 1), targets)) {
        targets.add(enemyBoard.get(x).get(y - 1));
      }
      if (x + 1 < boardWidth && !containsCoord(enemyBoard.get(x + 1).get(y), shots)
          && !containsCoord(enemyBoard.get(x + 1).get(y), targets)) {
        targets.add(enemyBoard.get(x + 1).get(y));
      }
      if (x > 0 && !containsCoord(enemyBoard.get(x - 1).get(y), shots)
          && !containsCoord(enemyBoard.get(x - 1).get(y), targets)) {
        targets.add(enemyBoard.get(x - 1).get(y));
      }
    }
  }

  /**
   * checks if a Coord with a specific X and Y is contained within a list of Coord
   */

  private boolean containsCoord(Coord c1, List<Coord> listCoord) {
    boolean contains = false;
    for (Coord c2 : listCoord) {
      if (c1.getX() == c2.getX() && c1.getY() == c2.getY()) {
        contains = true;
        break;
      }
    }
    return contains;
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
    System.out.println(result.toString() + " " + reason);
  }
}
