package id.academy.springailabs.labs;

import jakarta.validation.constraints.NotBlank;

public record EvaluationInput(
        @NotBlank String question,
        @NotBlank String context,
        @NotBlank String response) {
}
