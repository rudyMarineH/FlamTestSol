package org.taf.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RetryProperties {

    @Value("${retry.enabled:true}")
    private boolean enabled;

    @PostConstruct
    void apply() {
        Retry.configure(enabled);
    }
}
