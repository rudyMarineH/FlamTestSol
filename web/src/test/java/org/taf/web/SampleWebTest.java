package org.taf.web;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SampleWebTest extends BaseWebTest {

    @Test
    void playwright_browser_and_page_initialise() {
        page.navigate("about:blank");
        assertThat(page.url()).isEqualTo("about:blank");
    }
}
