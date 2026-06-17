package com.qacopilot.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qacopilot.api.dto.TestCaseDTO;
import com.qacopilot.api.entity.Requirement;
import com.qacopilot.api.entity.TestCase;
import com.qacopilot.api.repository.RequirementRepository;
import com.qacopilot.api.repository.TestCaseRepository;
import com.qacopilot.api.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestCaseServiceImpl implements TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final RequirementRepository requirementRepository;
    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    @Autowired
    public TestCaseServiceImpl(
            TestCaseRepository testCaseRepository,
            RequirementRepository requirementRepository,
            GeminiService geminiService,
            ObjectMapper objectMapper
    ) {
        this.testCaseRepository = testCaseRepository;
        this.requirementRepository = requirementRepository;
        this.geminiService = geminiService;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public List<TestCaseDTO> generateTestCases(Long requirementId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Requirement requirement = requirementRepository.findByIdAndUserId(requirementId, currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Requirement not found with id: " + requirementId));

        String systemInstruction = """
                You are an expert QA Automation Engineer. Generate detailed test cases based on the provided software requirement.
                You must return a JSON object with a key 'testCases' that contains an array of test cases.
                Each testcase in the array MUST contain exactly these keys:
                - 'testCaseId' (String, e.g., 'TC-001')
                - 'testName' (String)
                - 'preconditions' (String)
                - 'steps' (Array of Strings)
                - 'expectedResult' (String)
                """;

        String prompt = String.format("Requirement Title: %s\nRequirement Description: %s",
                requirement.getTitle(), requirement.getDescription());

        String jsonResponse = geminiService.generateContent(systemInstruction, prompt);

        try {
            GeminiTestCaseWrapper wrapper = objectMapper.readValue(jsonResponse, GeminiTestCaseWrapper.class);

            // Delete existing test cases for this requirement
            List<TestCase> existing = testCaseRepository.findByRequirementId(requirementId);
            testCaseRepository.deleteAll(existing);

            if (wrapper.testCases() == null || wrapper.testCases().isEmpty()) {
                return Collections.emptyList();
            }

            List<TestCase> toSave = wrapper.testCases().stream()
                    .map(dto -> {
                        String stepsJson;
                        try {
                            stepsJson = objectMapper.writeValueAsString(dto.steps());
                        } catch (Exception e) {
                            stepsJson = "[]";
                        }
                        return TestCase.builder()
                                .requirement(requirement)
                                .testCaseId(dto.testCaseId())
                                .title(dto.testName())
                                .preconditions(dto.preconditions())
                                .steps(stepsJson)
                                .expectedResult(dto.expectedResult())
                                .build();
                    })
                    .collect(Collectors.toList());

            List<TestCase> saved = testCaseRepository.saveAll(toSave);
            return saved.stream().map(this::convertToDTO).collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate and parse test cases: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestCaseDTO> getTestCasesByRequirement(Long requirementId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Verify user owns the requirement
        requirementRepository.findByIdAndUserId(requirementId, currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Requirement not found with id: " + requirementId));

        List<TestCase> testCases = testCaseRepository.findByRequirementId(requirementId);
        return testCases.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private TestCaseDTO convertToDTO(TestCase testCase) {
        List<String> stepsList;
        try {
            stepsList = objectMapper.readValue(testCase.getSteps(), new TypeReference<List<String>>() {});
        } catch (Exception e) {
            stepsList = Collections.emptyList();
        }

        return TestCaseDTO.builder()
                .id(testCase.getId())
                .requirementId(testCase.getRequirement().getId())
                .testCaseId(testCase.getTestCaseId())
                .title(testCase.getTitle())
                .preconditions(testCase.getPreconditions())
                .steps(stepsList)
                .expectedResult(testCase.getExpectedResult())
                .createdAt(testCase.getCreatedAt())
                .build();
    }

    // Static wrapper records for JSON parsing
    private record GeminiTestCaseWrapper(List<TestCaseGenerateDTO> testCases) {}
    private record TestCaseGenerateDTO(String testCaseId, String testName, String preconditions, List<String> steps, String expectedResult) {}
}
