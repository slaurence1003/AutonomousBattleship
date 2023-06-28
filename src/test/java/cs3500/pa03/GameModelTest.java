package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.java.cs3500.pa03.model.Coord;
import cs3500.java.cs3500.pa03.model.CoordState;
import cs3500.java.cs3500.pa03.model.GameModel;
import cs3500.java.cs3500.pa03.model.GameResult;
import cs3500.java.cs3500.pa03.model.Ship;
import cs3500.java.cs3500.pa03.model.ShipOrientation;
import cs3500.java.cs3500.pa03.model.ShipType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * tests the model
 */
public class GameModelTest {
  GameModel gameModel;
  ArrayList<Integer> validDimensions;
  ArrayList<Integer> invalidDimensions1;
  ArrayList<Integer> invalidDimensions2;
  ArrayList<Integer> invalidDimensions3;
  ArrayList<Integer> invalidDimensions4;
  HashMap<ShipType, Integer> validFleet;
  HashMap<ShipType, Integer> invalidFleet1;
  HashMap<ShipType, Integer> invalidFleet2;
  List<Coord> invalidCoords1;
  List<Coord> invalidCoords2;
  List<Coord> validCoords;
  List<Coord> carrierCoords;
  List<Coord> battleshipCoords;
  List<Coord> destroyerCoords;
  List<Coord> subCoords;
  Ship carrier;
  Ship battleship;
  Ship destroyer;
  Ship sub;
  List<Ship> ships;
  List<Ship> updatedShips;
  List<Ship> emptyList;

  /**
   * sets up the tests
   */
  @BeforeEach
  public void setup() {
    gameModel = new GameModel();

    validDimensions = new ArrayList<>();
    validDimensions.add(7);
    validDimensions.add(9);

    invalidDimensions1 = new ArrayList<>();
    invalidDimensions1.add(4);
    invalidDimensions1.add(9);

    invalidDimensions2 = new ArrayList<>();
    invalidDimensions2.add(7);
    invalidDimensions2.add(19);

    invalidDimensions3 = new ArrayList<>();
    invalidDimensions3.add(17);
    invalidDimensions3.add(9);

    invalidDimensions4 = new ArrayList<>();
    invalidDimensions4.add(5);
    invalidDimensions4.add(2);

    validFleet = new HashMap<>();
    validFleet.put(ShipType.CARRIER, 2);
    validFleet.put(ShipType.BATTLESHIP, 1);
    validFleet.put(ShipType.DESTROYER, 3);
    validFleet.put(ShipType.SUBMARINE, 1);

    invalidFleet1 = new HashMap<>();
    invalidFleet1.put(ShipType.CARRIER, 2);
    invalidFleet1.put(ShipType.BATTLESHIP, 1);
    invalidFleet1.put(ShipType.DESTROYER, 0);
    invalidFleet1.put(ShipType.SUBMARINE, 1);

    invalidFleet2 = new HashMap<>();
    invalidFleet2.put(ShipType.CARRIER, 2);
    invalidFleet2.put(ShipType.BATTLESHIP, 1);
    invalidFleet2.put(ShipType.DESTROYER, 1);
    invalidFleet2.put(ShipType.SUBMARINE, 1);

    invalidCoords1 = new ArrayList<>();
    invalidCoords1.add(new Coord(0, 10));

    invalidCoords2 = new ArrayList<>();
    invalidCoords2.add(new Coord(20, 1));

    validCoords = new ArrayList<>();
    validCoords.add(new Coord(2, 2));
    validCoords.add(new Coord(1, 3));

    carrierCoords = new ArrayList<>();
    carrierCoords.add(new Coord(0, 0));
    carrierCoords.add(new Coord(1, 0));
    carrierCoords.add(new Coord(2, 0));
    carrierCoords.add(new Coord(3, 0));
    carrierCoords.add(new Coord(4, 0));
    carrierCoords.add(new Coord(5, 0));

    battleshipCoords = new ArrayList<>();
    battleshipCoords.add(new Coord(0, 1));
    battleshipCoords.add(new Coord(0, 2));
    battleshipCoords.add(new Coord(0, 3));
    battleshipCoords.add(new Coord(0, 4));
    battleshipCoords.add(new Coord(0, 5));

    destroyerCoords = new ArrayList<>();
    destroyerCoords.add(new Coord(1, 2));
    destroyerCoords.add(new Coord(2, 2));
    destroyerCoords.add(new Coord(3, 2));
    destroyerCoords.add(new Coord(4, 2));

    subCoords = new ArrayList<>();
    subCoords.add(new Coord(3, 4));
    subCoords.add(new Coord(4, 4));
    subCoords.add(new Coord(5, 4));

    carrier = new Ship(ShipType.CARRIER, carrierCoords, ShipOrientation.VERTICAL);
    battleship = new Ship(ShipType.BATTLESHIP, battleshipCoords, ShipOrientation.VERTICAL);
    destroyer = new Ship(ShipType.DESTROYER, destroyerCoords, ShipOrientation.VERTICAL);
    sub = new Ship(ShipType.SUBMARINE, subCoords, ShipOrientation.VERTICAL);

    ships = new ArrayList<>();
    ships.add(carrier);
    ships.add(battleship);
    ships.add(destroyer);
    ships.add(sub);

    updatedShips = new ArrayList<>();
    updatedShips.add(carrier);
    updatedShips.add(battleship);
    updatedShips.add(destroyer);

    emptyList = new ArrayList<>();
  }

