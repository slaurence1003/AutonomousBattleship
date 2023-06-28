package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.java.cs3500.pa03.controller.ProxyController;
import cs3500.java.cs3500.pa03.json.CoordJson;
import cs3500.java.cs3500.pa03.json.FleetSetupJson;
import cs3500.java.cs3500.pa03.json.JsonUtils;
import cs3500.java.cs3500.pa03.json.MessageJson;
import cs3500.java.cs3500.pa03.json.ResultJson;
import cs3500.java.cs3500.pa03.json.SetupJson;
import cs3500.java.cs3500.pa03.json.VolleyJson;
import cs3500.java.cs3500.pa03.model.AiPlayer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * tests for the ProxyController
 */
public class ProxyControllerTest {
  private ByteArrayOutputStream testLog;
  private ProxyController controller;


  /**
   * Reset the test log before each test is run.
   */
  @BeforeEach
  public void setup() {
    this.testLog = new ByteArrayOutputStream(2048);
    assertEquals("", logToString());
  }

  @Test
  public void testHandleJoin() {

    JsonNode sampleMessage = JsonUtils.serializeRecord(new MessageJson("join",
        VOID_RESPONSE));

    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    try {
      this.controller = new ProxyController(socket, new AiPlayer(), "SINGLE");
    } catch (IOException e) {
      fail();
    }

    this.controller.run();

    String expected = "{\"method-name\":\"join\",\"arguments\":{\"name\":\"slaurence1003\",\""
        + "game-type\":\"SINGLE\"}}\n";
    assertEquals(expected, logToString());
  }

  @Test
  public void testHandleSetup() {
    JsonNode fleet = JsonUtils.serializeRecord(new FleetSetupJson(1, 2, 3, 4));
    SetupJson arguments = new SetupJson(10, 11, fleet);
    JsonNode sampleMessage = createSampleMessage("setup", arguments);

    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    try {
      this.controller = new ProxyController(socket, new AiPlayer(), "SINGLE");
    } catch (IOException e) {
      fail();
    }

    this.controller.run();
    responseToClass(MessageJson.class);
  }

  @Test
  public void testHandleTakeShots() {
    JsonNode sampleMessage = JsonUtils.serializeRecord(new MessageJson("take-shots",
        VOID_RESPONSE));

    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    try {
      this.controller = new ProxyController(socket, new AiPlayer(), "SINGLE");
    } catch (IOException e) {
      fail();
    }

    this.controller.run();
    responseToClass(MessageJson.class);
  }

  @Test
  public void testHandleReportDamage() {
    VolleyJson volley = new VolleyJson(List.of(new CoordJson(5, 3)));
    JsonNode sampleMessage = createSampleMessage("report-damage", volley);

    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    try {
      this.controller = new ProxyController(socket, new AiPlayer(), "SINGLE");
    } catch (IOException e) {
      fail();
    }

    this.controller.run();
    responseToClass(MessageJson.class);
  }

  @Test
  public void testHandleSuccessfulHits() {
    JsonNode sampleMessage1 = JsonUtils.serializeRecord(new MessageJson("join",
        VOID_RESPONSE));
    JsonNode fleet = JsonUtils.serializeRecord(new FleetSetupJson(1, 2, 3, 4));
    SetupJson arguments = new SetupJson(10, 11, fleet);
    JsonNode sampleMessage2 = createSampleMessage("setup", arguments);
    VolleyJson volley = new VolleyJson(List.of(new CoordJson(10, 5)));
    JsonNode sampleMessage3 = JsonUtils.serializeRecord(new MessageJson("take-shots",
        VOID_RESPONSE));
    VolleyJson volley2 = new VolleyJson(List.of(new CoordJson(5, 3)));
    JsonNode sampleMessage4 = createSampleMessage("report-damage", volley);
    JsonNode sampleMessage5 = createSampleMessage("successful-hits", volley2);

    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage1.toString(),
        sampleMessage2.toString(), sampleMessage3.toString(), sampleMessage4.toString(),
        sampleMessage5.toString()));

    try {
      this.controller = new ProxyController(socket, new AiPlayer(), "SINGLE");
    } catch (IOException e) {
      fail();
    }

    this.controller.run();
    responseToClass(MessageJson.class);
  }

  @Test
  public void testHandleEndGame() {
    ResultJson result = new ResultJson("WIN", "Player 1 sank all of Player 2's ships");
    JsonNode sampleMessage = createSampleMessage("end-game", result);

    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    try {
      this.controller = new ProxyController(socket, new AiPlayer(), "SINGLE");
    } catch (IOException e) {
      fail();
    }

    this.controller.run();
    responseToClass(MessageJson.class);
  }

  private static final JsonNode VOID_RESPONSE =
      new ObjectMapper().getNodeFactory().textNode("void");

  /**
   * Converts the ByteArrayOutputStream log to a string in UTF_8 format
   *
   * @return String representing the current log buffer
   */
  private String logToString() {
    return testLog.toString(StandardCharsets.UTF_8);
  }
  /**
   * Try converting the current test log to a string of a certain class.
   *
   * @param classRef Type to try converting the current test stream to.
   * @param <T>      Type to try converting the current test stream to.
   */

  private <T> void responseToClass(@SuppressWarnings("SameParameterValue") Class<T> classRef) {
    try {
      JsonParser jsonParser = new ObjectMapper().createParser(logToString());
      jsonParser.readValueAs(classRef);
      // No error thrown when parsing to a GuessJson, test passes!
    } catch (IOException e) {
      // Could not read
      // -> exception thrown
      // -> test fails since it must have been the wrong type of response.
      fail();
    }
  }

  /**
   * Create a MessageJson for some name and arguments.
   *
   * @param messageName name of the type of message; "hint" or "win"
   * @param messageObject object to embed in a message json
   * @return a MessageJson for the object
   */
  private JsonNode createSampleMessage(String messageName, Record messageObject) {
    MessageJson messageJson = new MessageJson(messageName,
        JsonUtils.serializeRecord(messageObject));
    return JsonUtils.serializeRecord(messageJson);
  }
}
