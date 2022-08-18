package cl.cizquierdonov.metrics.dal.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;

import cl.cizquierdonov.metrics.dal.entity.MetricType;
import cl.cizquierdonov.metrics.dal.model.MetricTypeDTO;
import cl.cizquierdonov.metrics.dal.services.MetricTypeService;
import cl.cizquierdonov.metrics.dal.util.Constants;

/**
 * Implements operations of {@link MetricTypeService} interface and the business logic on the resource {@link MetricTypeDTO}. 
 */
@ApplicationScoped
public class MetricTypeServiceImpl implements MetricTypeService {

  private static final Logger LOG = Logger.getLogger(MetricTypeServiceImpl.class);

  @Override
  public List<MetricTypeDTO> getActiveMetricTypes() {
    LOG.info("[" + Constants.METRIC_TYPES_CONTEXT_PATH + "] Getting active metrics.");

    List<MetricType> entities = MetricType.getActiveMetrics();
      
    List<MetricTypeDTO> metricTypes = new ArrayList<>();
    
    for (MetricType entity : entities) {
      MetricTypeDTO metricType = buildMetricType(entity);
      metricTypes.add(metricType);
    }

    LOG.info("[" + Constants.METRIC_TYPES_CONTEXT_PATH + "] Result: " + metricTypes);

    return metricTypes;
  }

  @Override
  @Transactional
  public void createMetricType(MetricTypeDTO metricType) {
    LOG.info("[" + Constants.METRIC_TYPES_CONTEXT_PATH + "] Creating new metric type: " + metricType + "");

    MetricType entity = buildMetricTypeEntity(metricType);
    MetricType.persist(entity);

    LOG.info("[" + Constants.METRIC_TYPES_CONTEXT_PATH + "] Metric type '" + metricType.getName() + "'' created.");
  }

  /**
   * Create new {@link MetricType} object for database from {@link MetricTypeDTO} request object.
   * @param metricType  Input metric type DTO.
   * @return  Entity to call database operations.
   */
  private MetricType buildMetricTypeEntity(MetricTypeDTO metricType) {
    MetricType entity = new MetricType();
    
    if (metricType != null) {
      entity.setName(metricType.getName());
      entity.setDescription(metricType.getDescription());
      entity.setActive(true);
      entity.setSuffix(metricType.getSuffix() != null ? metricType.getSuffix() : "");
      entity.setScale(metricType.isScale());
    }
    
    return entity;
  }

  /**
   * Create new {@link MetricTypeDTO} DTO for response from {@link MetricType} database object.
   * @param entity
   * @return
   */
  private MetricTypeDTO buildMetricType(MetricType entity) {
    MetricTypeDTO metricType = new MetricTypeDTO();

    if (entity != null) {
      metricType.setName(entity.getName());
      metricType.setDescription(entity.getDescription());
      metricType.setSuffix(entity.getSuffix());
      metricType.setScale(entity.isScale());
    }

    return metricType;
  }
  
}
