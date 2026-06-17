package com.qacopilot.api.service;

import com.qacopilot.api.dto.RequirementDTO;
import java.util.List;

public interface RequirementService {
    RequirementDTO createRequirement(RequirementDTO requirementDTO);
    List<RequirementDTO> getAllRequirements();
    RequirementDTO getRequirementById(Long id);
}
