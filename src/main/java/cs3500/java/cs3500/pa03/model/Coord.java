package cs3500.java.cs3500.pa03.model;

/**
 * represents a single coordinate on the board
 */
public class Coord {
  private final int x;
  private final int y;
  private CoordState state;
  /**
   * @param x  x location
   * @param y the y location
   */

  public Coord(int x, int y) {
    this.x = x;
    this.y = y;
    this.state = CoordState.EMPTY;
  }

  /**
   * gets the x value of the coord
   *
   * @return the x value
   */
  public int getX() {
    return this.x;
  }

  /**
   * gets the y value of the coord
   *
   * @return the y value
   */
  public int getY() {
    return this.y;
  }

  /**
   * changes the state of the coord
   *
   * @param newState the state you want to change the coord to
   */
  public void changeState(CoordState newState) {
    this.state = newState;
  }

  /**
   * gets the state of the coord
   *
   * @return the state
   */
  public CoordState getState() {
    return this.state;
  }

  /**
   * checks if the coordinate represents a ship
   *
   * @return true or false
   */
  public boolean isShip() {
    return this.state.equals(CoordState.BATTLESHIP)
        || this.state.equals(CoordState.CARRIER)
        || this.state.equals(CoordState.DESTROYER)
        || this.state.equals(CoordState.SUBMARINE);
  }
}
