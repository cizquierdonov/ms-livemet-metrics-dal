package cl.cizquierdonov.metrics.dal.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents entity class of the database table 'metric_post' and implements its CRUD operations.
 * @author cizquierdo
 */
@Entity
@Table(name = "metric_post")
@Getter
@Setter
@NamedQueries({
  @NamedQuery(name = "MetricPost.getMetricPostsByType", query = "from MetricPost where type = ?1 order by recordDate desc"),
  @NamedQuery(name = "MetricPost.getAverageByDateRange", query = "from MetricPost where type = ?1 and recordDate >= ?2 and recordDate <= ?3")
})
public class MetricPost extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "metric_post_id")
  private Integer metricPostId;

  @Column(name = "type")
  private String type;

  @Column(name = "value")
  private Double value;

  @Column(name = "review")
  private String review;

  @Column(name = "record_date")
  private Timestamp recordDate;

  /**
   * Implements query to get all metric posts for a specific type.
   * @param type  Identifier of the {@link MetricType} to filter.
   * @return  List of {@link MetricPost} of kind 'type'.
   */
  public static List<MetricPost> getMetricPostsByType(String type) {
    return find("#MetricPost.getMetricPostsByType", type).list();
  }

  /**
   * Implements query to get metric posts list between a date range.
   * @param from  Date lower limit (inclusive).
   * @param to    Date greater limit (exclusive).
   * @return  List of {@link MetricPost} inside this range.
   */
  public static List<MetricPost> getAverageByDateRange(String type, Timestamp from, Timestamp to) {
    return find("#MetricPost.getAverageByDateRange", type, from, to).list();
  }
  
}