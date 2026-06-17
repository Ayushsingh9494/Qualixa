package com.qacopilot.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qacopilot.api.dto.SeleniumScriptDTO;
import com.qacopilot.api.entity.SeleniumScript;
import com.qacopilot.api.entity.TestCase;
import com.qacopilot.api.repository.SeleniumScriptRepository;
import com.qacopilot.api.entity.User;
import com.qacopilot.api.repository.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SeleniumScriptServiceImpl implements SeleniumScriptService {

    private final SeleniumScriptRepository seleniumScriptRepository;
    private final TestCaseRepository testCaseRepository;
    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    @Autowired
    public SeleniumScriptServiceImpl(
            SeleniumScriptRepository seleniumScriptRepository,
            TestCaseRepository testCaseRepository,
            GeminiService geminiService,
            ObjectMapper objectMapper
    ) {
        this.seleniumScriptRepository = seleniumScriptRepository;
        this.testCaseRepository = testCaseRepository;
        this.geminiService = geminiService;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public SeleniumScriptDTO generateScript(Long testCaseId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TestCase testCase = testCaseRepository.findByIdAndRequirementUserId(testCaseId, currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Test Case not found with id: " + testCaseId));

        String systemInstruction = """
                You are an expert QA Automation Engineer. Generate complete, runnable Selenium WebDriver Java code using TestNG annotations based on the provided test case description.
                You must return a JSON object with a single key 'scriptCode' whose value is a String containing the Java class code.
                Do not wrap the code in markdown formatting (like ```java ... ```) inside the JSON string value; it must be a raw Java class text inside the JSON string.
                The class should include:
                - Proper imports (org.openqa.selenium.*, org.testng.annotations.*, org.testng.Assert).
                - WebDriver initialization in a @BeforeMethod setup block (e.g. ChromeDriver).
                - A @Test method translating the steps and expected result. The method signature MUST declare 'throws Exception'.
                - WebDriver teardown in an @AfterMethod block.
                - Proper assertions using TestNG Assert class to verify expectations.
                - Best practices like implicit/explicit waits (WebDriverWait).
                - IMPORTANT: Use http://localhost:8080 as the base URL for all navigations (e.g., http://localhost:8080/login, http://localhost:8080/forgot-password, http://localhost:8080/dashboard).
                - IMPORTANT: Use standard element locators: email field by By.id("email"), password field by By.id("password"), login button by By.id("loginButton"), email error message by By.id("email-error"), forgot-password submit button by By.id("submitBtn"), and forgot-password success message by By.id("successMessage").
                - IMPORTANT: To allow the user to notice what is happening in the browser, insert a Thread.sleep(1500); pause after every navigation, typing (sendKeys), or clicking action.
                """;

        String prompt = String.format("Test Case ID: %s\nTest Case Title: %s\nPreconditions: %s\nSteps: %s\nExpected Result: %s",
                testCase.getTestCaseId(), testCase.getTitle(), testCase.getPreconditions(), testCase.getSteps(), testCase.getExpectedResult());

        String jsonResponse = geminiService.generateContent(systemInstruction, prompt);

        try {
            GeminiScriptWrapper wrapper = objectMapper.readValue(jsonResponse, GeminiScriptWrapper.class);

            Optional<SeleniumScript> existing = seleniumScriptRepository.findByTestCaseId(testCaseId);
            existing.ifPresent(seleniumScriptRepository::delete);

            String code = wrapper.scriptCode() != null ? wrapper.scriptCode() : "";

            SeleniumScript script = SeleniumScript.builder()
                    .testCase(testCase)
                    .scriptCode(code)
                    .build();

            SeleniumScript saved = seleniumScriptRepository.save(script);
            return convertToDTO(saved);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate and parse Selenium script: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SeleniumScriptDTO getScriptByTestCase(Long testCaseId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Verify user owns the test case
        testCaseRepository.findByIdAndRequirementUserId(testCaseId, currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Test Case not found with id: " + testCaseId));

        SeleniumScript script = seleniumScriptRepository.findByTestCaseId(testCaseId)
                .orElseThrow(() -> new IllegalArgumentException("Selenium Script not found for test case: " + testCaseId));
        return convertToDTO(script);
    }

    private SeleniumScriptDTO convertToDTO(SeleniumScript script) {
        return SeleniumScriptDTO.builder()
                .id(script.getId())
                .testCaseId(script.getTestCase().getId())
                .scriptCode(script.getScriptCode())
                .createdAt(script.getCreatedAt())
                .build();
    }

    private record GeminiScriptWrapper(String scriptCode) {}
}
