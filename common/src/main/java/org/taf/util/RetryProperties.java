package org.taf.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
//class for managing retry properties and set variable "enabled" from Retry class as it @UtilityClass
@Component
public class RetryProperties {

    @Value("${retry.enabled:true}")
    private boolean enabled;

    @PostConstruct
    void apply() {
        Retry.configure(enabled);
    }
}
