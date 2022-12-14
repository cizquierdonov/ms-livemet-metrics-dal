package cl.cizquierdonov.metrics.dal.api;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import cl.cizquierdonov.metrics.dal.entity.MetricPost;
import cl.cizquierdonov.metrics.dal.model.Average;
import cl.cizquierdonov.metrics.dal.model.MetricPostDTO;
import cl.cizquierdonov.metrics.dal.model.MetricTypeDTO;
import cl.cizquierdonov.metrics.dal.model.request.CreateMetricPostRequest;
import cl.cizquierdonov.metrics.dal.model.request.CreateMetricTypeRequest;
import cl.cizquierdonov.metrics.dal.model.request.GetAverageByDateRequest;
import cl.cizquierdonov.metrics.dal.model.response.CreateMetricPostResponse;
import cl.cizquierdonov.metrics.dal.model.response.CreateMetricTypeResponse;
import cl.cizquierdonov.metrics.dal.model.response.GetAverageByDateResponse;
import cl.cizquierdonov.metrics.dal.model.response.GetMetricPostsResponse;
import cl.cizquierdonov.metrics.dal.model.response.GetMetricTypesResponse;
import cl.cizquierdonov.metrics.dal.model.response.Result;
import cl.cizquierdonov.metrics.dal.services.MetricPostService;
import cl.cizquierdonov.metrics.dal.services.MetricTypeService;
import cl.cizquierdonov.metrics.dal.util.Constants;
import cl.cizquierdonov.metrics.dal.util.RequestDataValidator;

/**
 * Controller that exposes REST operations to manage metric entities.
 * @author cizquierdo
 */
@Path("/v1/")
@Produces(MediaType.APPLICATION_JSON)
public class MetricResources {

  private static final Logger LOG = Logger.getLogger(MetricResources.class);

  @Inject
  MetricTypeService metricTypeService;

  @Inject
  MetricPostService metricPostService;

  /**
   * Get all active metric types from database.
   * @return  Result of the operation and list of metric types encapsulated in a json object.
   */
  @GET
  @Path("/metric-types")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getActiveMetricTypes() {    
    try {
      
      List<MetricTypeDTO> metricTypes = metricTypeService.getActiveMetricTypes();

      Result result = new Result(0, Constants.MESSAGE_SUCCESSFUL_OPERATON);

      GetMetricTypesResponse response = new GetMetricTypesResponse();
      response.setMetricTypes(metricTypes);
      response.setResult(result);
      return Response.ok(response).build();

    } catch (RuntimeException e) {
      LOG.error(Constants.ERROR_MESSAGE_GET_METRIC_TYPES);
      LOG.error(e.getMessage(), e);

      Result result = new Result(Constants.EXCEPTION_RESPONSE_ERROR_CODE, Constants.ERROR_MESSAGE_GET_METRIC_TYPES);
      GetMetricTypesResponse response = new GetMetricTypesResponse();
      response.setResult(result);
      return Response.status(Constants.HTTP_500_CODE).entity(response).build();
    }
  }

  /**
   * Get metric posts list filtered by type from database.
   * @param type  Metric type name to apply filter.
   * @return  Result of the operation and list of {@link MetricPost} encapsulated in a json object.
   */
  @GET
  @Path("/metric-posts")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPostMetricPostsByType(@QueryParam("type") String type) {
    try {

      if ( (type == null) || ("".equals(type)) ) {
        Result errorResult = new Result(Constants.EMPTY_FIELDS_RESPONSE_ERROR_CODE, Constants.INVALID_REQUEST_MESSAGE_GET_POST_METRICS_BY_TYPE);
        return Response.status(Constants.HTTP_400_CODE).entity(errorResult).build();
      }

      List<MetricPostDTO> metricPosts = metricPostService.getMetricPostsByType(type);

      Result result = new Result(0, Constants.MESSAGE_SUCCESSFUL_OPERATON);

      GetMetricPostsResponse response = new GetMetricPostsResponse();
      response.setMetricPosts(metricPosts);
      response.setResult(result);
      
      return Response.ok(response).build();

    } catch (RuntimeException e) {
      LOG.error(Constants.ERROR_MESSAGE_GET_METRIC_TYPES);
      LOG.error(e.getMessage(), e);

      Result result = new Result(Constants.EXCEPTION_RESPONSE_ERROR_CODE, Constants.ERROR_MESSAGE_GET_METRIC_POSTS);
      GetMetricPostsResponse response = new GetMetricPostsResponse();
      response.setResult(result);
      return Response.status(Constants.HTTP_500_CODE).entity(response).build();
    }
  }

