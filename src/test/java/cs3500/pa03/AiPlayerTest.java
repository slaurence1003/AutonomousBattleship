package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.java.cs3500.pa03.model.AiPlayer;
import cs3500.java.cs3500.pa03.model.Player;
import cs3500.java.cs3500.pa03.model.Ship;
import cs3500.java.cs3500.pa03.model.ShipType;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * tests the ai player class
 */
public class AiPlayerTest {
  Player player1;
  HashMap<ShipType, Integer> specs;
  List<Ship> ships;

  /**
   * sets up the tests
   */
  @BeforeEach
  public void setup() {
    player1 = new AiPlayer();
    specs = new HashMap<>();
    specs.put(ShipType.CARRIER, 1);
    specs.put(ShipType.BATTLESHIP, 1);
    specs.put(ShipType.SUBMARINE, 1);
    specs.put(ShipType.DESTROYER, 1);
    ships = player1.setup(6, 6, specs);
  }

  /**
   * tests the name
   */
  @Test
  public void testName() {
    assertTrue(player1.name().equals("slaurence1003"));
  }

  /**
   * tests the setup method
   */
  @Test
  public void testSetup() {
    assertEquals(4, ships.size());
  }

  /**
   * tests the take shots method
   */
  @Test
  public void testTakeShots() {
    assertEquals(4, player1.takeShots().size());
  }

}
