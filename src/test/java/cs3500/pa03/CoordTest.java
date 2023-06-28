package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.java.cs3500.pa03.model.Coord;
import cs3500.java.cs3500.pa03.model.CoordState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * tests the coordinate class
 */
public class CoordTest {
  Coord empty;
  Coord hit;
  Coord miss;
  Coord battleship;
  Coord sub;
  Coord destroyer;
  Coord carrier;

  /**
   * sets up the tests
   */
  @BeforeEach
  public void setup() {
    empty = new Coord(1, 2);

    hit = new Coord(1, 1);
    hit.changeState(CoordState.HIT);

    miss = new Coord(1, 1);
    miss.changeState(CoordState.MISS);

    battleship = new Coord(1, 1);
    battleship.changeState(CoordState.BATTLESHIP);

    sub = new Coord(1, 1);
    sub.changeState(CoordState.SUBMARINE);

    carrier = new Coord(1, 1);
    carrier.changeState(CoordState.CARRIER);

    destroyer = new Coord(1, 1);
    destroyer.changeState(CoordState.DESTROYER);
  }

  /**
   * tests the get x method
   */
  @Test
  public void testGetX() {
    assertEquals(1, empty.getX());
  }

  /**
   * tests the get y method
   */
  @Test
  public void testGetY() {
    assertEquals(2, empty.getY());
  }

  /**
   * tests the get state method
   */
  @Test
  public void testGetState() {
    assertEquals(CoordState.EMPTY, empty.getState());
    empty.changeState(CoordState.BATTLESHIP);
    assertEquals(CoordState.BATTLESHIP, empty.getState());
  }

  /**
   * tests the is ship method
   */
  @Test
  public void testIsShip() {
    assertFalse(empty.isShip());
    assertFalse(hit.isShip());
    assertFalse(miss.isShip());
    assertTrue(battleship.isShip());
    assertTrue(carrier.isShip());
    assertTrue(sub.isShip());
    assertTrue(destroyer.isShip());
  }
}
