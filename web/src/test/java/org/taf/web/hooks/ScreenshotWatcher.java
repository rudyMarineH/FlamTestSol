package org.taf.web.hooks;

import com.microsoft.playwright.Page;
import org.taf.web.config.base.BaseWebTest;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.io.ByteArrayInputStream;

public class ScreenshotWatcher implements TestExecutionExceptionHandler {
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        Page page = BaseWebTest.CURRENT_PAGE.get();
        if (page != null) {
            Allure.addAttachment(context.getDisplayName(), "image/png",
                    new ByteArrayInputStream(page.screenshot()), ".png");
        }
        throw throwable;
    }
}
