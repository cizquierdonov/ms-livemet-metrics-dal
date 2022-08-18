package cl.cizquierdonov.metrics.dal.model.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data object transfer structure that defines the result of any REST operations in the component.
 * A positive 'code' indicates a successful operation execution, a negative 'code' indicates an error.
 * The 'message' describes a detail of operation result.
 * @author cizquierdo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Result {

  @JsonProperty("code")
  private Integer code;

  @JsonProperty("message")
  private String message;
  
}
