package com.qacopilot.api.repository;

import com.qacopilot.api.entity.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {
    List<Requirement> findByUserId(Long userId);
    Optional<Requirement> findByIdAndUserId(Long id, Long userId);
}
