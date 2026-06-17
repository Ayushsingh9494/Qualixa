package com.qacopilot.api.service;

import com.qacopilot.api.dto.TestExecutionDTO;
import java.util.List;

public interface TestExecutionService {
    TestExecutionDTO executeTest(Long testCaseId);
    List<TestExecutionDTO> getExecutionsByTestCase(Long testCaseId);
}
