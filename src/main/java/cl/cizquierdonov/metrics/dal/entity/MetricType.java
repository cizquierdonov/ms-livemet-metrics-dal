package cl.cizquierdonov.metrics.dal.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents entity class of the database table 'metric_type' and implements its CRUD operations.
 * @author cizquierdo
 */
@Entity
@Table(name = "metric_type")
@Getter
@Setter
@NamedQueries({
  @NamedQuery(name = "MetricType.getActiveMetricTypes", query = "from MetricType where active = ?1")
})
public class MetricType extends PanacheEntityBase {

  @Id
  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "active")
  private boolean active;

  @Column(name = "suffix")
  private String suffix;

  @Column(name = "scale")
  private boolean scale;

  /**
   * Implements query to get all active metric type.
   * @return  List of {@link MetricType} with active state = true.
   */
  public static List<MetricType> getActiveMetrics(){
    return find("#MetricType.getActiveMetricTypes", true).list();
  }
  
}
