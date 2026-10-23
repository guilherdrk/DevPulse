package com.guilherdrk.backend.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Dados inválidos");
        problem.setTitle("Erro de Validação");
        var erros = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        e -> e.getDefaultMessage() == null ? "inválido" : e.getDefaultMessage(),
                        (a, b) -> a));
        problem.setProperty("errors", erros);
        return problem;

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ProblemDetail handlerHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Corpo da requisição inválido");
        problem.setTitle("Requisição inválida");
        var message = ex.getMostSpecificCause().getMessage();
        problem.setProperty("error", message);
        return problem;
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    ProblemDetail handlerProjectNotFoundException(ProjectNotFoundException ex) {
        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Projeto não encontrado");
        return problem;
    }

}
