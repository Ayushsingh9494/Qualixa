package com.qacopilot.api.repository;

import com.qacopilot.api.entity.TestExecution;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestExecutionRepository extends JpaRepository<TestExecution, Long> {
    List<TestExecution> findByTestCaseIdOrderByExecutedAtDesc(Long testCaseId);
    List<TestExecution> findByTestCaseRequirementUserId(Long userId, Pageable pageable);
}
