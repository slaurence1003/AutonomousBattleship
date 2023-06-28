package cs3500.java.cs3500.pa03.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * represents a FleetJson
 *
 * @param fleet the map of ships and counts of them
 */
public record FleetJson(
    @JsonProperty("fleet")List<ShipIntermediaryJson> fleet) {
}