  /**
   * Create in database a new {@link MetricTypeDTO} object.
   * @param request  Contains data of the metric type to create.
   * @return  Result of the operation.
   */
  @POST
  @Path("/metric-types")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createMetricType(CreateMetricTypeRequest request) {
    try {

      if ( (request == null) || (request.getMetricType() == null)
            || !(RequestDataValidator.noEmptyFieldsMetricTypeRequest(request.getMetricType())) ) {
        Result errorResult = new Result(Constants.EMPTY_FIELDS_RESPONSE_ERROR_CODE, Constants.INVALID_REQUEST_MESSAGE_CREATE_METRIC_TYPE);
        CreateMetricTypeResponse createMetricTypeResponse = new CreateMetricTypeResponse(errorResult);
        return Response.status(Constants.HTTP_400_CODE).entity(createMetricTypeResponse).build();
      }

      metricTypeService.createMetricType(request.getMetricType());

      Result result = new Result(0, Constants.MESSAGE_SUCCESSFUL_CREATION);
      CreateMetricTypeResponse createMetricTypeResponse = new CreateMetricTypeResponse(result);

      return Response.ok(createMetricTypeResponse).build();

    } catch (RuntimeException e) {
      LOG.error(Constants.ERROR_MESSAGE_CREATE_METRIC_TYPE);
      LOG.error(e.getMessage(), e);

      Result result = new Result(Constants.EXCEPTION_RESPONSE_ERROR_CODE, Constants.ERROR_MESSAGE_CREATE_METRIC_TYPE);
      CreateMetricTypeResponse createMetricTypeResponse = new CreateMetricTypeResponse(result);
      return Response.status(Constants.HTTP_500_CODE).entity(createMetricTypeResponse).build();
    }
  }

  /**
   * Create in database a new {@link MetricPostDTO} object.
   * @param request  Contains data of the metric post to create.
   * @return  Result of the operation.
   */
  @POST
  @Path("/metric-posts")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createMetricPost(CreateMetricPostRequest request) {
    CreateMetricPostResponse createMetricPostResponse = null;
    try {

      if ( (request == null) || (request.getMetricPost() == null)
            || !(RequestDataValidator.noEmptyFieldsMetricPostRequest(request.getMetricPost())) )  {
        Result errorResult = new Result(Constants.EMPTY_FIELDS_RESPONSE_ERROR_CODE, Constants.INVALID_REQUEST_MESSAGE_CREATE_METRIC_POST);
        createMetricPostResponse = new CreateMetricPostResponse(errorResult);
        return Response.status(Constants.HTTP_400_CODE).entity(createMetricPostResponse).build();
      }

      MetricPostDTO metricPost = request.getMetricPost();

      if (!RequestDataValidator.isValidDate(metricPost.getRecordDate())) {
        Result errorResult = new Result(Constants.INVALID_FIELD_FORMAT_RESPONSE_ERROR_CODE, Constants.INVALID_DATE_MESSAGE_CREATE_METRIC_POST);
        createMetricPostResponse = new CreateMetricPostResponse(errorResult);
        return Response.status(Constants.HTTP_400_CODE).entity(createMetricPostResponse).build();
      }

      if (!RequestDataValidator.isValidValue(metricPost.getValue())) {
        Result errorResult = new Result(Constants.INVALID_FIELD_FORMAT_RESPONSE_ERROR_CODE, Constants.INVALID_VALUE_MESSAGE_CREATE_METRIC_POST);
        createMetricPostResponse = new CreateMetricPostResponse(errorResult);
        return Response.status(Constants.HTTP_400_CODE).entity(createMetricPostResponse).build();
      }

      metricPostService.createMetricPost(metricPost);

      Result result = new Result(Constants.SUCCESS_RESPONSE_CODE, Constants.MESSAGE_SUCCESSFUL_CREATION);
      createMetricPostResponse = new CreateMetricPostResponse(result);

      return Response.ok(createMetricPostResponse).build();

    } catch (RuntimeException e) {
      LOG.error(Constants.ERROR_MESSAGE_CREATE_METRIC_POST);
      LOG.error(e.getMessage(), e);

      Result errorResult = new Result(Constants.EXCEPTION_RESPONSE_ERROR_CODE, Constants.ERROR_MESSAGE_CREATE_METRIC_POST);
      createMetricPostResponse = new CreateMetricPostResponse(errorResult);

      return Response.status(Constants.HTTP_500_CODE).entity(createMetricPostResponse).build();
    }
  }

