package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.java.cs3500.pa03.model.Coord;
import cs3500.java.cs3500.pa03.model.CoordState;
import cs3500.java.cs3500.pa03.model.GameResult;
import cs3500.java.cs3500.pa03.model.Ship;
import cs3500.java.cs3500.pa03.model.ShipOrientation;
import cs3500.java.cs3500.pa03.model.ShipType;
import cs3500.java.cs3500.pa03.view.GameView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * tests the view
 */
public class GameViewTest {
  GameView view;
  Appendable appendable;
  Readable validBoardSize;
  Readable validFleet;
  Readable name;
  ArrayList<ArrayList<Coord>> oppBoard;
  ArrayList<ArrayList<Coord>> userBoard;
  Readable inputShots;
  List<Coord> noShots;
  List<Coord> twoShots;
  List<Ship> sunkShips;

  /**
   * sets up the tests
   */
  @BeforeEach
  public void setup() {
    appendable = new StringBuffer();
    name = new ReadableMock("sabine\n");
    validBoardSize = new ReadableMock("6 9\n");
    validFleet = new ReadableMock("1 1 2 1\n");

    ArrayList<Coord> row1 = new ArrayList<>();
    row1.add(new Coord(0, 0));
    row1.add(new Coord(1, 0));
    row1.add(new Coord(2, 0));

    ArrayList<Coord> row2 = new ArrayList<>();
    Coord c1 = new Coord(0, 1);
    c1.changeState(CoordState.HIT);
    row2.add(c1);
    Coord c2 = new Coord(1, 1);
    c2.changeState(CoordState.HIT);
    row2.add(c2);
    Coord c3 = new Coord(2, 1);
    c3.changeState(CoordState.MISS);
    row2.add(c3);

    ArrayList<Coord> row3 = new ArrayList<>();
    row3.add(new Coord(0, 2));
    row3.add(new Coord(1, 2));
    row3.add(new Coord(2, 2));

    oppBoard = new ArrayList<>();
    oppBoard.add(row1);
    oppBoard.add(row2);
    oppBoard.add(row3);

    ArrayList<Coord> row4 = new ArrayList<>();
    Coord c4 = new Coord(0, 3);
    c4.changeState(CoordState.CARRIER);
    row4.add(c4);
    Coord c5 = new Coord(1, 3);
    c5.changeState(CoordState.BATTLESHIP);
    row4.add(c5);
    Coord c6 = new Coord(2, 3);
    c6.changeState(CoordState.DESTROYER);
    row4.add(c6);
    Coord c7 = new Coord(3, 3);
    c7.changeState(CoordState.SUBMARINE);
    row4.add(c7);

    userBoard = new ArrayList<>();
    userBoard.add(row1);
    userBoard.add(row2);
    userBoard.add(row3);
    userBoard.add(row4);

    inputShots = new ReadableMock("0 0\n");

    noShots = new ArrayList<>();
    twoShots = new ArrayList<>();
    twoShots.add(new Coord(0, 0));
    twoShots.add(new Coord(1, 2));

    sunkShips = new ArrayList<>();
    sunkShips.add(new Ship(ShipType.BATTLESHIP, twoShots, ShipOrientation.VERTICAL));
    sunkShips.add(new Ship(ShipType.CARRIER, twoShots, ShipOrientation.VERTICAL));
    sunkShips.add(new Ship(ShipType.SUBMARINE, twoShots, ShipOrientation.VERTICAL));

  }

  /**
   * tests the get name method
   */
  @Test
  public void testGetName() {
    view = new GameView(name, appendable);
    assertEquals("sabine", view.getName());
    assertEquals("Welcome to BattleSalvo! Please enter your name: \n", appendable.toString());
  }

  /**
   * tests the get board size method
   */
  @Test
  public void testGetBoardSize() {
    view = new GameView(validBoardSize, appendable);
    ArrayList<Integer> result = new ArrayList();
    result.add(6);
    result.add(9);
    assertEquals(result, view.getBoardSize("sabine"));
    assertEquals("Hello sabine! Please input the height and width of the board (must "
        + "be between 6 and 15):\n", appendable.toString());
  }

  /**
   * tests the invalid dimensions method
   */
  @Test
  public void testInvalidDimensions() {
    view  = new GameView(validBoardSize, appendable);
    ArrayList<Integer> result = new ArrayList();
    result.add(6);
    result.add(9);
    assertEquals(result, view.invalidDimensions());
    assertEquals("Oops! You have not input valid dimensions. Please try again!\n",
        appendable.toString());
  }

