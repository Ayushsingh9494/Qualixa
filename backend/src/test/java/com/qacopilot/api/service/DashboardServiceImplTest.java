package com.qacopilot.api.service;

import com.qacopilot.api.dto.DashboardDTO;
import com.qacopilot.api.entity.TestCase;
import com.qacopilot.api.entity.TestExecution;
import com.qacopilot.api.entity.User;
import com.qacopilot.api.repository.TestCaseRepository;
import com.qacopilot.api.repository.TestExecutionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceImplTest {

    @Mock
    private TestCaseRepository testCaseRepository;

    @Mock
    private TestExecutionRepository testExecutionRepository;

    private DashboardServiceImpl dashboardService;

    @BeforeEach
    public void setUp() {
        dashboardService = new DashboardServiceImpl(testCaseRepository, testExecutionRepository);
    }

    @Test
    public void testGetDashboardStatistics() {
        User mockUser = User.builder().id(1L).username("testuser").email("test@example.com").build();
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(mockUser);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        TestCase tc1 = TestCase.builder().id(1L).testCaseId("TC-001").title("Login").build();
        TestCase tc2 = TestCase.builder().id(2L).testCaseId("TC-002").title("Logout").build();
        TestCase tc3 = TestCase.builder().id(3L).testCaseId("TC-003").title("Search").build();

        Mockito.when(testCaseRepository.countByRequirementUserId(1L)).thenReturn(3L);
        Mockito.when(testCaseRepository.findByRequirementUserId(1L)).thenReturn(List.of(tc1, tc2, tc3));

        TestExecution run1 = TestExecution.builder().id(10L).testCase(tc1).status("PASSED").executedAt(LocalDateTime.now()).build();
        TestExecution run2 = TestExecution.builder().id(11L).testCase(tc2).status("FAILED").executedAt(LocalDateTime.now()).build();

        Mockito.when(testExecutionRepository.findByTestCaseIdOrderByExecutedAtDesc(1L)).thenReturn(List.of(run1));
        Mockito.when(testExecutionRepository.findByTestCaseIdOrderByExecutedAtDesc(2L)).thenReturn(List.of(run2));
        Mockito.when(testExecutionRepository.findByTestCaseIdOrderByExecutedAtDesc(3L)).thenReturn(Collections.emptyList());

        Mockito.when(testExecutionRepository.findByTestCaseRequirementUserId(Mockito.eq(1L), Mockito.any(Pageable.class)))
                .thenReturn(List.of(run1, run2));

        DashboardDTO result = dashboardService.getDashboardStatistics();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(3L, result.totalTestCases());
        Assertions.assertEquals(1L, result.passedTestCases()); // tc1 is passed
        Assertions.assertEquals(1L, result.failedTestCases()); // tc2 is failed
        Assertions.assertEquals(1L, result.untestedTestCases()); // tc3 is untested
        Assertions.assertEquals(2, result.recentExecutions().size());
    }
}
