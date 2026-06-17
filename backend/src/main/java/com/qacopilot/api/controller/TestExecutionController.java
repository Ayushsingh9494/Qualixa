package com.qacopilot.api.controller;

import com.qacopilot.api.dto.TestExecutionDTO;
import com.qacopilot.api.service.TestExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-cases/{testCaseId}")
public class TestExecutionController {

    private final TestExecutionService testExecutionService;

    @Autowired
    public TestExecutionController(TestExecutionService testExecutionService) {
        this.testExecutionService = testExecutionService;
    }

    @PostMapping("/execute")
    public ResponseEntity<TestExecutionDTO> executeTest(@PathVariable Long testCaseId) {
        try {
            TestExecutionDTO execution = testExecutionService.executeTest(testCaseId);
            return new ResponseEntity<>(execution, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/executions")
    public ResponseEntity<List<TestExecutionDTO>> getExecutionsByTestCase(@PathVariable Long testCaseId) {
        List<TestExecutionDTO> executions = testExecutionService.getExecutionsByTestCase(testCaseId);
        return ResponseEntity.ok(executions);
    }
}
