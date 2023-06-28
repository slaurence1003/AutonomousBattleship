package cs3500.java.cs3500.pa03.view;

import cs3500.java.cs3500.pa03.model.Coord;
import cs3500.java.cs3500.pa03.model.CoordState;
import cs3500.java.cs3500.pa03.model.GameResult;
import cs3500.java.cs3500.pa03.model.Ship;
import cs3500.java.cs3500.pa03.model.ShipType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * class that displays game to the terminal
 */
public class GameView {
  private final Scanner scanner;
  private final Appendable appendable;

  /**
   *
   * @param readable what the view reads
   * @param appendable what the view displays on
   */
  public GameView(Readable readable, Appendable appendable) {
    this.scanner = new Scanner(readable);
    this.appendable = appendable;
  }

  /**
   *
   * @return the name of the player
   */
  public String getName() {
    try {
      appendable.append("Welcome to BattleSalvo! Please enter your name: \n");
    } catch (IOException e) {
      System.out.println("Can't append to appendable");
    }
    return scanner.next();
  }

  /**
   *
   * @param name the name of the player
   * @return the height and width of the board
   */
  public List<Integer> getBoardSize(String name) {
    try {
      appendable.append("Hello ").append(name)
          .append("! Please input the height and width of the board (must ")
          .append("be between 6 and 15):\n");
    } catch (IOException e) {
      System.out.println("Can't append to appendable");
    }
    int height = scanner.nextInt();
    int width = scanner.nextInt();
    List<Integer> dimensions = new ArrayList<>();
    dimensions.add(height);
    dimensions.add(width);
    return dimensions;
  }

  /**
   *
   * @return valid dimensions
   */
  public ArrayList<Integer> invalidDimensions() {
    try {
      appendable.append("Oops! You have not input valid dimensions. Please try again!\n");
    } catch (IOException e) {
      System.out.println("Can't append to appendable");
    }
    Integer height = scanner.nextInt();
    Integer width = scanner.nextInt();
    ArrayList<Integer> dimensions = new ArrayList<>();
    dimensions.add(height);
    dimensions.add(width);
    return dimensions;
  }

  /**
   *
   * @param max the max num of ships the fleet can be
   * @return a hashmap of the ship types and how many of them
   */
  public HashMap<ShipType, Integer> getFleet(int max) {
    try {
      appendable.append(
          "Please enter your fleet in the order [Carrier, Battleship, Destroyer, Submarine].\n"
              + "Remember, your fleet may not exceed size ").append(String.valueOf(max)).append(
                  "\n");
    } catch (IOException e) {
      System.out.println("Can't append to appendable");
    }

    HashMap<ShipType, Integer> hash = new HashMap<>();
    hash.put(ShipType.CARRIER, scanner.nextInt());
    hash.put(ShipType.BATTLESHIP, scanner.nextInt());
    hash.put(ShipType.DESTROYER, scanner.nextInt());
    hash.put(ShipType.SUBMARINE, scanner.nextInt());

    return hash;
  }

  /**
   *
   * @param max the max amount of ships in the fleet
   * @return the fleet
   */
  public HashMap<ShipType, Integer> invalidFleet(int max) {
    try {
      appendable.append(
              "Oops! You must enter at least one of each and the entire fleet may not exceed ")
          .append(String.valueOf(max)).append(
              "!\n Please enter your fleet in the order [Carrier, Battleship, Destroyer, "
                  + "Submarine]:\n");
    } catch (IOException e) {
      System.out.println("Can't append to appendable");
    }

    HashMap<ShipType, Integer> hash = new HashMap<>();
    hash.put(ShipType.CARRIER, scanner.nextInt());
    hash.put(ShipType.BATTLESHIP, scanner.nextInt());
    hash.put(ShipType.DESTROYER, scanner.nextInt());
    hash.put(ShipType.SUBMARINE, scanner.nextInt());

    return hash;
  }

  /**
   *
   * @param board the opponents board
   */
  public void displayOpponentBoard(ArrayList<ArrayList<Coord>> board) {
    try {
      appendable.append("Opponent's Board:\n");

      for (ArrayList<Coord> row : board) {
        for (Coord coord : row) {
          CoordState state = coord.getState();

          switch (state) {
            case HIT:
              appendable.append("X ");
              break;
            case MISS:
              appendable.append("O ");
              break;
            default:
              appendable.append("~ ");
              break;
          }
        }
        appendable.append("\n");
      }

      appendable.append("\n");
    } catch (IOException e) {
      System.out.println("Can't append to appendable");
    }
  }

