package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.java.cs3500.pa03.model.Player;
import cs3500.java.cs3500.pa03.model.Ship;
import cs3500.java.cs3500.pa03.model.ShipType;
import cs3500.java.cs3500.pa03.model.TerminalPlayer;
import cs3500.java.cs3500.pa03.view.GameView;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * tests terminal player class
 */
public class TerminalPlayerTest {
  Player player1;
  HashMap<ShipType, Integer> specs;
  List<Ship> ships;
  Appendable appendable;
  Readable readable;
  GameView view;

  /**
   * sets up the tests
   */
  @BeforeEach
  public void setup() {
    appendable = new StringBuffer();
    readable = new ReadableMock("sabine\n 0 0 \n 0 1\n 0 2 \n 0 3\n");
    view = new GameView(readable, appendable);
    player1 = new TerminalPlayer("sabine", view);
    specs = new HashMap<>();
    specs.put(ShipType.CARRIER, 1);
    specs.put(ShipType.BATTLESHIP, 1);
    specs.put(ShipType.SUBMARINE, 1);
    specs.put(ShipType.DESTROYER, 1);
    ships = player1.setup(6, 6, specs);
  }

  /**
   * tests the name method
   */
  @Test
  public void testName() {
    assertEquals("sabine", player1.name());
  }

  /**
   * tests the setup method
   */
  @Test
  public void testSetup() {
    assertEquals(4, ships.size());
  }

}
