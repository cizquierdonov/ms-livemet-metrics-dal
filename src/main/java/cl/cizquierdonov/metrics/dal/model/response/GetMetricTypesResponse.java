package cl.cizquierdonov.metrics.dal.model.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.cizquierdonov.metrics.dal.model.MetricTypeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data object transfer structure of JSON response for get metric types operation.
 * @author cizquierdo
 */
@Data
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GetMetricTypesResponse {

  @JsonProperty("metricTypes")
  private List<MetricTypeDTO> metricTypes;

  @JsonProperty("result")
  private Result result;

  public GetMetricTypesResponse() {
    metricTypes = new ArrayList<>();
    result = new Result();
  }

}