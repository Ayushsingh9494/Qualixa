package com.qacopilot.api.dto;

import java.time.LocalDateTime;

public record SeleniumScriptDTO(
    Long id,
    Long testCaseId,
    String scriptCode,
    LocalDateTime createdAt
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long testCaseId;
        private String scriptCode;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder testCaseId(Long testCaseId) {
            this.testCaseId = testCaseId;
            return this;
        }

        public Builder scriptCode(String scriptCode) {
            this.scriptCode = scriptCode;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public SeleniumScriptDTO build() {
            return new SeleniumScriptDTO(id, testCaseId, scriptCode, createdAt);
        }
    }
}
