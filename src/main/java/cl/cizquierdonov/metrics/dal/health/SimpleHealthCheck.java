package cl.cizquierdonov.metrics.dal.health;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

/**
 * Quarkus class that implements a simple service health check.
 * @author cizquierdo
 */
@Liveness
@ApplicationScoped  
public class SimpleHealthCheck implements HealthCheck {

  @Override
  public HealthCheckResponse call() {
    return HealthCheckResponse.up("Simple health check");
  }
}
