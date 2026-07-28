package org.taf.web.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class PracticeFormPage {

    private final Page page;

    public PracticeFormPage(Page page) {
        this.page = page;
    }

    @Step("Fill first name: {value}")
    public PracticeFormPage fillFirstName(String value) {
        page.fill("#firstName", value);
        return this;
    }

    @Step("Fill last name: {value}")
    public PracticeFormPage fillLastName(String value) {
        page.fill("#lastName", value);
        return this;
    }

    @Step("Fill email: {value}")
    public PracticeFormPage fillEmail(String value) {
        page.fill("#userEmail", value);
        return this;
    }

    @Step("Fill mobile: {value}")
    public PracticeFormPage fillMobile(String value) {
        page.fill("#userNumber", value);
        return this;
    }

    @Step("Fill current address")
    public PracticeFormPage fillCurrentAddress(String value) {
        page.fill("#currentAddress", value);
        return this;
    }

    @Step("Select gender: {label}")
    public PracticeFormPage selectGender(String label) {
        page.locator("label[for^='gender-radio']")
                .and(page.getByText(label, new Page.GetByTextOptions().setExact(true)))
                .click();
        return this;
    }

    @Step("Set date of birth: {day} {month} {year}")
    public PracticeFormPage setDateOfBirth(String day, String month, String year) {
        page.locator("#dateOfBirthInput").click();
        page.locator(".react-datepicker").waitFor(new Locator.WaitForOptions().setTimeout(3000));
        page.locator(".react-datepicker__month-select").selectOption(new SelectOption().setLabel(month));
        page.locator(".react-datepicker__year-select").selectOption(year);
        page.locator(".react-datepicker__day--" + String.format("%03d", Integer.parseInt(day))
                + ":not(.react-datepicker__day--outside-month)").click();
        return this;
    }

    @Step("Add subject: {subject}")
    public PracticeFormPage addSubject(String subject) {
        page.fill("#subjectsInput", subject);
        page.locator(".subjects-auto-complete__menu").waitFor(new Locator.WaitForOptions().setTimeout(3000));
        page.locator(".subjects-auto-complete__option").first().click();
        page.locator(".subjects-auto-complete__multi-value__label")
                .filter(new Locator.FilterOptions().setHasText(subject))
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(3000));
        return this;
    }

    @Step("Select hobby: {hobby}")
    public PracticeFormPage selectHobby(String hobby) {
        page.locator("label[for^='hobbies-checkbox']")
                .filter(new Locator.FilterOptions().setHasText(hobby))
                .click();
        return this;
    }

    @Step("Upload picture: {file}")
    public PracticeFormPage uploadPicture(Path file) {
        page.locator("#uploadPicture").setInputFiles(file);
        page.waitForFunction(
                "() => document.querySelector('#uploadPicture').files.length > 0",
                null, new Page.WaitForFunctionOptions().setTimeout(3000));
        return this;
    }

    @Step("Select state: {state}")
    public PracticeFormPage selectState(String state) {
        page.click("#state");
        page.locator("#state div[class*='menu']").waitFor(new Locator.WaitForOptions().setTimeout(3000));
        page.locator("div[class*='option']")
                .filter(new Locator.FilterOptions().setHasText(state))
                .first().click();
        page.locator("#state div[class*='menu']")
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(3000));
        return this;
    }

    @Step("Select city: {city}")
    public PracticeFormPage selectCity(String city) {
        page.click("#city");
        page.locator("#city div[class*='menu']").waitFor(new Locator.WaitForOptions().setTimeout(3000));
        page.locator("div[class*='option']")
                .filter(new Locator.FilterOptions().setHasText(city))
                .first().click();
        page.locator("#city div[class*='menu']")
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(3000));
        return this;
    }

    @Step("Submit the form")
    public PracticeFormPage submit() {
        page.locator("#submit").scrollIntoViewIfNeeded();
        page.click("#submit");
        return this;
    }

    @Step("Check success modal is visible")
    public boolean isSuccessModalVisible() {
        try {
            page.getByRole(AriaRole.DIALOG).waitFor(new Locator.WaitForOptions().setTimeout(3000));
            return true;
        } catch (com.microsoft.playwright.TimeoutError e) {
            return false;
        }
    }

    @Step("Get modal title")
    public String getModalTitle() {
        return page.locator(".modal-title").textContent().trim();
    }

    @Step("Get submitted values from modal")
    public Map<String, String> getSubmittedValues() {
        Map<String, String> result = new LinkedHashMap<>();
        page.locator("table tbody tr").all().forEach(row -> {
            var cells = row.locator("td").all();
            if (cells.size() == 2) {
                result.put(cells.get(0).textContent().trim(), cells.get(1).textContent().trim());
            }
        });
        return result;
    }

    @Step("Check field '{fieldId}' is invalid")
    public boolean isFieldInvalid(String fieldId) {
        return (Boolean) page.locator("#" + fieldId).evaluate("el => el.matches(':invalid')");
    }
}
