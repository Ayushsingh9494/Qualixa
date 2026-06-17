package com.qacopilot.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qacopilot.api.dto.TestCaseDTO;
import com.qacopilot.api.entity.Requirement;
import com.qacopilot.api.repository.RequirementRepository;
import com.qacopilot.api.repository.TestCaseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TestCaseServiceImplTest {

    @Mock
    private TestCaseRepository testCaseRepository;

    @Mock
    private RequirementRepository requirementRepository;

    @Mock
    private GeminiService geminiService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private TestCaseServiceImpl testCaseService;

    @BeforeEach
    public void setUp() {
        testCaseService = new TestCaseServiceImpl(
                testCaseRepository,
                requirementRepository,
                geminiService,
                objectMapper
        );
    }

    @Test
    public void testGenerateTestCases() {
        Long requirementId = 1L;
        Requirement requirement = Requirement.builder()
                .id(requirementId)
                .title("Authentication")
                .description("Login system")
                .build();

        Mockito.when(requirementRepository.findById(requirementId)).thenReturn(Optional.of(requirement));

        String mockGeminiResponse = """
                {
                  "testCases": [
                    {
                      "testCaseId": "TC-001",
                      "testName": "Valid login",
                      "preconditions": "User is on homepage",
                      "steps": ["Click Login", "Enter credentials", "Submit"],
                      "expectedResult": "User is logged in"
                    }
                  ]
                }
                """;

        Mockito.when(geminiService.generateContent(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(mockGeminiResponse);

        Mockito.when(testCaseRepository.findByRequirementId(requirementId)).thenReturn(Collections.emptyList());

        Mockito.when(testCaseRepository.saveAll(Mockito.anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        List<TestCaseDTO> results = testCaseService.generateTestCases(requirementId);

        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
        TestCaseDTO tc = results.getFirst();
        Assertions.assertEquals("TC-001", tc.testCaseId());
        Assertions.assertEquals("Valid login", tc.title());
        Assertions.assertEquals("User is on homepage", tc.preconditions());
        Assertions.assertEquals(3, tc.steps().size());
        Assertions.assertEquals("User is logged in", tc.expectedResult());
    }
}
