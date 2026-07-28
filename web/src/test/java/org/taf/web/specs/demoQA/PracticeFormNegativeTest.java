package org.taf.web.specs.demoQA;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.taf.tags.Tags;
import org.taf.web.config.base.BaseDemoQaTest;
import org.taf.web.steps.PracticeFormSteps;

class PracticeFormNegativeTest extends BaseDemoQaTest {

    @Test
    @Tag(Tags.NEGATIVE) @Tag(Tags.SMOKE)
    void submit_empty_form_shows_validation(Page page) {
        PracticeFormSteps steps = new PracticeFormSteps(page);
        steps.submit();
        steps.verifyNoModal();
        steps.verifyFieldInvalid("firstName");
    }

    @Test
    @Tag(Tags.NEGATIVE) @Tag(Tags.SMOKE)
    void submit_with_invalid_mobile_shows_validation(Page page) {
        PracticeFormSteps steps = new PracticeFormSteps(page);
        steps.fillWithInvalidMobile();
        steps.submit();
        steps.verifyNoModal();
    }

    @Test
    @Tag(Tags.DEBUG)
    void allure_screenshot_on_failure(Page page) {
        // intentional failure after form submit — screenshot captures the success modal
        PracticeFormSteps steps = new PracticeFormSteps(page);
        steps.fillAndSubmitValidForm();
        steps.verifySuccessModalVisible();
        steps.assertWrongStudentName();
    }
}
