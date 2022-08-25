package cl.cizquierdonov.metrics.dal.model.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.cizquierdonov.metrics.dal.model.Average;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wrapper response DTO class to get metric posts average by date.
 * @author cizquierdo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GetAverageByDateResponse {

  @JsonProperty("average")
  private Average average;

  @JsonProperty("result")
  private Result result;
  
}
