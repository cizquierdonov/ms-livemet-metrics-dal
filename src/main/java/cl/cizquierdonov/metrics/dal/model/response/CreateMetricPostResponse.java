package cl.cizquierdonov.metrics.dal.model.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data object transfer structure of JSON response for create metric post operation. 'Id' is the primary key
 * value of created object.
 * @author cizquierdo
 */
@Data
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CreateMetricPostResponse {

  @JsonProperty("result")
  private Result result;

  public CreateMetricPostResponse() {
    result = new Result();
  }

}
