package cl.cizquierdonov.metrics.dal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import cl.cizquierdonov.metrics.dal.model.Average;
import cl.cizquierdonov.metrics.dal.model.MetricPostDTO;
import cl.cizquierdonov.metrics.dal.model.MetricTypeDTO;
import cl.cizquierdonov.metrics.dal.model.request.CreateMetricPostRequest;
import cl.cizquierdonov.metrics.dal.model.request.CreateMetricTypeRequest;
import cl.cizquierdonov.metrics.dal.model.request.GetAverageByDateRequest;
import cl.cizquierdonov.metrics.dal.services.MetricPostService;
import cl.cizquierdonov.metrics.dal.services.MetricTypeService;
import cl.cizquierdonov.metrics.dal.util.Constants;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class MetricResourcesTest {

  @InjectMock 
  MetricTypeService metricTypeService;

  @InjectMock
  MetricPostService metricPostService;

  static List<MetricTypeDTO> metricTypes;
  static List<MetricPostDTO> posts;
  static CreateMetricTypeRequest createMetricTypeRequest;
  static CreateMetricPostRequest createMetricPostRequest;
  static GetAverageByDateRequest getAverageByDateRequest;

  @BeforeAll
  public static void setup() {
    MetricTypeDTO metric1 = new MetricTypeDTO("Temperature", "Metric to measure the weather expressed in celsius",
          true, "°C", true);
    MetricTypeDTO metric2 = new MetricTypeDTO("Height", "Height of a person in centimeters",
          true, "°C", false);
    
    metricTypes = new ArrayList<>();
    metricTypes.add(metric1);
    metricTypes.add(metric2);

    MetricPostDTO post1 = new MetricPostDTO(1, "Temperature", "20.5", "My city weather today", "2022-08-17 01:53:00.123");
    MetricPostDTO post2 = new MetricPostDTO(2, "Temperature", "-4", "Punta Arenas weather yesterday", "2022-08-16 16:05:00.000");

    posts = new ArrayList<>();
    posts.add(post1);
    posts.add(post2);

    Average average = new Average();
    average.setMetricType("Temperature");
    average.setDate("2022-08-17 00:00:00.000");
    getAverageByDateRequest = new GetAverageByDateRequest(average);
  }

  @AfterAll
  public static void cleanUp() {
    metricTypes.clear();
    posts.clear();
  }

  @Test
  public void getActiveMetricTypesHttp200Test() {    
    when(metricTypeService.getActiveMetricTypes()).thenReturn(metricTypes);

    given()
      .when()
      .get("/v1/metric-types")
      .then()
      .statusCode(200)
      .body("size()", is(2))
      .body("result.code", equalTo(0))
      .body("metricTypes[0].name", equalTo(metricTypes.get(0).getName()))
      .body("metricTypes[0].description", equalTo(metricTypes.get(0).getDescription()))
      .body("metricTypes[0].active", equalTo(metricTypes.get(0).isActive()))
      .body("metricTypes[0].suffix", equalTo(metricTypes.get(0).getSuffix()))
      .body("metricTypes[0].scale", equalTo(metricTypes.get(0).isScale()))
      .body("metricTypes[1].name", equalTo(metricTypes.get(1).getName()))
      .body("metricTypes[1].description", equalTo(metricTypes.get(1).getDescription()))
      .body("metricTypes[1].active", equalTo(metricTypes.get(1).isActive()))
      .body("metricTypes[1].suffix", equalTo(metricTypes.get(1).getSuffix()))
      .body("metricTypes[1].scale", equalTo(metricTypes.get(1).isScale()));

  }

  @Test
  public void getActiveMetricTypesHttp500Test() {
    when(metricTypeService.getActiveMetricTypes()).thenThrow(RuntimeException.class);

    given()
      .when()
      .get("/v1/metric-types")
      .then()
      .statusCode(Constants.HTTP_500_CODE)
      .body("result.code", equalTo(-1));

  }

  @Test
  public void getPostMetricPostsByTypeHttp200Test() {
    String metricType = "Temperature";

    when(metricPostService.getMetricPostsByType(metricType)).thenReturn(posts);

    given()
      .when()
      .get(String.format("/v1/metric-posts?type=%s", metricType))
      .then()
      .statusCode(200)
      .body("size()", is(2))
      .body("result.code", equalTo(0))
      .body("metricPosts[0].id", equalTo(posts.get(0).getId()))
      .body("metricPosts[0].type", equalTo(posts.get(0).getType()))
      .body("metricPosts[0].value", equalTo(posts.get(0).getValue()))
      .body("metricPosts[0].review", equalTo(posts.get(0).getReview()))
      .body("metricPosts[0].recordDate", equalTo(posts.get(0).getRecordDate()))
      .body("metricPosts[1].id", equalTo(posts.get(1).getId()))
      .body("metricPosts[1].type", equalTo(posts.get(1).getType()))
      .body("metricPosts[1].value", equalTo(posts.get(1).getValue()))
      .body("metricPosts[1].review", equalTo(posts.get(1).getReview()))
      .body("metricPosts[1].recordDate", equalTo(posts.get(1).getRecordDate()));
  
  }

  @Test
  public void getPostMetricPostsByTypeHttp500Test() {
    String metricType = "Weight";

    when(metricPostService.getMetricPostsByType(metricType)).thenThrow(RuntimeException.class);

    given()
      .when()
      .get(String.format("/v1/metric-posts?type=%s", metricType))
      .then()
      .statusCode(Constants.HTTP_500_CODE)
      .body("result.code", equalTo(-1));

  }

  @Test
  public void createMetricTypeHttp200Test() {
    MetricTypeDTO metricType = metricTypes.get(0).clone();
    createMetricTypeRequest = new CreateMetricTypeRequest(metricType);
    doNothing().when(metricTypeService).createMetricType(metricType);

    given()
      .when()
      .header("Content-Type", "application/json")
      .body(createMetricTypeRequest)
      .post("/v1/metric-types")      
      .then()
      .statusCode(200)
      .body("result.code", equalTo(0));

  }

  @Test
  public void createMetricTypeHttp400Test() {
    MetricTypeDTO metricType = metricTypes.get(0).clone();
    metricType.setName("");
    createMetricTypeRequest = new CreateMetricTypeRequest(metricType);

    given()
      .when()
      .header("Content-Type", "application/json")
      .body(createMetricTypeRequest)
      .post("/v1/metric-types")      
      .then()
      .statusCode(Constants.HTTP_400_CODE)
      .body("result.code", equalTo(-2));
    
  }

  @Test
  public void createMetricTypeHttp500Test() {
    MetricTypeDTO metricType = metricTypes.get(0).clone();
    createMetricTypeRequest = new CreateMetricTypeRequest(metricType);
    doThrow(RuntimeException.class).when(metricTypeService).createMetricType(metricType);

    given()
      .when()
      .header("Content-Type", "application/json")
      .body(createMetricTypeRequest)
      .post("/v1/metric-types")      
      .then()
      .statusCode(Constants.HTTP_500_CODE)
      .body("result.code", equalTo(-1));

  }

  @Test
  public void createMetricPostHttp200Test() {
    MetricPostDTO post = posts.get(0).clone();
    createMetricPostRequest = new CreateMetricPostRequest(post);
    doNothing().when(metricPostService).createMetricPost(post);

    given()
      .when()
      .header("Content-Type", "application/json")
      .body(createMetricPostRequest)
      .post("/v1/metric-posts")      
      .then()
      .statusCode(200)
      .body("result.code", equalTo(0));
  
  }

  @Test
  public void createMetricPostHttp400Test() {
    MetricPostDTO post = posts.get(0).clone();
    post.setType(null);
    createMetricPostRequest = new CreateMetricPostRequest(post);

    given()
      .when()
      .header("Content-Type", "application/json")
      .body(createMetricPostRequest)
      .post("/v1/metric-posts")      
      .then()
      .statusCode(Constants.HTTP_400_CODE)
      .body("result.code", equalTo(Constants.EMPTY_FIELDS_RESPONSE_ERROR_CODE));

    Calendar futureDate = Calendar.getInstance();
    futureDate.add(Calendar.DAY_OF_MONTH, 1);
    String futureDateStr = new SimpleDateFormat(Constants.SECS_DATE_FORMAT).format(futureDate.getTime());
    post.setRecordDate(futureDateStr);
    post.setType("Temperature");    
    createMetricPostRequest = new CreateMetricPostRequest(post);

    given()
      .when()
      .header("Content-Type", "application/json")
      .body(createMetricPostRequest)
      .post("/v1/metric-posts")      
      .then()
      .statusCode(Constants.HTTP_400_CODE)
      .body("result.code", equalTo(Constants.INVALID_FIELD_FORMAT_RESPONSE_ERROR_CODE));

    Calendar currentDate = Calendar.getInstance();
    String currentDateStr = new SimpleDateFormat(Constants.SECS_DATE_FORMAT).format(currentDate.getTime());
    post.setRecordDate(currentDateStr);
    post.setValue("ABC");    
    createMetricPostRequest = new CreateMetricPostRequest(post);

    given()
      .when()
      .header("Content-Type", "application/json")
      .body(createMetricPostRequest)
      .post("/v1/metric-posts")      
      .then()
      .statusCode(Constants.HTTP_400_CODE)
      .body("result.code", equalTo(Constants.INVALID_FIELD_FORMAT_RESPONSE_ERROR_CODE));
  
  }

  @Test
  public void createMetricPostHttp500Test() {
    MetricPostDTO post = posts.get(0).clone();
    createMetricPostRequest = new CreateMetricPostRequest(post);
    doThrow(RuntimeException.class).when(metricPostService).createMetricPost(post);

    given()
      .when()
      .header("Content-Type", "application/json")
      .body(createMetricPostRequest)
      .post("/v1/metric-posts")      
      .then()
      .statusCode(Constants.HTTP_500_CODE)
      .body("result.code", equalTo(Constants.EXCEPTION_RESPONSE_ERROR_CODE));
  
  }

  @Test
  public void getAverageByDateHttp200Test() {
    Average resAvg = getAverageByDateRequest.getAverage().clone();
    resAvg.setAvgPerDay("25.3 °C");
    resAvg.setAvgPerMinute("20 °C");
    resAvg.setAvgPerHour("23.3 °C");
    resAvg.setDate("2022-08-17 00:00:00.000");
    getAverageByDateRequest = new GetAverageByDateRequest(resAvg);
    when(metricPostService.getMetricAverageByDatetime(resAvg.getMetricType(), resAvg.getDate())).thenReturn(resAvg);

    given()
      .when()
      .header("Content-Type", "application/json")
      .body(getAverageByDateRequest)
      .post("/v1/metric-posts/average")      
      .then()
      .statusCode(200)
      .body("result.code", equalTo(Constants.SUCCESS_RESPONSE_CODE))
      .body("average.avgPerDay", equalTo(resAvg.getAvgPerDay()))
      .body("average.avgPerMinute", equalTo(resAvg.getAvgPerMinute()))
      .body("average.avgPerHour", equalTo(resAvg.getAvgPerHour()));
  }

  @Test
  public void getAverageByDateHttp400Test() {
    Average resAvg = getAverageByDateRequest.getAverage().clone();
    resAvg.setMetricType(null);
    getAverageByDateRequest = new GetAverageByDateRequest(resAvg);

    given()
      .when()
      .header("Content-Type", "application/json")
      .body(getAverageByDateRequest)
      .post("/v1/metric-posts/average")      
      .then()
      .statusCode(Constants.HTTP_400_CODE)
      .body("result.code", equalTo(Constants.EMPTY_FIELDS_RESPONSE_ERROR_CODE));

    resAvg.setMetricType("Temperature");
    Calendar futureDate = Calendar.getInstance();
    futureDate.add(Calendar.DAY_OF_MONTH, 1);
    String futureDateStr = new SimpleDateFormat(Constants.SECS_DATE_FORMAT).format(futureDate.getTime());
    resAvg.setDate(futureDateStr);
    getAverageByDateRequest = new GetAverageByDateRequest(resAvg);

    given()
      .when()
      .header("Content-Type", "application/json")
      .body(getAverageByDateRequest)
      .post("/v1/metric-posts/average")      
      .then()
      .statusCode(Constants.HTTP_400_CODE)
      .body("result.code", equalTo(Constants.INVALID_FIELD_FORMAT_RESPONSE_ERROR_CODE));
      
  }

  @Test
  public void getAverageByDateHttp500Test() {
    Average avg = new Average();
    avg.setMetricType("Temperature");
    avg.setDate("2022-08-17 00:00:00.000");
    getAverageByDateRequest = new GetAverageByDateRequest(avg);
    when(metricPostService.getMetricAverageByDatetime(avg.getMetricType(), avg.getDate())).thenThrow(RuntimeException.class);

    given()
      .when()
      .header("Content-Type", "application/json")
      .body(getAverageByDateRequest)
      .post("/v1/metric-posts/average")      
      .then()
      .statusCode(Constants.HTTP_500_CODE)
      .body("result.code", equalTo(Constants.EXCEPTION_RESPONSE_ERROR_CODE));

  }
  
}
