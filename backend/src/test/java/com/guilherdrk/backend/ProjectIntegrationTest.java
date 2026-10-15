package com.guilherdrk.backend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public class ProjectIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve criar um projeto no PostgreSQL real e listá-lo em seguida")
    void shouldCreateAndListProjectInRealDatabase() throws Exception {
        // 1. DADO: um JSON com os dados do novo projeto
        var newProjectJson = """
                    {
                        "name": "Projeto Testcontainer",
                        "description": "Criado e persistido de verdade",
                        "repoUrl": "https://github.com/guilherdrk/testcontainer"
                    }
                """;

        // 2. QUANDO: fazemos o POST /api/v1/projects
        mockMvc.perform(post("/api/v1/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newProjectJson))
                // 3. ENTÃO: esperamos status 201 Created
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Projeto Testcontainer"));

        // 4. E QUANDO: fazemos um GET /api/v1/projects logo em seguida...
        mockMvc.perform(get("/api/v1/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    @DisplayName("Deve rejeitar criação com dados inválidos retornando RFC 7807 Problem Details")
    void shouldReturnProblemDetailsWhenRequestIsInvalid() throws Exception {
        // Cenário de erro: nome vazio (viola @NotBlank)
        var invalidJson = """
                    {
                        "name": "",
                        "description": "Sem nome"
                    }
                """;

        mockMvc.perform(post("/api/v1/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Erro de Validação"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.name").exists());
    }
}
