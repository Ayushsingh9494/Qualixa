package com.qacopilot.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_executions")
public class TestExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "test_case_id", nullable = false)
    private TestCase testCase;

    @Column(nullable = false)
    private String status; // PASSED, FAILED

    @Column(name = "execution_time_ms", nullable = false)
    private Long executionTimeMs;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "screenshot_base64", columnDefinition = "TEXT")
    private String screenshotBase64;

    @Column(name = "executed_at", nullable = false, updatable = false)
    private LocalDateTime executedAt;

    public TestExecution() {
    }

    public TestExecution(Long id, TestCase testCase, String status, Long executionTimeMs, String errorMessage, String screenshotBase64, LocalDateTime executedAt) {
        this.id = id;
        this.testCase = testCase;
        this.status = status;
        this.executionTimeMs = executionTimeMs;
        this.errorMessage = errorMessage;
        this.screenshotBase64 = screenshotBase64;
        this.executedAt = executedAt;
    }

    @PrePersist
    protected void onCreate() {
        executedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(Long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getScreenshotBase64() {
        return screenshotBase64;
    }

    public void setScreenshotBase64(String screenshotBase64) {
        this.screenshotBase64 = screenshotBase64;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private TestCase testCase;
        private String status;
        private Long executionTimeMs;
        private String errorMessage;
        private String screenshotBase64;
        private LocalDateTime executedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder testCase(TestCase testCase) {
            this.testCase = testCase;
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

        public TestExecution build() {
            return new TestExecution(id, testCase, status, executionTimeMs, errorMessage, screenshotBase64, executedAt);
        }
    }
}
