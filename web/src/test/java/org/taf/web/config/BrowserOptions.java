package org.taf.web.config;

import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;

public class BrowserOptions implements OptionsFactory {

    static boolean headless = System.getenv("CI") != null || "true".equals(System.getProperty("headless"));

    @Override
    public Options getOptions() {
        return new Options()
                .setBrowserName(System.getProperty("browser", "chromium"))
                .setHeadless(headless);
    }
}