  /**
   * Calculates the average from a list of metric posts values, for a specific metric type and inside a specific date.
   * The average to calculate could be in a {@link AverageType#DAY}, {@link AverageType#HOUR} or
   * {@link AverageType#MINUTE}.
   * @param request   Contains the metric type, average type and date.
   * @return  String numeric value of the average, could be an integer or a double.
   */
  @POST
  @Path("/metric-posts/average")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAverageByDate(GetAverageByDateRequest request) {
    GetAverageByDateResponse getAverageByDateResponse = new GetAverageByDateResponse();
    try {

      if ( (request == null) || (request.getAverage() == null)
            || !(RequestDataValidator.noEmptyFieldsGetAverageRequest(request.getAverage())) ) {

        Result errorResult = new Result(Constants.EMPTY_FIELDS_RESPONSE_ERROR_CODE, Constants.INVALID_REQUEST_MESSAGE_GET_AVERAGE);
        if (request != null) {
          getAverageByDateResponse.setAverage(request.getAverage());
        }
        getAverageByDateResponse.setResult(errorResult);
        return Response.status(Constants.HTTP_400_CODE).entity(getAverageByDateResponse).build();

      }

      Average average = request.getAverage();

      if (!RequestDataValidator.isValidDate(average.getDate())) {
        Result errorResult = new Result(Constants.INVALID_FIELD_FORMAT_RESPONSE_ERROR_CODE, Constants.INVALID_DATE_MESSAGE_GET_AVERAGE);
        getAverageByDateResponse.setAverage(request.getAverage());
        getAverageByDateResponse.setResult(errorResult);
        return Response.status(Constants.HTTP_400_CODE).entity(getAverageByDateResponse).build();
      }

      Average avg = metricPostService.getMetricAverageByDatetime(average.getMetricType(), average.getDate());

      Result result = new Result(0, Constants.MESSAGE_SUCCESSFUL_OPERATON);      
      getAverageByDateResponse.setAverage(avg);
      getAverageByDateResponse.setResult(result);    
      
      return Response.ok(getAverageByDateResponse).build();

    } catch (RuntimeException e) {
      LOG.error(Constants.ERROR_MESSAGE_GET_AVERAGE);
      LOG.error(e.getMessage(), e);

      Result result = new Result(Constants.EXCEPTION_RESPONSE_ERROR_CODE, Constants.ERROR_MESSAGE_GET_AVERAGE);
      getAverageByDateResponse.setAverage(request.getAverage());
      getAverageByDateResponse.setResult(result);
      return Response.status(Constants.HTTP_500_CODE).entity(getAverageByDateResponse).build();
    }
  }
  
}
