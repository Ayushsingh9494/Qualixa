package com.qacopilot.api.service;

import com.qacopilot.api.dto.GeminiRequest;
import com.qacopilot.api.dto.GeminiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class GeminiServiceImpl implements GeminiService {

    private final RestClient restClient;
    private final String apiKey;
    private final String model;

    public GeminiServiceImpl(
            RestClient.Builder restClientBuilder,
            @Value("${gemini.api.key}") String apiKey,
            @Value("${gemini.model:gemini-2.5-flash}") String model
    ) {
        this.restClient = restClientBuilder.baseUrl("https://generativelanguage.googleapis.com").build();
        this.apiKey = apiKey;
        this.model = model;
    }

    @Override
    public String generateContent(String systemInstruction, String prompt) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("Gemini API key is not configured. Please set the GEMINI_API_KEY environment variable.");
        }

        GeminiRequest.Part promptPart = new GeminiRequest.Part(prompt);
        GeminiRequest.Content content = new GeminiRequest.Content(List.of(promptPart));

        GeminiRequest.Content sysInstructionContent = null;
        if (systemInstruction != null && !systemInstruction.trim().isEmpty()) {
            GeminiRequest.Part sysPart = new GeminiRequest.Part(systemInstruction);
            sysInstructionContent = new GeminiRequest.Content(List.of(sysPart));
        }

        GeminiRequest.GenerationConfig config = new GeminiRequest.GenerationConfig("application/json");

        GeminiRequest requestPayload = new GeminiRequest(
                List.of(content),
                sysInstructionContent,
                config
        );

        GeminiResponse response = restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/" + model + ":generateContent")
                        .queryParam("key", apiKey)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestPayload)
                .retrieve()
                .body(GeminiResponse.class);

        if (response == null || response.candidates() == null || response.candidates().isEmpty()) {
            throw new RuntimeException("Empty response from Gemini API");
        }

        GeminiResponse.Candidate candidate = response.candidates().getFirst();
        if (candidate.content() == null || candidate.content().parts() == null || candidate.content().parts().isEmpty()) {
            throw new RuntimeException("No content returned from Gemini API candidates");
        }

        return candidate.content().parts().getFirst().text();
    }
}
