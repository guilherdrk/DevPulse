package com.guilherdrk.backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.guilherdrk.backend.dto.ProjectResponse;
import com.guilherdrk.backend.service.ProjectService;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @Test
    public void shouldReturnListOfProjects() throws Exception {
        var project1 = new ProjectResponse(1L,
                "Project 1",
                "Description",
                "https://repo.url/",
                Instant.now());
        when(projectService.findAll()).thenReturn(List.of(project1));

        var project2 = new ProjectResponse(2L,
                "Project 2",
                "Description 2",
                "https://repo.url/2/",
                Instant.now());
        when(projectService.findAll()).thenReturn(List.of(project1, project2));

        mockMvc.perform(get("/api/v1/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Project 1"))
                .andExpect(jsonPath("$[0].createdAt").exists());
    }

    @Test
    public void shouldReturnEmptyList() throws Exception {
        when(projectService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

    }
}