  /**
   * tests the valid dimensions method
   */
  @Test
  public void testValidDimensions() {
    assertTrue(gameModel.validDimensions(validDimensions));
    assertFalse(gameModel.validDimensions(invalidDimensions1));
    assertFalse(gameModel.validDimensions(invalidDimensions2));
    assertFalse(gameModel.validDimensions(invalidDimensions3));
    assertFalse(gameModel.validDimensions(invalidDimensions4));
  }

  /**
   * tests the invalid fleet method
   */
  @Test
  public void testInvalidFleet() {
    assertFalse(gameModel.invalidFleet(validFleet, 7));
    assertTrue(gameModel.invalidFleet(invalidFleet1, 6));
    assertTrue(gameModel.invalidFleet(invalidFleet2, 4));
  }

  /**
   * tests the setup board method
   */
  @Test
  public void testSetupBoard() {
    assertEquals(6, gameModel.setupBoard(6, 6, ships).size());
    assertEquals(6, gameModel.setupBoard(6, 6, ships).get(0).size());
    assertEquals(CoordState.BATTLESHIP, gameModel.setupBoard(6, 6, ships).get(1).get(0).getState());
    assertEquals(CoordState.CARRIER, gameModel.setupBoard(6, 6, ships).get(0).get(0).getState());
    assertEquals(CoordState.SUBMARINE, gameModel.setupBoard(6, 6, ships).get(4).get(3).getState());
    assertEquals(CoordState.DESTROYER, gameModel.setupBoard(6, 6, ships).get(2).get(1).getState());
    assertEquals(CoordState.EMPTY, gameModel.setupBoard(6, 6, ships).get(5).get(5).getState());
  }

  /**
   * tests the invalid shots method
   */
  @Test
  public void testInvalidShots() {
    assertTrue(gameModel.invalidShots(invalidCoords1, 4, 4));
    assertTrue(gameModel.invalidShots(invalidCoords2, 4, 4));
    assertFalse(gameModel.invalidShots(validCoords, 4, 4));
  }

  /**
   * tests the update board method
   */
  @Test
  public void testUpdateBoard() {
    ArrayList<ArrayList<Coord>> ogBoard = gameModel.setupBoard(6, 6, ships);
    List<Coord> shots = new ArrayList<>();
    shots.add(new Coord(0, 0));
    shots.add(new Coord(5, 5));
    ArrayList<ArrayList<Coord>> updatedBoard = gameModel.updateBoard(ogBoard, shots);
    assertEquals(CoordState.MISS, updatedBoard.get(5).get(5).getState());
    assertEquals(CoordState.HIT, updatedBoard.get(0).get(0).getState());
  }

  /**
   * tests the get sunk ship method
   */
  @Test
  public void testGetSunkShip() {
    List<Ship> result = new ArrayList<>();
    result.add(sub);
    assertEquals(result, gameModel.getSunkShip(ships, updatedShips));
  }

  /**
   * tests the game over method
   */
  @Test
  public void testGameOver() {
    assertFalse(gameModel.gameOver(ships, updatedShips));
    assertTrue(gameModel.gameOver(emptyList, ships));
    assertTrue(gameModel.gameOver(ships, emptyList));
  }

  /**
   * tests the check result method
   */
  @Test
  public void testCheckResult() {
    assertEquals(GameResult.WIN, gameModel.checkResult(ships, emptyList));
    assertEquals(GameResult.DRAW, gameModel.checkResult(emptyList, emptyList));
    assertEquals(GameResult.LOSE, gameModel.checkResult(emptyList, ships));
  }
}
