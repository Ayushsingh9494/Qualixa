package com.qacopilot.api.service;

import com.qacopilot.api.dto.TestCaseDTO;
import java.util.List;

public interface TestCaseService {
    List<TestCaseDTO> generateTestCases(Long requirementId);
    List<TestCaseDTO> getTestCasesByRequirement(Long requirementId);
}
