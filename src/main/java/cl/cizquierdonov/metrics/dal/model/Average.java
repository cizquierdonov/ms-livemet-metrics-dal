package cl.cizquierdonov.metrics.dal.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Defines input data for metric posts values average calculation.
 * @author cizquierdo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Average {
  
  @JsonProperty("metricType")
  private String metricType;

  @JsonProperty("averageType")
  private String averageType;

  @JsonProperty("date")
  private String date;

}
