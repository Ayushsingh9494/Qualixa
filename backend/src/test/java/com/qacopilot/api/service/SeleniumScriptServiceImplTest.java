package com.qacopilot.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qacopilot.api.dto.SeleniumScriptDTO;
import com.qacopilot.api.entity.TestCase;
import com.qacopilot.api.entity.User;
import com.qacopilot.api.repository.SeleniumScriptRepository;
import com.qacopilot.api.repository.TestCaseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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
        User mockUser = User.builder().id(1L).username("testuser").email("test@example.com").build();
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(mockUser);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Long testCaseId = 1L;
        TestCase testCase = TestCase.builder()
                .id(testCaseId)
                .testCaseId("TC-001")
                .title("Valid Login")
                .preconditions("On page")
                .steps("[\"Enter credentials\"]")
                .expectedResult("Logged in")
                .build();

        Mockito.when(testCaseRepository.findByIdAndRequirementUserId(testCaseId, 1L)).thenReturn(Optional.of(testCase));

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
