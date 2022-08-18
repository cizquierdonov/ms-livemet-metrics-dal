package cl.cizquierdonov.metrics.dal.model.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.cizquierdonov.metrics.dal.model.Average;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Wrapper request DTO class to get metric posts average by date.
 * @author cizquierdo
 */
@Data
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GetAverageByDateRequest {

  @JsonProperty("average")
  private Average average;

  public GetAverageByDateRequest() {
    average = new Average();
  }
  
}
