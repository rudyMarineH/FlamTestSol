package org.taf.web.config;

import org.springframework.stereotype.Component;

@Component
public class BrowserConfig {

    public BrowserConfig(WebUrls webUrls) {
        BrowserOptions.headless = webUrls.headless();
    }
}
