package org.taf.web.config.base;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.taf.web.config.BrowserOptions;
import org.taf.web.config.WebTestConfig;
import org.taf.web.config.WebUrls;
import org.taf.web.hooks.ScreenshotWatcher;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest(classes = WebTestConfig.class)
@UsePlaywright(BrowserOptions.class)
@ExtendWith(ScreenshotWatcher.class)
@ConfigurationPropertiesScan("org.taf.web")
public abstract class BaseWebTest {

    @Autowired
    protected WebUrls webUrls;

    protected static final Map<Class<?>, Long> CLASS_TO_THREAD = new ConcurrentHashMap<>();
    public static final ThreadLocal<Page> CURRENT_PAGE = new ThreadLocal<>();

    protected abstract String startUrl();

//    I have added System.out.printf to show parallelism ability
    @BeforeEach
    void setUp(TestInfo testInfo, Page page) {
        CURRENT_PAGE.set(page);
        CLASS_TO_THREAD.putIfAbsent(getClass(), Thread.currentThread().threadId());
        page.navigate(startUrl());
        System.out.printf("[parallel] %s#%s → thread=%s browser=%s%n",
                getClass().getSimpleName(),
                testInfo.getDisplayName(),
                Thread.currentThread().getName(),
                System.getProperty("browser", "chromium"));
    }

    @AfterEach
    void clearPage() {
        CURRENT_PAGE.remove();
    }
}
