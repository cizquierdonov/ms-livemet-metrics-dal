package cl.cizquierdonov.metrics.dal.model.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data object transfer structure of JSON response for create metric type operation.
 * @author cizquierdo
 */
@Data
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CreateMetricTypeResponse {

  @JsonProperty("result")
  private Result result;

  public CreateMetricTypeResponse() {
    result = new Result();
  }
  
}
