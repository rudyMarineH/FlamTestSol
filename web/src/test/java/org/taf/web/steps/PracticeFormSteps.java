package org.taf.web.steps;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.taf.web.pages.PracticeFormPage;

import java.nio.file.Paths;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PracticeFormSteps {

    private final PracticeFormPage form;
    private final Page page;

    public PracticeFormSteps(Page page) {
        this.form = new PracticeFormPage(page);
        this.page = page;
    }

    @Step("Fill all required and optional fields")
    public void fillAllFields() {
        form.fillFirstName("John")
                .fillLastName("Smith")
                .fillEmail("john.smith@example.com")
                .selectGender("Male")
                .fillMobile("1234567890")
                .setDateOfBirth("15", "June", "1990")
                .addSubject("Maths")
                .selectHobby("Sports")
                .uploadPicture(Paths.get("src/test/resources/test-upload.png"))
                .fillCurrentAddress("123 Main St")
                .selectState("NCR")
                .selectCity("Delhi");
    }

    @Step("Fill required fields only")
    public void fillRequiredFieldsOnly() {
        form.fillFirstName("Jane")
                .fillLastName("Doe")
                .selectGender("Female")
                .fillMobile("0987654321");
    }

    @Step("Fill required fields with invalid mobile (5 digits)")
    public void fillWithInvalidMobile() {
        form.fillFirstName("John")
                .fillLastName("Smith")
                .selectGender("Male")
                .fillMobile("12345");
    }

    @Step("Fill and submit form with valid required fields")
    public void fillAndSubmitValidForm() {
        form.fillFirstName("John")
                .fillLastName("Smith")
                .selectGender("Male")
                .fillMobile("1234567890")
                .submit();
    }

    @Step("Select state '{state}' and city '{city}'")
    public void selectStateAndCity(String state, String city) {
        form.selectState(state).selectCity(city);
    }

    @Step("Submit the form")
    public void submit() {
        form.submit();
    }

    @Step("Verify success modal with title: {expectedTitle}")
    public void verifySuccessModal(String expectedTitle) {
        assertThat(form.isSuccessModalVisible()).isTrue();
        assertThat(form.getModalTitle()).isEqualTo(expectedTitle);
    }

    @Step("Verify success modal is visible")
    public void verifySuccessModalVisible() {
        assertThat(form.isSuccessModalVisible()).isTrue();
    }

    @Step("Verify submitted data — name: {name}, mobile: {mobile}")
    public void verifySubmittedData(String name, String mobile) {
        Map<String, String> values = form.getSubmittedValues();
        assertThat(values.get("Student Name")).isEqualTo(name);
        assertThat(values.get("Mobile")).isEqualTo(mobile);
    }

    @Step("Verify state '{state}' and city '{city}' are displayed")
    public void verifyStateAndCity(String state, String city) {
        assertThat(page.locator("#state").textContent()).contains(state);
        assertThat(page.locator("#city").textContent()).contains(city);
    }

    @Step("Verify no success modal appears")
    public void verifyNoModal() {
        assertThat(form.isSuccessModalVisible()).isFalse();
    }

    @Step("Verify field '{fieldId}' is marked invalid")
    public void verifyFieldInvalid(String fieldId) {
        assertThat(form.isFieldInvalid(fieldId)).isTrue();
    }

    @Step("Assert student name is 'Wrong Name' (intentional failure)")
    public void assertWrongStudentName() {
        Map<String, String> values = form.getSubmittedValues();
        assertThat(values.get("Student Name")).isEqualTo("Wrong Name");
    }
}
