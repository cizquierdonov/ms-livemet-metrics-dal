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
import cl.cizquierdonov.metrics.dal.model.Average;
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
  public Average getMetricAverageByDatetime(String type, String strDate) {

    LOG.info("[" + Constants.METRIC_AVERAGE_CONTEXT_PATH + "] Calculating post metrics average: metric=" + type
          + ", date=" + strDate + ".");

    Average average = new Average();
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

    } catch (ParseException e) {
      LOG.error(e.getMessage(), e);
      return null;
    }

    MetricType metricTypeEntity = MetricType.findById(type);
    String suffix = metricTypeEntity.getSuffix();

    String avgPerMinute = calculateAveragePerMinute(type, (Calendar) calendarFrom.clone(), (Calendar) calendarTo.clone(), suffix);
    String avgPerHour = calculateAveragePerHour(type, (Calendar) calendarFrom.clone(), (Calendar) calendarTo.clone(), suffix);
    String avgPerDay = calculateAveragePerDay(type, (Calendar) calendarFrom.clone(), (Calendar) calendarTo.clone(), suffix);

    LOG.info("[" + Constants.METRIC_AVERAGE_CONTEXT_PATH + "] Average calculated: per minute: '" 
          + avgPerMinute + "', per hour: '" + avgPerHour + "', per day: '" + avgPerDay + "'");

    average.setAvgPerMinute(avgPerMinute);
    average.setAvgPerHour(avgPerHour);    
    average.setAvgPerDay(avgPerDay);
    average.setDate(strDate);
    average.setMetricType(type);
    
    return average;
  }

  /**
   * Calculate post metric values average from specific date to one minute ahead for a specific metric type.
   * @param type  Metric type name.
   * @param calendarFrom  Date 'from' for average calculation.
   * @param calendarTo  Date 'to' for average calculation.
   * @param suffix  Concatenated at the end of the calculated average, to indicate the type of metric.
   * @return   Average amount string with metric suffix at the end.
   */
  private String calculateAveragePerMinute(String type, Calendar calendarFrom, Calendar calendarTo, String suffix) {
    String avgStr = null;

    calendarTo.add(Calendar.MINUTE, 1);

    avgStr = calculateAverage(type, calendarFrom, calendarTo, suffix);

    return avgStr;
  }

  /**
   * Calculate post metric values average from specific date to one hour ahead for a specific metric type.
   * @param type  Metric type name.
   * @param calendarFrom  Date 'from' for average calculation.
   * @param calendarTo  Date 'to' for average calculation.
   * @param suffix  Concatenated at the end of the calculated average, to indicate the type of metric.
   * @return   Average amount string with metric suffix at the end.
   */
  private String calculateAveragePerHour(String type, Calendar calendarFrom, Calendar calendarTo, String suffix) {
    String avgStr = null;

    calendarTo.add(Calendar.HOUR, 1);

    avgStr = calculateAverage(type, calendarFrom, calendarTo, suffix);

    return avgStr;
  }

  /**
   * Calculate post metric values average from specific date to one day ahead for a specific metric type.
   * @param type  Metric type name.
   * @param calendarFrom  Date 'from' for average calculation.
   * @param calendarTo  Date 'to' for average calculation.
   * @param suffix  Concatenated at the end of the calculated average, to indicate the type of metric.
   * @return   Average amount string with metric suffix at the end.
   */
  private String calculateAveragePerDay(String type, Calendar calendarFrom, Calendar calendarTo, String suffix) {
    String avgStr = null;

    calendarTo.add(Calendar.DAY_OF_MONTH, 1);
    avgStr = calculateAverage(type, calendarFrom, calendarTo, suffix);

    return avgStr;
  }

  /**
   * Get post metrics by type and by a date range from database and calculates a simply average.
   * @param type  Metric type name to apply filter.
   * @param calendarFrom  Date 'from' for average calculation.
   * @param calendarTo  Date 'to' for average calculation.
   * @param suffix  Concatenated at the end of the calculated average, to indicate the type of metric.
   * @return  Average amount string with metric suffix at the end.
   */
  private String calculateAverage(String type, Calendar calendarFrom, Calendar calendarTo, String suffix) {
    String avgStr;
    Timestamp from = new Timestamp(calendarFrom.getTime().getTime());
    Timestamp to = new Timestamp(calendarTo.getTime().getTime());
    List<MetricPost> entities = MetricPost.getAverageByDateRange(type, from, to);
    Double avg = 0.0;

    for (MetricPost entity : entities) {
      avg += entity.getValue();
    }

    if (avg != 0) {
      avg /= entities.size();
      avgStr = String.format("%.1f", avg).replace(",", ".").concat(" ").concat(suffix);

    } else {
      avgStr = "undefined";
    }
    return avgStr;
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
