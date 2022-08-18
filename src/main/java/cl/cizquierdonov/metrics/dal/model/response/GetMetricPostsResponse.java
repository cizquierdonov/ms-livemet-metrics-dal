package cl.cizquierdonov.metrics.dal.model.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.cizquierdonov.metrics.dal.model.MetricPostDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data object transfer structure of JSON response for get metric posts operation.
 * @author cizquierdo
 */
@Data
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GetMetricPostsResponse {

  @JsonProperty("metricPosts")
  private List<MetricPostDTO> metricPosts;

  @JsonProperty("result")
  private Result result;

  public GetMetricPostsResponse() {
    metricPosts = new ArrayList<>();
    result = new Result();
  }
  
}
