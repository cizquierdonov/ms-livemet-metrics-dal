package cl.cizquierdonov.metrics.dal.services;

import java.util.List;

import cl.cizquierdonov.metrics.dal.model.Average;
import cl.cizquierdonov.metrics.dal.model.MetricPostDTO;

/**
 * Defines the operations facade on {@link MetricPostDTO} resource.
 */
public interface MetricPostService {

  /**
   * Get a list of all metric posts of a specific type.
   * @param type
   * @return
   */
  public List<MetricPostDTO> getMetricPostsByType(String type);

  /**
   * Create a new metric post object.
   * @param metricPost  Data of metric post object to create.
   */
  public void createMetricPost(MetricPostDTO metricPost);

  /**
   * Calculates metric value average from a metric posts list of a specific metric type, in a specific date,
   * getting three different types of average: by day, hour and minute.
   * @param metricType  Metric type to filter.
   * @param strDate String represetation of the specific date.
   * @return  Average in string format (could be an integer or a double).
   */
  public Average getMetricAverageByDatetime(String type, String strDate);
  
}
