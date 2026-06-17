package com.qacopilot.api.controller;

import com.qacopilot.api.dto.RequirementDTO;
import com.qacopilot.api.service.RequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requirements")
public class RequirementController {

    private final RequirementService requirementService;

    @Autowired
    public RequirementController(RequirementService requirementService) {
        this.requirementService = requirementService;
    }

    @PostMapping
    public ResponseEntity<RequirementDTO> createRequirement(@RequestBody RequirementDTO requirementDTO) {
        RequirementDTO created = requirementService.createRequirement(requirementDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RequirementDTO>> getAllRequirements() {
        List<RequirementDTO> requirements = requirementService.getAllRequirements();
        return ResponseEntity.ok(requirements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequirementDTO> getRequirementById(@PathVariable Long id) {
        try {
            RequirementDTO requirement = requirementService.getRequirementById(id);
            return ResponseEntity.ok(requirement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