  /**
   *
   * @param board the user's board
   */
  public void displayUserBoard(ArrayList<ArrayList<Coord>> board) {
    try {
      appendable.append("Your Board:\n");

      for (ArrayList<Coord> row : board) {
        for (Coord coord : row) {
          CoordState state = coord.getState();

          switch (state) {
            case CARRIER:
              appendable.append("C ");
              break;
            case BATTLESHIP:
              appendable.append("B ");
              break;
            case DESTROYER:
              appendable.append("D ");
              break;
            case SUBMARINE:
              appendable.append("S ");
              break;
            case HIT:
              appendable.append("X ");
              break;
            case MISS:
              appendable.append("O ");
              break;
            default:
              appendable.append("~ ");
              break;
          }
        }
        appendable.append("\n");
      }

      appendable.append("\n");
    } catch (IOException e) {
      System.out.println("Can't append to appendable");
    }
  }

  /**
   *
   * @param numOfShots the amount of shots that the player can take
   * @return where they want to take shots
   */
  public List<Coord> getShots(int numOfShots) {
    ArrayList<Coord> shots = new ArrayList<>();
    try {
      appendable.append("Please enter ").append(String.valueOf(numOfShots)).append(" shots:\n");
    } catch (IOException e) {
      System.out.println("Can't append to appendable");
    }

    for (int i = 0; i < numOfShots; i++) {
      int x = scanner.nextInt();
      int y = scanner.nextInt();
      shots.add(new Coord(x, y));
    }
    return shots;
  }

  /**
   *
   * @param ymax the max value y can be
   * @param xmax the max value x can be
   */
  public void invalidShots(int ymax, int xmax) {
    try {
      appendable.append("Oops! You must enter coordinates with the x between 0 and ")
          .append(String.valueOf(xmax)).append(" and the y between 0 and ")
          .append(String.valueOf(ymax)).append("\n");
    } catch (IOException e) {
      System.out.println("Can't append to appendable");
    }
  }

  /**
   *
   * @param shotsThatHitOpponentShips display your successful hits on your opponent's board
   */
  public void displaySuccessfulHits(List<Coord> shotsThatHitOpponentShips) {
    if (shotsThatHitOpponentShips.size() == 0) {
      try {
        appendable.append("Oops! You didn't hit any of their ships!\n");
      } catch (IOException e) {
        System.out.println("Can't append to appendable");
      }
    } else {
      try {
        appendable.append("Congrats! You hit ships at the following coordinates:\n");
        for (Coord c : shotsThatHitOpponentShips) {
          appendable.append(String.valueOf(c.getX())).append(" ").append(
              String.valueOf(c.getY()))
              .append("\n");
        }
      } catch (IOException e) {
        System.out.println("Can't append to appendable");
      }
    }
  }

  /**
   *
   * @param ships your sunk ships
   */
  public void displayUserSunkShips(List<Ship> ships) {
    for (Ship ship : ships) {
      try {
        appendable.append("They sunk your ").append(ship.getType().toString()).append("!\n");
      } catch (IOException e) {
        System.out.println("Can't append to appendable");
      }
    }
  }

  /**
   *
   * @param ships ships you sunk on their board
   */
  public void displayOppSunkShips(List<Ship> ships) {
    for (Ship ship : ships) {
      try {
        appendable.append("You sunk their ").append(ship.getType().toString()).append("!\n");
      } catch (IOException e) {
        System.out.println("Can't append to appendable");
      }
    }
  }

  /**
   *
   * @param result displays the result of the game
   */
  public void endScreen(GameResult result, String message) {
    if (result.equals(GameResult.DRAW)) {
      try {
        appendable.append("You both tied! " + message + "\n");
      } catch (IOException e) {
        System.out.println("Can't append to appendable");
      }
    } else if (result.equals(GameResult.WIN)) {
      try {
        appendable.append("You won! " + message + "\n");
      } catch (IOException e) {
        System.out.println("Can't append to appendable");
      }
    } else {
      try {
        appendable.append("You lost! " + message + "\n");
      } catch (IOException e) {
        System.out.println("Can't append to appendable");
      }
    }
  }
}
