package cl.cizquierdonov.metrics.dal.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cl.cizquierdonov.metrics.dal.entity.MetricPost;
import cl.cizquierdonov.metrics.dal.entity.MetricType;
import cl.cizquierdonov.metrics.dal.model.Average;
import cl.cizquierdonov.metrics.dal.model.MetricPostDTO;
import cl.cizquierdonov.metrics.dal.model.MetricTypeDTO;

/**
 * Util class that implements request data validations and object parse methods.
 * @author cizquierdo
 */
public class RequestDataValidator {
  
  /**
   * Validates a {@link MetricType} object, checking if attributes aren't null or empty.
   * @param metricType  Metric Type object to validate.
   * @return  Returns true if all attributes aren't null or empty, false in any other case.
   */
  public static boolean noEmptyFieldsMetricTypeRequest(MetricTypeDTO metricType) {
    if ( (metricType.getName() != null) && (!metricType.getName().trim().equals(""))
          && (metricType.getDescription() != null) && (!metricType.getDescription().trim().equals("")) ) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Validates a {@link MetricPost} object, checking if attributes aren't null or empty.
   * @param metricPost  Metric Post object to validate.
   * @return  Returns true if all conditions described all are fulfilled, false in any other case.
   */
  public static boolean noEmptyFieldsMetricPostRequest(MetricPostDTO metricPost) {
    if ( (metricPost.getType() != null) && (!metricPost.getType().trim().equals(""))
          && (metricPost.getValue() != null) && (!metricPost.getValue().trim().equals(""))
          && (metricPost.getRecordDate() != null) ) {
      return true;
    } else {
      return false;
    }
  }
  
  /**
   * Validates a {@link Average} object, checking if attributes aren't null or empty.
   * @param average Average object to validate.
   * @return  Returns true if all conditions described all are fulfilled, false in any other case.
   */
  public static boolean noEmptyFieldsGetAverageRequest(Average average) {
    if ( (average.getMetricType() != null) && (!average.getMetricType().trim().equals(""))
          && (average.getDate() != null) ) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Checks if a string date has valid format ({@link Constants#MILLISECS_DATE_FORMAT} or
   * {@link Constants#SECS_DATE_FORMAT}) and checks if date is before than right now.
   * @param strDate Date in String format.
   * @return  Returns true if is valid date, otherwise returns false.
   */
  public static boolean isValidDate(String strDate) {    
    Timestamp timestamp = getTimestampFromString(strDate);

    if ( (timestamp != null) && (timestamp.before(new Date())) ) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Verify is a string is a representation of a number.
   * @param strNumber   Number in string format.
   * @return
   */
  public static boolean isValidValue(String strNumber) {
    Double number = null;
    
    if (strNumber == null) {
      return false;
    }

    try {
      number = Double.parseDouble(strNumber);
    } catch (NumberFormatException nfe) {
        return false;
    }

    if ( (number < -9999.9) || (number > 9999.9) ) {
      return false;
    }

    return true;
  }

  /**
   * Get timestamp object from string date, using {@link Constants.MILLISECS_DATE_FORMAT} or
   * {@link Constants.SECS_DATE_FORMAT} format.
   * @param strDate   Date in string format.
   * @return  {@link Timestamp} object equivalent to string date.
   */
  public static Timestamp getTimestampFromString(String strDate) {
    Timestamp timestamp = null;
    SimpleDateFormat millisecsSdf = new SimpleDateFormat(Constants.MILLISECS_DATE_FORMAT);
    SimpleDateFormat secsSdf = new SimpleDateFormat(Constants.SECS_DATE_FORMAT);

    try {
      Date date = millisecsSdf.parse(strDate);
      timestamp = new Timestamp(date.getTime());

    } catch (ParseException e) {
      try {
        Date date = secsSdf.parse(strDate);
        timestamp = new Timestamp(date.getTime());
      } catch (ParseException e2) {
        timestamp = null;
      }
    }
    
    return timestamp;
  }

  /**
   * Get date string representation from Timestamp object, using {@link Constants.MILLISECS_DATE_FORMAT} format.
   * @param strDate   Date in string format.
   * @return  {@link Timestamp} Timestamp object to convert to string.
   */
  public static String getStringFromTimestamp(Timestamp timestamp) {
    SimpleDateFormat millisecsSdf = new SimpleDateFormat(Constants.MILLISECS_DATE_FORMAT);

    String strDate = millisecsSdf.format(timestamp);
    
    return strDate;
  }

}