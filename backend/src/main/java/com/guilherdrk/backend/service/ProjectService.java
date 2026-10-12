package com.guilherdrk.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guilherdrk.backend.dto.ProjectResponse;
import com.guilherdrk.backend.repository.ProjectRepository;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> findAll() {
        var projects = projectRepository.findAll();

        return projects.stream()
                .map(project -> ProjectResponse.fromEntity(project)).toList();
    }

}
