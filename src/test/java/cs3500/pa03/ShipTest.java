package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.java.cs3500.pa03.model.Coord;
import cs3500.java.cs3500.pa03.model.Ship;
import cs3500.java.cs3500.pa03.model.ShipOrientation;
import cs3500.java.cs3500.pa03.model.ShipType;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * tests ship class
 */
public class ShipTest {
  Ship battleship;
  Ship carrier;
  Ship sub;
  Ship destroyer;
  List<Coord> coords;

  /**
   * sets up the tests
   */
  @BeforeEach
  public void setup() {
    coords = new ArrayList<>();
    coords.add(new Coord(0, 0));

    battleship = new Ship(ShipType.BATTLESHIP, coords, ShipOrientation.HORIZONTAL);
    carrier = new Ship(ShipType.CARRIER, coords, ShipOrientation.VERTICAL);
    sub = new Ship(ShipType.SUBMARINE, coords, ShipOrientation.HORIZONTAL);
    destroyer = new Ship(ShipType.DESTROYER, coords, ShipOrientation.VERTICAL);
  }

  /**
   * tests the ship size method
   */
  @Test
  public void testShipSize() {
    assertEquals(5, battleship.shipSize());
    assertEquals(6, carrier.shipSize());
    assertEquals(3, sub.shipSize());
    assertEquals(4, destroyer.shipSize());
  }

  /**
   * tests the get type method
   */
  @Test
  public void testGetType() {
    assertEquals(ShipType.BATTLESHIP, battleship.getType());
    assertEquals(ShipType.CARRIER, carrier.getType());
    assertEquals(ShipType.SUBMARINE, sub.getType());
    assertEquals(ShipType.DESTROYER, destroyer.getType());
  }

  /**
   * tests the get coord method
   */
  @Test
  public void testGetCoords() {
    assertEquals(coords, battleship.getCoords());
  }
}
