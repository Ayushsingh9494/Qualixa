package com.qacopilot.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qacopilot.api.dto.RequirementDTO;
import com.qacopilot.api.service.RequirementService;
import com.qacopilot.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequirementController.class)
@AutoConfigureMockMvc
public class RequirementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequirementService requirementService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void testCreateRequirement() throws Exception {
        RequirementDTO input = RequirementDTO.builder()
                .title("Login requirement")
                .description("As a user I want to login to the portal")
                .build();

        RequirementDTO output = RequirementDTO.builder()
                .id(1L)
                .title("Login requirement")
                .description("As a user I want to login to the portal")
                .createdAt(LocalDateTime.now())
                .build();

        Mockito.when(requirementService.createRequirement(Mockito.any(RequirementDTO.class))).thenReturn(output);

        mockMvc.perform(post("/api/requirements")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Login requirement"))
                .andExpect(jsonPath("$.description").value("As a user I want to login to the portal"));
    }

    @Test
    @WithMockUser
    public void testGetAllRequirements() throws Exception {
        RequirementDTO req = RequirementDTO.builder()
                .id(1L)
                .title("Search requirement")
                .description("As a user I want to search items")
                .createdAt(LocalDateTime.now())
                .build();

        Mockito.when(requirementService.getAllRequirements()).thenReturn(List.of(req));

        mockMvc.perform(get("/api/requirements")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Search requirement"));
    }
}
