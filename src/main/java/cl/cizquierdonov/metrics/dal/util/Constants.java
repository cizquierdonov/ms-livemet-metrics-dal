package cl.cizquierdonov.metrics.dal.util;

/**
 * Defines all constants of the project.
 * @author
 */
public class Constants {

  public static final int HTTP_400_CODE = 400;
  public static final int HTTP_500_CODE = 500;
  public static final int EMPTY_FIELDS_RESPONSE_ERROR_CODE = -2;
  public static final int SUCCESS_RESPONSE_CODE = 0;
  public static final int EXCEPTION_RESPONSE_ERROR_CODE = -1;
  public static final int INVALID_FIELD_FORMAT_RESPONSE_ERROR_CODE = -3;

  public static final String MILLISECS_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final String SECS_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

  public static final String METRIC_TYPES_CONTEXT_PATH = "/metric-types";
  public static final String METRIC_POSTS_CONTEXT_PATH = "/metric-posts";
  public static final String METRIC_AVERAGE_CONTEXT_PATH = "/metric-posts/average";

  public static final String MESSAGE_SUCCESSFUL_OPERATON = "Successful operation.";
  public static final String MESSAGE_SUCCESSFUL_CREATION = "Resource created successfully.";
  public static final String ERROR_MESSAGE_GET_METRIC_TYPES = "An error ocurred getting active metrics.";
  public static final String ERROR_MESSAGE_GET_METRIC_POSTS = "An error ocurred getting metric posts.";
  public static final String ERROR_MESSAGE_CREATE_METRIC_TYPE = "An error ocurred creating metric type.";
  public static final String ERROR_MESSAGE_CREATE_METRIC_POST = "An error ocurred creating metric post.";
  public static final String ERROR_MESSAGE_GET_AVERAGE = "An error ocurred calculating metric average.";
  public static final String INVALID_REQUEST_MESSAGE_GET_POST_METRICS_BY_TYPE = "Query string parameter 'type' is mandatory and must not be empty.";
  public static final String INVALID_REQUEST_MESSAGE_CREATE_METRIC_TYPE = "Fields 'name' and 'description' inside an 'metricType' object are mandatory and must not be null or empty.";
  public static final String INVALID_REQUEST_MESSAGE_CREATE_METRIC_POST = "Fields 'type', 'value' and 'recordDate' inside an 'metricPost' element are mandatory, must not be null or empty.";
  public static final String INVALID_DATE_MESSAGE_CREATE_METRIC_POST = "Field 'recorDate' must have a valid format ('" + MILLISECS_DATE_FORMAT + "' or '" + SECS_DATE_FORMAT + "') and must be less than the current time.";
  public static final String INVALID_VALUE_MESSAGE_CREATE_METRIC_POST = "Field 'value' must be a number (could be float with '.' numeric separator or an integer) and must be a value between -9999.9 and 9999.9 inclusive (max 4 precision digits and 0 or 1 scale digits). Examples: \"9999\", \"1234.5\", \"68\", \"0.1\", etc.";
  public static final String INVALID_REQUEST_MESSAGE_GET_AVERAGE = "Fields 'metricType', and 'date' inside 'average' object are mandatory and must not be null or empty.";
  public static final String INVALID_AVERAGE_TYPE_MESSAGE_GET_AVERAGE = "Field 'averageType' value must be 'Hour', 'Minute' or 'Day'.";
  public static final String INVALID_DATE_MESSAGE_GET_AVERAGE = "Field 'date' must have a valid format ('" + MILLISECS_DATE_FORMAT + "' or '" + SECS_DATE_FORMAT + "') and must be less than the current time.";
}
