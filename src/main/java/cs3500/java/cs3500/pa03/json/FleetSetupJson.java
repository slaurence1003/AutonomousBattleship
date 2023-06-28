package cs3500.java.cs3500.pa03.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the FleeSetupJson
 *
 * @param carrier number of carriers
 * @param battleship number of battleships
 * @param destroyer number of destroyers
 * @param submarine number of submarines
 */
public record FleetSetupJson(
    @JsonProperty("CARRIER") int carrier,
    @JsonProperty("BATTLESHIP") int battleship,
    @JsonProperty("DESTROYER") int destroyer,
    @JsonProperty("SUBMARINE") int submarine) {
}
