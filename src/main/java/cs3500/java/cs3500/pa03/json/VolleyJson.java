package cs3500.java.cs3500.pa03.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * represents a volleyJson
 *
 * @param shots a list of coords in the volley
 */
public record VolleyJson(
    @JsonProperty("coordinates")List<CoordJson> shots) {
}
