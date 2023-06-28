package cs3500.java.cs3500.pa03.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * represents the setupJson
 *
 * @param width width of the board
 * @param height height of the board
 * @param fleetSpecs the map of the fleet
 */
public record SetupJson(
    @JsonProperty("width") int width,
    @JsonProperty("height") int height,
    @JsonProperty("fleet-spec")JsonNode fleetSpecs) {
}
