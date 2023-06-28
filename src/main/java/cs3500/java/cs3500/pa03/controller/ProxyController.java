package cs3500.java.cs3500.pa03.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.java.cs3500.pa03.json.CoordJson;
import cs3500.java.cs3500.pa03.json.FleetJson;
import cs3500.java.cs3500.pa03.json.FleetSetupJson;
import cs3500.java.cs3500.pa03.json.JoinJson;
import cs3500.java.cs3500.pa03.json.JsonUtils;
import cs3500.java.cs3500.pa03.json.MessageJson;
import cs3500.java.cs3500.pa03.json.ResultJson;
import cs3500.java.cs3500.pa03.json.SetupJson;
import cs3500.java.cs3500.pa03.json.ShipIntermediaryJson;
import cs3500.java.cs3500.pa03.json.VolleyJson;
import cs3500.java.cs3500.pa03.model.Coord;
import cs3500.java.cs3500.pa03.model.GameResult;
import cs3500.java.cs3500.pa03.model.Player;
import cs3500.java.cs3500.pa03.model.Ship;
import cs3500.java.cs3500.pa03.model.ShipType;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * represents a ProxyController
 */
public class ProxyController implements ControllerInterface {
  private final Socket server;
  private final InputStream in;
  private final PrintStream out;
  private final Player player;
  private final ObjectMapper mapper = new ObjectMapper();
  private final String gameMode;

  private static final JsonNode VOID_RESPONSE =
      new ObjectMapper().getNodeFactory().textNode("void");

  /**
   * Constructor for ProxyController
   *
   *
   * @param server The game server
   * @param player the Ai
   * @param gameMode Mode for the game, Ai vs Ai or Ai vs server
   * @throws IOException if there is an exception
   */
  public ProxyController(Socket server, Player player, String gameMode)
      throws IOException {
    this.server = server;
    this.in = server.getInputStream();
    this.out = new PrintStream(server.getOutputStream());
    this.player = player;
    this.gameMode = gameMode;
  }

  @Override
  public void run() {
    try {
      JsonParser parser = this.mapper.getFactory().createParser(this.in);

      while (!this.server.isClosed()) {
        MessageJson message = parser.readValueAs(MessageJson.class);
        delegateMessage(message);
      }
    } catch (IOException e) {
      System.out.println("can't parse");
    }
  }

  private void delegateMessage(MessageJson message) {
    String methodName = message.methodName();
    JsonNode arguments = message.arguments();

    if ("join".equals(methodName)) {
      handleJoin();
    } else if ("setup".equals(methodName)) {
      handleSetup(arguments);
    } else if ("take-shots".equals(methodName)) {
      handleTakeShots();
    } else if ("report-damage".equals(methodName)) {
      handleReportDamage(arguments);
    } else if ("successful-hits".equals(methodName)) {
      handleSuccessfulHits(arguments);
    } else if ("end-game".equals(methodName)) {
      handleEndGame(arguments);
    } else {
      throw new IllegalStateException("Invalid method name");
    }
  }

  /**
   * handles when the join method is called by the server
   */
  public void handleJoin() {
    String name = player.name();

    JsonNode response = JsonUtils.serializeRecord(new JoinJson(name, gameMode));
    MessageJson reply = new MessageJson("join", response);
    JsonNode jsonResponse = JsonUtils.serializeRecord(reply);
    this.out.println(jsonResponse);
  }

