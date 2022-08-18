package cl.cizquierdonov.metrics.dal.model.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.cizquierdonov.metrics.dal.model.MetricPostDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Wrapper request DTO class to metric post creation.
 */
@Data
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CreateMetricPostRequest {

  @JsonProperty("metricPost")
  private MetricPostDTO metricPost;

  public CreateMetricPostRequest() {
    metricPost = new MetricPostDTO();
  }
  
}
