package com.qacopilot.api.controller;

import com.qacopilot.api.dto.SeleniumScriptDTO;
import com.qacopilot.api.service.SeleniumScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test-cases/{testCaseId}")
public class SeleniumScriptController {

    private final SeleniumScriptService seleniumScriptService;

    @Autowired
    public SeleniumScriptController(SeleniumScriptService seleniumScriptService) {
        this.seleniumScriptService = seleniumScriptService;
    }

    @PostMapping("/generate-script")
    public ResponseEntity<SeleniumScriptDTO> generateScript(@PathVariable Long testCaseId) {
        try {
            SeleniumScriptDTO generated = seleniumScriptService.generateScript(testCaseId);
            return new ResponseEntity<>(generated, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/script")
    public ResponseEntity<SeleniumScriptDTO> getScriptByTestCase(@PathVariable Long testCaseId) {
        try {
            SeleniumScriptDTO script = seleniumScriptService.getScriptByTestCase(testCaseId);
            return ResponseEntity.ok(script);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
