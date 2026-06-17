package com.qacopilot.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "selenium_scripts")
public class SeleniumScript {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "test_case_id", nullable = false, unique = true)
    private TestCase testCase;

    @Column(name = "script_code", nullable = false, columnDefinition = "TEXT")
    private String scriptCode;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public SeleniumScript() {
    }

    public SeleniumScript(Long id, TestCase testCase, String scriptCode, LocalDateTime createdAt) {
        this.id = id;
        this.testCase = testCase;
        this.scriptCode = scriptCode;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
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

    public String getScriptCode() {
        return scriptCode;
    }

    public void setScriptCode(String scriptCode) {
        this.scriptCode = scriptCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private TestCase testCase;
        private String scriptCode;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder testCase(TestCase testCase) {
            this.testCase = testCase;
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

        public SeleniumScript build() {
            return new SeleniumScript(id, testCase, scriptCode, createdAt);
        }
    }
}
