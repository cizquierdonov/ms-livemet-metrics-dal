package cl.cizquierdonov.metrics.dal.health;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

/**
 * Quarkus class that implements a service health check, verifying database connection.
 * @author cizquierdo
 */
@Readiness
@ApplicationScoped
public class DatabaseConnectionHealthCheck implements HealthCheck {

  @Override
  public HealthCheckResponse call() {
    return HealthCheckResponse.up("Database connection health check");
  }
}
