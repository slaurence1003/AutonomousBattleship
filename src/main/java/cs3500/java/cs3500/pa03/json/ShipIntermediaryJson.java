package cs3500.java.cs3500.pa03.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.java.cs3500.pa03.model.ShipOrientation;

/**
 * represents the ShipIntermediaryJson
 *
 * @param coord the coord the ship starts at
 * @param length the length of the ship
 * @param direction the position of the ship
 */
public record ShipIntermediaryJson(
    @JsonProperty("coord") CoordJson coord,
    @JsonProperty("length") int length,
    @JsonProperty("direction")ShipOrientation direction) {
}

