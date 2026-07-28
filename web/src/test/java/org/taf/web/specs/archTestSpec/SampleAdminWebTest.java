package org.taf.web.specs.archTestSpec;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Test;
import org.taf.web.config.base.BaseAdminTest;

import static org.assertj.core.api.Assertions.assertThat;
//dummy test that demonstrate ability to add any page to tests
class SampleAdminWebTest extends BaseAdminTest {

    @Test
    void navigates_to_REDACTED_url(Page page) {
        assertThat(page.url()).contains("login");
    }

    @Test
    void REDACTED_url_configured(Page page) {
        System.out.println(webUrls.REDACTEDUrl());
        assertThat(page.url()).contains("login");
    }
}
