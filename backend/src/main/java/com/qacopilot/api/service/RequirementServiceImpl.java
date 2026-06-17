package com.qacopilot.api.service;

import com.qacopilot.api.dto.RequirementDTO;
import com.qacopilot.api.entity.Requirement;
import com.qacopilot.api.entity.User;
import com.qacopilot.api.repository.RequirementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequirementServiceImpl implements RequirementService {

    private final RequirementRepository requirementRepository;

    @Autowired
    public RequirementServiceImpl(RequirementRepository requirementRepository) {
        this.requirementRepository = requirementRepository;
    }

    @Override
    public RequirementDTO createRequirement(RequirementDTO dto) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Requirement requirement = Requirement.builder()
                .title(dto.title())
                .description(dto.description())
                .user(currentUser)
                .build();

        Requirement saved = requirementRepository.save(requirement);
        return convertToDTO(saved);
    }

    @Override
    public List<RequirementDTO> getAllRequirements() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        return requirementRepository.findByUserId(currentUser.getId())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RequirementDTO getRequirementById(Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Requirement requirement = requirementRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Requirement not found with id: " + id));
        return convertToDTO(requirement);
    }

    private RequirementDTO convertToDTO(Requirement requirement) {
        return RequirementDTO.builder()
                .id(requirement.getId())
                .title(requirement.getTitle())
                .description(requirement.getDescription())
                .createdAt(requirement.getCreatedAt())
                .build();
    }
}
