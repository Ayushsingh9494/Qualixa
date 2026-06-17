package com.qacopilot.api.service;

import com.qacopilot.api.dto.DashboardDTO;
import com.qacopilot.api.dto.TestExecutionDTO;
import com.qacopilot.api.entity.TestCase;
import com.qacopilot.api.entity.TestExecution;
import com.qacopilot.api.entity.User;
import com.qacopilot.api.repository.TestCaseRepository;
import com.qacopilot.api.repository.TestExecutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final TestCaseRepository testCaseRepository;
    private final TestExecutionRepository testExecutionRepository;

    @Autowired
    public DashboardServiceImpl(
            TestCaseRepository testCaseRepository,
            TestExecutionRepository testExecutionRepository
    ) {
        this.testCaseRepository = testCaseRepository;
        this.testExecutionRepository = testExecutionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardDTO getDashboardStatistics() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = currentUser.getId();

        long totalTestCases = testCaseRepository.countByRequirementUserId(userId);

        List<TestCase> allTestCases = testCaseRepository.findByRequirementUserId(userId);
        long passed = 0;
        long failed = 0;
        long untested = 0;

        for (TestCase tc : allTestCases) {
            List<TestExecution> executions = testExecutionRepository.findByTestCaseIdOrderByExecutedAtDesc(tc.getId());
            if (executions.isEmpty()) {
                untested++;
            } else {
                TestExecution latest = executions.getFirst();
                if ("PASSED".equals(latest.getStatus())) {
                    passed++;
                } else {
                    failed++;
                }
            }
        }

        // Fetch top 5 recent executions for this user sorted by executedAt descending
        Pageable topFive = PageRequest.of(0, 5, Sort.by("executedAt").descending());
        List<TestExecution> recent = testExecutionRepository.findByTestCaseRequirementUserId(userId, topFive);

        List<TestExecutionDTO> recentDTOs = recent.stream()
                .map(this::convertToExecutionDTO)
                .collect(Collectors.toList());

        return DashboardDTO.builder()
                .totalTestCases(totalTestCases)
                .passedTestCases(passed)
                .failedTestCases(failed)
                .untestedTestCases(untested)
                .recentExecutions(recentDTOs)
                .build();
    }

    private TestExecutionDTO convertToExecutionDTO(TestExecution execution) {
        return TestExecutionDTO.builder()
                .id(execution.getId())
                .testCaseId(execution.getTestCase().getId())
                .status(execution.getStatus())
                .executionTimeMs(execution.getExecutionTimeMs())
                .errorMessage(execution.getErrorMessage())
                .screenshotBase64(execution.getScreenshotBase64())
                .executedAt(execution.getExecutedAt())
                .build();
    }
}
