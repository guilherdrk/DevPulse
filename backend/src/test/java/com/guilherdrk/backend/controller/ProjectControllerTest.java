package com.guilherdrk.backend.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.guilherdrk.backend.dto.CreateProjectRequest;
import com.guilherdrk.backend.dto.ProjectResponse;
import com.guilherdrk.backend.exception.ProjectNotFoundException;
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

        @Test
        public void shouldReturnProjectById() throws Exception {
                var project1 = new ProjectResponse(1L,
                                "Project 1",
                                "Description",
                                "https://repo.url/",
                                Instant.now());
                when(projectService.findById(1L)).thenReturn(project1);

                mockMvc.perform(get("/api/v1/projects/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.name").value("Project 1"))
                                .andExpect(jsonPath("$.description").value("Description"))
                                .andExpect(jsonPath("$.repoUrl").value("https://repo.url/"))
                                .andExpect(jsonPath("$.createdAt").exists());
        }

        @Test
        public void shouldReturnNotFoundWhenProjectDoesNotExist() throws Exception {
                when(projectService.findById(99L)).thenThrow(new ProjectNotFoundException(99L));

                mockMvc.perform(get("/api/v1/projects/99"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.detail").value("Projeto não encontrado: 99"))
                                .andExpect(jsonPath("$.title").value("Projeto não encontrado"));
        }

        @Test
        public void shouldCreateProject() throws Exception {
                var projectRequest = new CreateProjectRequest(
                                "New Project",
                                "New Description",
                                "https://repo.url/new/");

                var createdProject = new ProjectResponse(1L,
                                "New Project",
                                "New Description",
                                "https://repo.url/new/",
                                Instant.now());

                when(projectService.create(projectRequest)).thenReturn(createdProject);

                mockMvc.perform(post("/api/v1/projects")
                                .contentType("application/json")
                                .content("{\"name\":\"New Project\", \"description\":\"New Description\", \"repoUrl\":\"https://repo.url/new/\"}"))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.name").value("New Project"))
                                .andExpect(jsonPath("$.repoUrl").value("https://repo.url/new/"))
                                .andExpect(jsonPath("$.createdAt").exists());
        }

        @Test
        public void shouldReturnBadRequestWhenInvalidName() throws Exception {
                mockMvc.perform(post("/api/v1/projects")
                                .contentType("application/json")
                                .content("{\"name\":\"\", \"description\":\"Description\", \"repoUrl\":\"https://repo.url/\"}"))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.errors.name").value("não pode estar em branco"));
        }

        @Test
        public void shouldReturnBadRequestWhenInvalidRepoUrl() throws Exception {
                mockMvc.perform(post("/api/v1/projects")
                                .contentType("application/json")
                                .content("{\"name\":\"Project\", \"description\":\"Description\", \"repoUrl\":\"invalid-url\"}"))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.errors.repoUrl").value("deve ser uma URL válida"));
        }

        @Test
        public void shouldUpdateProject() throws Exception {
                var updateRequest = new CreateProjectRequest(
                                "Updated Project",
                                "Updated Description",
                                "https://repo.url/updated/");

                var updatedProject = new ProjectResponse(1L,
                                "Updated Project",
                                "Updated Description",
                                "https://repo.url/updated/",
                                Instant.now());

                when(projectService.update(1L, updateRequest)).thenReturn(updatedProject);

                mockMvc.perform(put("/api/v1/projects/1")
                                .contentType("application/json")
                                .content("{\"name\":\"Updated Project\", \"description\":\"Updated Description\", \"repoUrl\":\"https://repo.url/updated/\"}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.name").value("Updated Project"))
                                .andExpect(jsonPath("$.repoUrl").value("https://repo.url/updated/"));
        }

        @Test
        public void shouldReturnNotFoundWhenUpdateNonExistentProject() throws Exception {
                var updateRequest = new CreateProjectRequest(
                                "Updated Project",
                                "Updated Description",
                                "https://repo.url/updated/");

                when(projectService.update(999L, updateRequest))
                                .thenThrow(new ProjectNotFoundException(999L));

                mockMvc.perform(put("/api/v1/projects/999")
                                .contentType("application/json")
                                .content("{\"name\":\"Updated Project\", \"description\":\"Updated Description\", \"repoUrl\":\"https://repo.url/updated/\"}"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.detail").value("Projeto não encontrado: 999"));
        }

        @Test
        public void shouldDeleteProject() throws Exception {
                mockMvc.perform(delete("/api/v1/projects/1"))
                                .andExpect(status().isNoContent());
        }

        @Test
        public void shouldReturnNotFoundWhenDeleteNonExistentProject() throws Exception {
                doThrow(new ProjectNotFoundException(1L)).when(projectService).delete(1L);

                mockMvc.perform(delete("/api/v1/projects/1"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.detail").value("Projeto não encontrado: 1"));
        }

}
