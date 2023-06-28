package cs3500.java.cs3500.pa03.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * represents a resultJson
 *
 * @param result the gameResult
 * @param reason why it is that result
 */
public record ResultJson(
    @JsonProperty("result") String result,
    @JsonProperty("reason") String reason) {
}