  /**
   * Handles the setup when the terminals says so
   *
   * @param arguments the shipMap, the height, and the width
   */
  public void handleSetup(JsonNode arguments) {
    SetupJson setupArgs = this.mapper.convertValue(arguments, SetupJson.class);
    FleetSetupJson fleetSetup = this.mapper.convertValue(setupArgs.fleetSpecs(),
        FleetSetupJson.class);

    HashMap<ShipType, Integer> fleetSpecs = new HashMap<>();
    fleetSpecs.put(ShipType.CARRIER, fleetSetup.carrier());
    fleetSpecs.put(ShipType.BATTLESHIP, fleetSetup.battleship());
    fleetSpecs.put(ShipType.DESTROYER, fleetSetup.destroyer());
    fleetSpecs.put(ShipType.SUBMARINE, fleetSetup.submarine());

    List<Ship> ships = player.setup(setupArgs.height(), setupArgs.width(), fleetSpecs);

    List<ShipIntermediaryJson> shipIntermediaries = new ArrayList<>();
    for (Ship ship : ships) {
      Coord shipCoord = ship.getCoords().get(0);
      shipIntermediaries.add(new ShipIntermediaryJson(new CoordJson(shipCoord.getX(),
          shipCoord.getY()), ship.shipSize(), ship.getOrientation()));
    }

    JsonNode response = JsonUtils.serializeRecord(new FleetJson(shipIntermediaries));
    MessageJson message = new MessageJson("setup", response);
    JsonNode jsonResponse = JsonUtils.serializeRecord(message);
    this.out.println(jsonResponse);
  }

  /**
   * handles the takeShots method
   */
  public void handleTakeShots() {
    List<Coord> shots = player.takeShots();
    List<CoordJson> shotsJson = new ArrayList<>();

    for (Coord shot : shots) {
      shotsJson.add(new CoordJson(shot.getX(), shot.getY()));
    }

    JsonNode response = JsonUtils.serializeRecord(new VolleyJson(shotsJson));
    MessageJson message = new MessageJson("take-shots", response);
    JsonNode jsonResponse = JsonUtils.serializeRecord(message);
    System.out.println("shots:" + jsonResponse);
    this.out.println(jsonResponse);
  }

  /**
   * handles the reportDamage method
   *
   * @param arguments the list of Coords which the enemy fired at us
   */
  public void handleReportDamage(JsonNode arguments) {
    VolleyJson incomingShotsJson = this.mapper.convertValue(arguments, VolleyJson.class);
    List<Coord> incomingShots = new ArrayList<>();

    for (CoordJson coord : incomingShotsJson.shots()) {
      incomingShots.add(new Coord(coord.x(), coord.y()));
    }

    List<Coord> damage = player.reportDamage(incomingShots);
    List<CoordJson> reportDamage = new ArrayList<>();

    for (Coord coord : damage) {
      reportDamage.add(new CoordJson(coord.getX(), coord.getY()));
    }

    JsonNode response = JsonUtils.serializeRecord(new VolleyJson(reportDamage));
    MessageJson message = new MessageJson("report-damage", response);
    JsonNode jsonResponse = JsonUtils.serializeRecord(message);
    this.out.println(jsonResponse);
  }

  /**
   * handles the successfulHits method
   *
   * @param arguments the list of coords which hit opponent ships
   */
  public void handleSuccessfulHits(JsonNode arguments) {
    VolleyJson successfulHits = this.mapper.convertValue(arguments, VolleyJson.class);
    List<Coord> hits = new ArrayList<>();


    for (CoordJson coord : successfulHits.shots()) {
      hits.add(new Coord(coord.x(), coord.y()));
      System.out.println("ServerX:" + coord.x() + " ServerY:" + coord.y());
    }

    player.successfulHits(hits);

    MessageJson message = new MessageJson("successful-hits", VOID_RESPONSE);
    JsonNode jsonResponse = JsonUtils.serializeRecord(message);
    this.out.println(jsonResponse);
  }

  /**
   * handles the endGame method
   *
   * @param arguments the gameResult decided by the server
   */
  public void handleEndGame(JsonNode arguments) {
    ResultJson result = this.mapper.convertValue(arguments, ResultJson.class);

    GameResult gameResult;
    if (result.result().equals("WIN")) {
      gameResult = GameResult.WIN;
    } else if (result.result().equals("LOSE")) {
      gameResult = GameResult.LOSE;
    } else {
      gameResult = GameResult.DRAW;
    }

    player.endGame(gameResult, result.reason());

    MessageJson message = new MessageJson("end-game", VOID_RESPONSE);
    JsonNode jsonResponse = JsonUtils.serializeRecord(message);
    this.out.println(jsonResponse);
  }
}
