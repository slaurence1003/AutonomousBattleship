package cs3500.java.cs3500.pa03.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * represents the joinJson
 *
 * @param name the player name
 * @param gameMode gameMode, single or multi
 */
public record JoinJson(
    @JsonProperty("name") String name,
    @JsonProperty("game-type") String gameMode) {
}
