package si.fri.rso.smartarticle.institutions.api.v1.health;

import si.fri.rso.smartarticle.institutions.services.configuration.AppProperties;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@Health
@ApplicationScoped
public class InstitutionServiceHealthCheck implements HealthCheck{
    @Inject
    private AppProperties appProperties;

    public HealthCheckResponse call() {
        if (appProperties.isHealthy()) {
            return  HealthCheckResponse.named(InstitutionServiceHealthCheck.class.getSimpleName()).up().build();
        } else {
            return  HealthCheckResponse.named(InstitutionServiceHealthCheck.class.getSimpleName()).down().build();
        }
    }
