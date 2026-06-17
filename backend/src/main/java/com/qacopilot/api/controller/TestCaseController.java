package com.qacopilot.api.controller;

import com.qacopilot.api.dto.TestCaseDTO;
import com.qacopilot.api.service.TestCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requirements/{requirementId}")
public class TestCaseController {

    private final TestCaseService testCaseService;

    @Autowired
    public TestCaseController(TestCaseService testCaseService) {
        this.testCaseService = testCaseService;
    }

    @PostMapping("/generate-test-cases")
    public ResponseEntity<List<TestCaseDTO>> generateTestCases(@PathVariable Long requirementId) {
        try {
            List<TestCaseDTO> generated = testCaseService.generateTestCases(requirementId);
            return new ResponseEntity<>(generated, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/test-cases")
    public ResponseEntity<List<TestCaseDTO>> getTestCasesByRequirement(@PathVariable Long requirementId) {
        List<TestCaseDTO> testCases = testCaseService.getTestCasesByRequirement(requirementId);
        return ResponseEntity.ok(testCases);
    }
}
