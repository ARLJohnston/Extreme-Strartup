package uk.ac.gla.spre.warmup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

public class ExtremeStartupHealthIndicator implements HealthIndicator {
    @Autowired
    private ExtremeStartupClient extremeStartupClient;

    @Value("${instance}")
    private String instanceNumber;

    @Value("${spre.delegate.port}")
    private String maybeDelegationPort;

    @Override
    public Health health() {
        Health.Builder status = Health.up();
        if (!extremeStartupClient.registrationStillCurrent()) {
            status = Health.down();
        }
        return status
                .withDetail( "instance", instanceNumber )
                .withDetail( "delegate", maybeDelegationPort)
                .withDetail( "pid", ProcessHandle.current().pid())
                .build();
    }
}