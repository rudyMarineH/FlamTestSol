package org.taf.web.config.base;

//that class used for current DemoQa tests
public abstract class BaseDemoQaTest extends BaseWebTest {

    @Override
    protected String startUrl() { return webUrls.baseUrl(); }
}
