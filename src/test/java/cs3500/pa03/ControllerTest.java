package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.java.cs3500.pa03.controller.GameController;
import cs3500.java.cs3500.pa03.model.GameModel;
import cs3500.java.cs3500.pa03.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * tests the controller class
 */
public class ControllerTest {
  Readable readable;
  Appendable appendable;
  GameController controller;
  GameModel model;
  GameView view;

  /**
   * sets up the tests
   */
  @BeforeEach
  public void setup() {
    readable = new ReadableMock("sabine\n6 6\n1 1 1 1\n \n0 0\n0 1\n 0 2\n0 3 \n 0 4\n0 5"
        + "\n 1 0\n1 1\n 1 2\n1 3 \n 1 4\n1 5\n 2 0\n2 1\n 2 2\n2 3 \n 2 4\n2 5"
        + "\n 3 0\n3 1\n 3 2\n3 3 \n 3 4\n3 5\n 4 0\n4 1\n 4 2\n4 3 \n 4 4\n4 5"
        + "\n 5 0\n5 1\n 5 2\n5 3 \n 5 4\n5 5");
    appendable = new StringBuffer();
    model = new GameModel();
    view = new GameView(readable, appendable);
    controller = new GameController(view, model);
  }

  /**
   * tests the run game method
   */
  @Test
  public void testRunGame() {
    controller.run();
    assertTrue(appendable.toString().contains("Welcome to BattleSalvo!"));
    assertTrue(appendable.toString().contains("Please input the height"));
    assertTrue(appendable.toString().contains("Please enter your fleet"));
    assertTrue(appendable.toString().contains("Opponent's Board:"));
    assertTrue(appendable.toString().contains("Your Board:"));
    assertTrue(appendable.toString().contains("You hit ships at the following"));
    assertTrue(appendable.toString().contains("You sunk their"));
  }
}
