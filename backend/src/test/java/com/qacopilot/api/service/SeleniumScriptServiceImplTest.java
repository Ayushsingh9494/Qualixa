package com.qacopilot.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qacopilot.api.dto.SeleniumScriptDTO;
import com.qacopilot.api.entity.TestCase;
import com.qacopilot.api.repository.SeleniumScriptRepository;
import com.qacopilot.api.repository.TestCaseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SeleniumScriptServiceImplTest {

    @Mock
    private SeleniumScriptRepository seleniumScriptRepository;

    @Mock
    private TestCaseRepository testCaseRepository;

    @Mock
    private GeminiService geminiService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private SeleniumScriptServiceImpl seleniumScriptService;

    @BeforeEach
    public void setUp() {
        seleniumScriptService = new SeleniumScriptServiceImpl(
                seleniumScriptRepository,
                testCaseRepository,
                geminiService,
                objectMapper
        );
    }

    @Test
    public void testGenerateScript() {
        Long testCaseId = 1L;
        TestCase testCase = TestCase.builder()
                .id(testCaseId)
                .testCaseId("TC-001")
                .title("Valid Login")
                .preconditions("On page")
                .steps("[\"Enter credentials\"]")
                .expectedResult("Logged in")
                .build();

        Mockito.when(testCaseRepository.findById(testCaseId)).thenReturn(Optional.of(testCase));

        String mockGeminiResponse = """
                {
                  "scriptCode": "public class TC001 { // Selenium code }"
                }
                """;

        Mockito.when(geminiService.generateContent(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(mockGeminiResponse);

        Mockito.when(seleniumScriptRepository.findByTestCaseId(testCaseId)).thenReturn(Optional.empty());

        Mockito.when(seleniumScriptRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

        SeleniumScriptDTO result = seleniumScriptService.generateScript(testCaseId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(testCaseId, result.testCaseId());
        Assertions.assertEquals("public class TC001 { // Selenium code }", result.scriptCode());
    }
}
