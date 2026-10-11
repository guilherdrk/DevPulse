package com.guilherdrk.backend.dto;

import java.time.Instant;

import com.guilherdrk.backend.entity.Project;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        String repoUrl,
        Instant createdAt) {

    public static ProjectResponse fromEntity(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getRepoUrl(),
                project.getCreatedAt());
    }
}
