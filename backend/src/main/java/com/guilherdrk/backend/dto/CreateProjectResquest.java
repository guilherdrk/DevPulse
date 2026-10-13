package com.guilherdrk.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateProjectResquest(

        @NotBlank @Size(max = 100) String name,
        @Size(max = 1000) String description,
        @Pattern(regexp = "https?:\\/\\/.*") @Size(max = 255) String repoUrl) {

}
