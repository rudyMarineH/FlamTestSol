package org.taf.web.specs.archTestSpec;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Test;
import org.taf.web.config.base.BaseAdminTest;

import static org.assertj.core.api.Assertions.assertThat;
//dummy test that demonstrate ability to add any page to tests
class SampleAdminWebTest extends BaseAdminTest {

    @Test
    void navigates_to_dummy_url(Page page) {
        assertThat(page.url()).contains("login");
    }

    @Test
    void dummy_url_configured(Page page) {
        System.out.println(webUrls.dummyUrl());
        assertThat(page.url()).contains("login");
    }
}
