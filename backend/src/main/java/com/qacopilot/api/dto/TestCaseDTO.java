package com.qacopilot.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TestCaseDTO(
    Long id,
    Long requirementId,
    String testCaseId,
    String title,
    String preconditions,
    List<String> steps,
    String expectedResult,
    LocalDateTime createdAt
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long requirementId;
        private String testCaseId;
        private String title;
        private String preconditions;
        private List<String> steps;
        private String expectedResult;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder requirementId(Long requirementId) {
            this.requirementId = requirementId;
            return this;
        }

        public Builder testCaseId(String testCaseId) {
            this.testCaseId = testCaseId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder preconditions(String preconditions) {
            this.preconditions = preconditions;
            return this;
        }

        public Builder steps(List<String> steps) {
            this.steps = steps;
            return this;
        }

        public Builder expectedResult(String expectedResult) {
            this.expectedResult = expectedResult;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public TestCaseDTO build() {
            return new TestCaseDTO(id, requirementId, testCaseId, title, preconditions, steps, expectedResult, createdAt);
        }
    }
}
