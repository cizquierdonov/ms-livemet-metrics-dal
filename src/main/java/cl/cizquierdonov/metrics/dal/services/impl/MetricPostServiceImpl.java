package cl.cizquierdonov.metrics.dal.services.impl;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;

import cl.cizquierdonov.metrics.dal.entity.MetricPost;
import cl.cizquierdonov.metrics.dal.entity.MetricType;
import cl.cizquierdonov.metrics.dal.model.AverageType;
import cl.cizquierdonov.metrics.dal.model.MetricPostDTO;
import cl.cizquierdonov.metrics.dal.services.MetricPostService;
import cl.cizquierdonov.metrics.dal.util.Constants;
import cl.cizquierdonov.metrics.dal.util.RequestDataValidator;

/**
 * Implements operations of {@link MetricPostService} interface and the business logic on the resource {@link MetricPostDTO}. 
 */
@ApplicationScoped
public class MetricPostServiceImpl implements MetricPostService {

  private static final Logger LOG = Logger.getLogger(MetricPostServiceImpl.class);

  @Override
  public List<MetricPostDTO> getMetricPostsByType(String type) {
    LOG.info("[" + Constants.METRIC_POSTS_CONTEXT_PATH + "] Getting metric posts of type '" + type + "''.");

    List<MetricPost> entities = MetricPost.getMetricPostsByType(type);
    MetricType metricTypeEntity = MetricType.findById(type);

    List<MetricPostDTO> metricPosts = new ArrayList<>();

    for (MetricPost entity : entities) {
      MetricPostDTO metricPost = buildMetricPost(entity, metricTypeEntity);
      metricPosts.add(metricPost);
    }

    LOG.info("[" + Constants.METRIC_POSTS_CONTEXT_PATH + "] Number of post metrics: " + metricPosts.size());

    return metricPosts;
  }

  @Override
  @Transactional
  public void createMetricPost(MetricPostDTO metricPost) {
    LOG.info("[" + Constants.METRIC_POSTS_CONTEXT_PATH + "] Creating new metric type: '" + metricPost + "'.");

    MetricPost entity = buildMetricPostEntity(metricPost);
    MetricPost.persist(entity);

    LOG.info("[" + Constants.METRIC_POSTS_CONTEXT_PATH + "] Metric post created with ID '" + metricPost.getId() + "'.");    
  }

  @Override
  public String getMetricAverageByDatetime(String type, AverageType avgType, String strDate) {

    LOG.info("[" + Constants.METRIC_AVERAGE_CONTEXT_PATH + "] Calculating post metrics average: metric=" + type
          + ", average=" + avgType + ", date=" + strDate + ".");
    String strAvg = null;
    Timestamp from = null;
    Timestamp to = null;
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.SECS_DATE_FORMAT);
    Calendar calendarFrom = Calendar.getInstance();
    Calendar calendarTo = Calendar.getInstance();

    try {
      Date date = sdf.parse(strDate);      
      calendarFrom.setTime(date);
      calendarTo.setTime(date);      
      calendarFrom.set(Calendar.MILLISECOND, 0);
      calendarTo.set(Calendar.MILLISECOND, 0);
      calendarFrom.set(Calendar.SECOND, 0);
      calendarTo.set(Calendar.SECOND, 0);
      
      if (avgType == AverageType.MINUTE) {
        LOG.info("IS MINUTE!!!");
        calendarTo.add(Calendar.MINUTE, 1);
        
      } else if (avgType == AverageType.HOUR) {
        LOG.info("IS HOUR!!!");
        calendarFrom.set(Calendar.MINUTE, 0);
        calendarTo.set(Calendar.MINUTE, 0);
        calendarTo.add(Calendar.HOUR, 1);
      
      } else if (avgType == AverageType.DAY) {
        LOG.info("IS DAY!!!");
        calendarFrom.set(Calendar.MINUTE, 0);
        calendarTo.set(Calendar.MINUTE, 0);
        calendarFrom.set(Calendar.HOUR, 0);
        calendarTo.set(Calendar.HOUR, 0);
        calendarTo.add(Calendar.DAY_OF_MONTH, 1);
      }

    } catch (ParseException e) {
      LOG.error(e.getMessage(), e);
      return null;
    }

    from = new Timestamp(calendarFrom.getTime().getTime());
    to = new Timestamp(calendarTo.getTime().getTime());
    List<MetricPost> entities = MetricPost.getAverageByDateRange(type, from, to);
    Double avg = 0.0;

    for (MetricPost entity : entities) {
      avg += entity.getValue();
    }

    if (avg != 0) {
      avg /= entities.size();
      strAvg = String.format("%.1f", avg).replace(",", ".");

    } else {
      strAvg = "0.0";
    }

    LOG.info("[" + Constants.METRIC_AVERAGE_CONTEXT_PATH + "] Average calculated: '" + strAvg + "'.");

    return strAvg;
  }

  /**
   * Create new {@link MetricPost} object for database from {@link MetricPostDTO} request object.
   * @param metricPost  Input metric post DTO.
   * @return  Entity to call database operations.
   */
  private static MetricPost buildMetricPostEntity(MetricPostDTO metricPost) {
    MetricPost entity = new MetricPost();
    
    if (metricPost != null) {
      entity.setType(metricPost.getType());
      entity.setReview(metricPost.getReview());

      entity.setValue(metricPost.getValue() != null ? Float.parseFloat(metricPost.getValue()) : 0.0 );
      Timestamp timestamp = RequestDataValidator.getTimestampFromString(metricPost.getRecordDate());
      entity.setRecordDate(timestamp);
    }
    
    return entity;
  }

  /**
   * Create new {@link MetricPostDTO} DTO for response from {@link MetricPost} database object.
   * @param entity
   * @return
   */
  private MetricPostDTO buildMetricPost(MetricPost entity, MetricType metricTypeEntity) {
    MetricPostDTO metricPost = new MetricPostDTO();

    if (entity != null) {
      metricPost.setId(entity.getMetricPostId());
      metricPost.setType(entity.getType());
      NumberFormat nf = DecimalFormat.getInstance();

      if (metricTypeEntity.isScale()) {
        nf.setMaximumFractionDigits(1);
      } else {
        nf.setMaximumFractionDigits(0);
      }

      String value = nf.format(entity.getValue());

      if ( (metricTypeEntity.getSuffix() != null) && !(metricTypeEntity.getSuffix().equals("")) ) {
        value = value.concat(" ").concat(metricTypeEntity.getSuffix());
      }

      metricPost.setValue(value);
      metricPost.setReview(entity.getReview());
      String recordDate = RequestDataValidator.getStringFromTimestamp(entity.getRecordDate());
      metricPost.setRecordDate(recordDate);
    }

    return metricPost;
  }

  
  
}
