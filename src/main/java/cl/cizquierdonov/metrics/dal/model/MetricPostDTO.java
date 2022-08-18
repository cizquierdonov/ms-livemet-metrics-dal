package cl.cizquierdonov.metrics.dal.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data object transfer structure of JSON request and response metric post object.
 * @author cizquierdo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MetricPostDTO {

  @JsonProperty("id")
  private Integer id;

  @JsonProperty("type")
  private String type;

  @JsonProperty("value")
  private String value;

  @JsonProperty("review")
  private String review;

  @JsonProperty("recordDate")
  private String recordDate;
  
}
