package com.qacopilot.api.dto;

import java.util.List;

public record GeminiRequest(
    List<Content> contents,
    Content systemInstruction,
    GenerationConfig generationConfig
) {
    public record Content(List<Part> parts) {}
    public record Part(String text) {}
    public record GenerationConfig(String responseMimeType) {}
}
