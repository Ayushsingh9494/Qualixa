package com.qacopilot.api.dto;

import java.time.LocalDateTime;

public record RequirementDTO(
    Long id,
    String title,
    String description,
    LocalDateTime createdAt
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String title;
        private String description;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public RequirementDTO build() {
            return new RequirementDTO(id, title, description, createdAt);
        }
    }
}
