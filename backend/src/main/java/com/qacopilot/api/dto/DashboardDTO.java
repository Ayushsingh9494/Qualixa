package com.qacopilot.api.dto;

import java.util.List;

public record DashboardDTO(
    long totalTestCases,
    long passedTestCases,
    long failedTestCases,
    long untestedTestCases,
    List<TestExecutionDTO> recentExecutions
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long totalTestCases;
        private long passedTestCases;
        private long failedTestCases;
        private long untestedTestCases;
        private List<TestExecutionDTO> recentExecutions;

        public Builder totalTestCases(long totalTestCases) {
            this.totalTestCases = totalTestCases;
            return this;
        }

        public Builder passedTestCases(long passedTestCases) {
            this.passedTestCases = passedTestCases;
            return this;
        }

        public Builder failedTestCases(long failedTestCases) {
            this.failedTestCases = failedTestCases;
            return this;
        }

        public Builder untestedTestCases(long untestedTestCases) {
            this.untestedTestCases = untestedTestCases;
            return this;
        }

        public Builder recentExecutions(List<TestExecutionDTO> recentExecutions) {
            this.recentExecutions = recentExecutions;
            return this;
        }

        public DashboardDTO build() {
            return new DashboardDTO(totalTestCases, passedTestCases, failedTestCases, untestedTestCases, recentExecutions);
        }
    }
}
