package org.taf.web.specs.demoQA;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.taf.tags.Tags;
import org.taf.web.config.base.BaseDemoQaTest;
import org.taf.web.steps.PracticeFormSteps;

class PracticeFormPositiveTest extends BaseDemoQaTest {

    @Test
    @Tag(Tags.POSITIVE) @Tag(Tags.SMOKE) @Tag(Tags.E2E)
    void submit_form_with_all_fields_filled(Page page) {
        PracticeFormSteps steps = new PracticeFormSteps(page);
        steps.fillAllFields();
        steps.submit();
        steps.verifySuccessModal("Thanks for submitting the form");
        steps.verifySubmittedData("John Smith", "1234567890");
    }

    @Test
    @Tag(Tags.POSITIVE) @Tag(Tags.SMOKE)
    void submit_form_with_only_required_fields(Page page) {
        PracticeFormSteps steps = new PracticeFormSteps(page);
        steps.fillRequiredFieldsOnly();
        steps.submit();
        steps.verifySuccessModal("Thanks for submitting the form");
    }

    @Test
    @Tag(Tags.POSITIVE) @Tag(Tags.SMOKE)
    void state_city_dropdowns_are_dependent(Page page) {
        PracticeFormSteps steps = new PracticeFormSteps(page);
        steps.selectStateAndCity("NCR", "Delhi");
        steps.verifyStateAndCity("NCR", "Delhi");
    }
}
