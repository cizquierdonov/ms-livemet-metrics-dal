package cl.cizquierdonov.metrics.dal.model.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.cizquierdonov.metrics.dal.model.MetricTypeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Wrapper request DTO class to metric type creation.
 */
@Data
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CreateMetricTypeRequest {

  @JsonProperty("metricType")
  private MetricTypeDTO metricType;

  public CreateMetricTypeRequest() {
    metricType = new MetricTypeDTO();
  }

}
