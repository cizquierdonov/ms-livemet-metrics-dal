package cl.cizquierdonov.metrics.dal.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data object transfer structure of JSON request and response metric type object.
 * @author cizquierdo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MetricTypeDTO {

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("active")
  private boolean active;

  @JsonProperty("suffix")
  private String suffix;

  @JsonProperty("scale")
  private boolean scale;

}