  /**
   * tests the get fleet method
   */
  @Test
  public void testGetFleet() {
    view = new GameView(validFleet, appendable);
    HashMap<ShipType, Integer> result = new HashMap<>();
    result.put(ShipType.CARRIER, 1);
    result.put(ShipType.BATTLESHIP, 1);
    result.put(ShipType.DESTROYER, 2);
    result.put(ShipType.SUBMARINE, 1);
    assertEquals(result, view.getFleet(5));
    assertEquals("Please enter your fleet in the order [Carrier, Battleship, Destroyer, "
        + "Submarine].\n"
        + "Remember, your fleet may not exceed size " + 5 + "\n", appendable.toString());
  }

  /**
   * tests the invalid fleet method
   */
  @Test
  public void testInvalidFleet() {
    view = new GameView(validFleet, appendable);
    HashMap<ShipType, Integer> result = new HashMap<>();
    result.put(ShipType.CARRIER, 1);
    result.put(ShipType.BATTLESHIP, 1);
    result.put(ShipType.DESTROYER, 2);
    result.put(ShipType.SUBMARINE, 1);
    assertEquals(result, view.invalidFleet(5));
    assertEquals("Oops! You must enter at least one of each and the entire fleet may not "
            + "exceed " + 5 + "!\n Please enter your fleet in the order [Carrier, Battleship, "
            + "Destroyer, Submarine]:\n",
        appendable.toString());
  }

  /**
   * tests the display opponenent board method
   */
  @Test
  public void testDisplayOpponentBoard() {
    view = new GameView(validFleet, appendable);
    String result = "Opponent's Board:\n~ ~ ~ \nX X O \n~ ~ ~ \n\n";
    view.displayOpponentBoard(oppBoard);
    assertEquals(result, appendable.toString());
  }

  /**
   * tests the display user board method
   */
  @Test
  public void testDisplayUserBoard() {
    view = new GameView(name, appendable);
    String result = "Your Board:\n~ ~ ~ \nX X O \n~ ~ ~ \nC B D S \n\n";
    view.displayUserBoard(userBoard);
    assertEquals(result, appendable.toString());
  }

  /**
   * tests the get shots method
   */
  @Test
  public void testGetShots() {
    view = new GameView(inputShots, appendable);
    List<Coord> result = view.getShots(1);
    assertEquals(1, result.size());
    assertEquals(0, result.get(0).getX());
    assertEquals(0, result.get(0).getY());
    assertEquals("Please enter 1 shots:\n", appendable.toString());
  }

  /**
   * tests the invalid shots method
   */
  @Test
  public void testInvalidShots() {
    view = new GameView(inputShots, appendable);
    view.invalidShots(7, 5);
    assertEquals("Oops! You must enter coordinates with the x between 0 and 5 and "
        + "the y between 0 and 7\n", appendable.toString());
  }

  /**
   * tests the display successful hits method
   */
  @Test
  public void testDisplaySuccessfulHitsNoShots() {
    view = new GameView(inputShots, appendable);
    view.displaySuccessfulHits(noShots);
    assertEquals("Oops! You didn't hit any of their ships!\n", appendable.toString());
  }

  /**
   * tests the display successful hits method
   */
  @Test
  public void testDisplaySuccessfulHitsTwoShots() {
    view = new GameView(inputShots, appendable);
    view.displaySuccessfulHits(twoShots);
    assertEquals("Congrats! You hit ships at the following coordinates:\n0 0\n1 2\n",
        appendable.toString());
  }

  /**
   * tests the display users sunk ships method
   */
  @Test
  public void testDisplayUserSunkShips() {
    view = new GameView(inputShots, appendable);
    view.displayUserSunkShips(sunkShips);
    assertEquals("They sunk your BATTLESHIP!\nThey sunk your CARRIER!\nThey sunk your "
        + "SUBMARINE!\n", appendable.toString());
  }

  /**
   * tests the display opponenet sunk ships method
   */
  @Test
  public void testDisplayOppSunkShips() {
    view = new GameView(inputShots, appendable);
    view.displayOppSunkShips(sunkShips);
    assertEquals("You sunk their BATTLESHIP!\nYou sunk their CARRIER!\nYou sunk their "
        + "SUBMARINE!\n", appendable.toString());
  }

  /**
   * tests the end screen win method
   */
  @Test
  public void testEndScreenWin() {
    view = new GameView(inputShots, appendable);
    view.endScreen(GameResult.WIN, "you win");
    assertEquals("You won! you win\n", appendable.toString());
  }

  /**
   * tests the end screen lose method
   */
  @Test
  public void testEndScreenLose() {
    view = new GameView(inputShots, appendable);
    view.endScreen(GameResult.LOSE, "you lose");
    assertEquals("You lost! you lose\n", appendable.toString());
  }

  /**
   * tests the end screen draw method
   */
  @Test
  public void testEndScreenDraw() {
    view = new GameView(inputShots, appendable);
    view.endScreen(GameResult.DRAW, "tie");
    assertEquals("You both tied! tie\n", appendable.toString());
  }
}
