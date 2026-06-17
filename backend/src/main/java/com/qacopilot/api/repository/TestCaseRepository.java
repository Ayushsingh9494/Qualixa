package com.qacopilot.api.repository;

import com.qacopilot.api.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    List<TestCase> findByRequirementId(Long requirementId);
    long countByRequirementUserId(Long userId);
    List<TestCase> findByRequirementUserId(Long userId);
    Optional<TestCase> findByIdAndRequirementUserId(Long id, Long userId);
}
