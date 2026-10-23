package com.guilherdrk.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import com.guilherdrk.backend.exception.ProjectNotFoundException;

import com.guilherdrk.backend.entity.Project;
import com.guilherdrk.backend.repository.ProjectRepository;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository; // O dublê

    @InjectMocks
    private ProjectService projectService; // A classe real que vamos testar

    @Test
    @DisplayName("Deve retornar lista de ProjectResponse quando existirem projetos")
    void shouldReturnListOfProjectResponses() {
        // 1. ARRANGE (Preparar o cenário)
        var project = new Project();
        project.setName("DevPulse");
        project.setDescription("Plataforma de portfólio vivo");
        project.setRepoUrl("https://github.com/guilherdrk/DevPulse");
        project.setCreatedAt(Instant.now());

        // Ensinamos o mock: "quando chamarem projectRepository.findAll(), devolva essa
        // lista"
        when(projectRepository.findAll()).thenReturn(List.of(project));

        // 2. ACT (Executar a ação)
        var result = projectService.findAll();

        // 3. ASSERT (Verificar o resultado)
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("DevPulse", result.get(0).name());
        assertEquals("Plataforma de portfólio vivo", result.get(0).description());
        assertEquals("https://github.com/guilherdrk/DevPulse", result.get(0).repoUrl());
        assertNotNull(result.get(0).createdAt());

        // Bônus: verifica se o método findAll() do repositório foi chamado exatamente 1
        // vez
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existirem projetos")
    void shouldReturnEmptyListWhenNoProjectsExist() {
        // 1. ARRANGE
        when(projectRepository.findAll()).thenReturn(List.of());

        // 2. ACT
        var result = projectService.findAll();

        // 3. ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve lançar ProjectNotFoundException quando o projeto não for encontrado")
    void shouldThrowExceptionWhenProjectNotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ProjectNotFoundException.class, () -> projectService.findById(99L));
    }
}
