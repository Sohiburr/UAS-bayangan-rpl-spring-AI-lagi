package id.academy.springailabs.labs;

public record EvaluationResult(String evaluator, boolean passed, float score, String feedback) {
}
