package com.qacopilot.api.dto;

import java.time.LocalDateTime;

public record TestExecutionDTO(
    Long id,
    Long testCaseId,
    String status,
    Long executionTimeMs,
    String errorMessage,
    String screenshotBase64,
    LocalDateTime executedAt
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long testCaseId;
        private String status;
        private Long executionTimeMs;
        private String errorMessage;
        private String screenshotBase64;
        private LocalDateTime executedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder testCaseId(Long testCaseId) {
            this.testCaseId = testCaseId;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder executionTimeMs(Long executionTimeMs) {
            this.executionTimeMs = executionTimeMs;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public Builder screenshotBase64(String screenshotBase64) {
            this.screenshotBase64 = screenshotBase64;
            return this;
        }

        public Builder executedAt(LocalDateTime executedAt) {
            this.executedAt = executedAt;
            return this;
        }

        public TestExecutionDTO build() {
            return new TestExecutionDTO(id, testCaseId, status, executionTimeMs, errorMessage, screenshotBase64, executedAt);
        }
    }
}
