package cs3500.java.cs3500.pa03.model;

import java.util.List;

/**
 * class to represent a ship
 */
public class Ship {
  private final ShipType type;
  private final List<Coord> coords;
  private final ShipOrientation orientation;

  /**
   *
   * @param type ship type
   * @param coords the coordinates the ship sits in
   */
  public Ship(ShipType type, List<Coord> coords, ShipOrientation orientation) {
    this.type = type;
    this.coords = coords;
    this.orientation = orientation;
  }

  /**
   *
   * @return what size the ship is based on its type
   */
  public int shipSize() {
    if (type.equals(ShipType.CARRIER)) {
      return 6;
    } else if (type.equals(ShipType.BATTLESHIP)) {
      return 5;
    } else if (type.equals(ShipType.DESTROYER)) {
      return 4;
    } else {
      return 3;
    }
  }

  /**
   *
   * @return what type the ship is
   */
  public ShipType getType() {
    return this.type;
  }

  /**
   *
   * @return the coords the ship sits on
   */
  public List<Coord> getCoords() {
    return this.coords;
  }

  public ShipOrientation getOrientation() {
    return this.orientation;
  }
}
