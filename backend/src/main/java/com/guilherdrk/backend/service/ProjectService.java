package com.guilherdrk.backend.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guilherdrk.backend.dto.CreateProjectRequest;
import com.guilherdrk.backend.dto.ProjectResponse;
import com.guilherdrk.backend.entity.Project;
import com.guilherdrk.backend.exception.ProjectNotFoundException;
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

    @Transactional(readOnly = true)
    public ProjectResponse findById(Long id) {
        var project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        return ProjectResponse.fromEntity(project);
    }

    @Transactional
    public ProjectResponse create(CreateProjectRequest request) {
        var project = new Project();
        project.setName(request.name());
        project.setDescription(request.description());
        project.setRepoUrl((request.repoUrl()));
        project.setCreatedAt(Instant.now());

        // var saved = projectRepository.save(project);
        return ProjectResponse.fromEntity(projectRepository.save(project));
    }

    @Transactional
    public ProjectResponse update(Long id, CreateProjectRequest request) {
        var project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        project.setName(request.name());
        project.setDescription(request.description());
        project.setRepoUrl(request.repoUrl());

        projectRepository.save(project);
        return ProjectResponse.fromEntity(project);

    }

    @Transactional
    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ProjectNotFoundException(id);
        }
        projectRepository.deleteById(id);
    }
}
