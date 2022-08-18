package cl.cizquierdonov.metrics.dal.services;

import java.util.List;

import cl.cizquierdonov.metrics.dal.model.MetricTypeDTO;

/**
 * Defines the operations facade on {@link MetricTypeDTO} resource.
 */
public interface MetricTypeService {

  /**
   * Get a list of all active metric types.
   * @return  List of {@link MetricTypeDTO}
   */
  public List<MetricTypeDTO> getActiveMetricTypes();

  /**
   * Create a new metric type object.
   * @param metricType  Data of metric type object to create.
   */
  public void createMetricType(MetricTypeDTO metricType);

}
