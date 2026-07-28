package org.taf.web.config.base;
//this was added as part of architecture that shows easy extend with different pages. this class do nothing
public abstract class BaseAdminTest extends BaseWebTest {

  @Override
  protected String startUrl() {
    return webUrls.REDACTEDUrl();
  }
}
