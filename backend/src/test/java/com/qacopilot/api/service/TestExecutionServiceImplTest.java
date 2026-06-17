package com.qacopilot.api.service;

import com.qacopilot.api.dto.TestExecutionDTO;
import com.qacopilot.api.entity.SeleniumScript;
import com.qacopilot.api.entity.TestCase;
import com.qacopilot.api.entity.TestExecution;
import com.qacopilot.api.repository.SeleniumScriptRepository;
import com.qacopilot.api.repository.TestCaseRepository;
import com.qacopilot.api.repository.TestExecutionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TestExecutionServiceImplTest {

    @Mock
    private TestExecutionRepository testExecutionRepository;

    @Mock
    private TestCaseRepository testCaseRepository;

    @Mock
    private SeleniumScriptRepository seleniumScriptRepository;

    private TestExecutionServiceImpl testExecutionService;

    @BeforeEach
    public void setUp() {
        testExecutionService = new TestExecutionServiceImpl(
                testExecutionRepository,
                testCaseRepository,
                seleniumScriptRepository
        );
    }

    @Test
    public void testExecuteTest() {
        Long testCaseId = 1L;
        TestCase testCase = TestCase.builder()
                .id(testCaseId)
                .testCaseId("TC-001")
                .title("Valid Login")
                .preconditions("On home page")
                .build();

        SeleniumScript script = SeleniumScript.builder()
                .id(2L)
                .testCase(testCase)
                .scriptCode("public class GeneratedTest {}")
                .build();

        Mockito.when(testCaseRepository.findById(testCaseId)).thenReturn(Optional.of(testCase));
        Mockito.when(seleniumScriptRepository.findByTestCaseId(testCaseId)).thenReturn(Optional.of(script));

        Mockito.when(testExecutionRepository.save(Mockito.any())).thenAnswer(invocation -> {
            TestExecution exec = invocation.getArgument(0);
            exec.setId(10L); // set mock ID
            return exec;
        });

        TestExecutionDTO result = testExecutionService.executeTest(testCaseId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(10L, result.id());
        Assertions.assertEquals(testCaseId, result.testCaseId());
        Assertions.assertEquals("PASSED", result.status());
        Assertions.assertTrue(result.executionTimeMs() > 0);
        Assertions.assertNull(result.errorMessage());
        Assertions.assertTrue(result.screenshotBase64().startsWith("data:image/svg+xml;base64,"));
    }
}
