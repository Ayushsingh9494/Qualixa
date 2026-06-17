package com.qacopilot.api.repository;

import com.qacopilot.api.entity.SeleniumScript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeleniumScriptRepository extends JpaRepository<SeleniumScript, Long> {
    Optional<SeleniumScript> findByTestCaseId(Long testCaseId);
}
