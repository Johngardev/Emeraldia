package com.emeraldia.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
  // Manejo de errores de validación de @Valid
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
          MethodArgumentNotValidException ex, WebRequest request) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            "Validation failed for request body.",
            request.getDescription(false).replace("uri=", ""),
            errors
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  // Manejo de IllegalArgumentException (ej. si usas 'throw new IllegalArgumentException')
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
          IllegalArgumentException ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  // Manejo de excepciones de "No encontrado" (Personaliza esto si tienes tus propias excepciones NotFound)
  // Puedes crear una clase como public class ResourceNotFoundException extends RuntimeException {}
  // y usarla en tus servicios.
  @ExceptionHandler(RuntimeException.class) // Usar RuntimeException como fallback general,
  // pero es mejor tener excepciones más específicas
  public ResponseEntity<ErrorResponse> handleRuntimeException(
          RuntimeException ex, WebRequest request) {
    // Podrías tener una lógica aquí para identificar si es un 404 (Not Found)
    // por ejemplo, si el mensaje de la excepción contiene "not found".
    // Para empezar, lo dejaremos como INTERNAL_SERVER_ERROR a menos que sea un NotFoundException explícito.
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    String errorMessage = "An unexpected error occurred.";
    String errorName = "Internal Server Error";

    // Si tienes una excepción personalizada para "No encontrado"
    // if (ex instanceof ResourceNotFoundException) {
    //     status = HttpStatus.NOT_FOUND;
    //     errorMessage = ex.getMessage();
    //     errorName = "Not Found";
    // } else {
    //     errorMessage = ex.getMessage(); // O un mensaje genérico si no quieres exponer detalles internos
    // }

    // Asumo que tu servicio lanza RuntimeException para "not found" o "bad request"
    // Si tienes "ResourceNotFoundException" o similar, la manejamos:
    if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("not found")) {
      status = HttpStatus.NOT_FOUND;
      errorName = "Not Found";
      errorMessage = ex.getMessage();
    } else {
      errorMessage = ex.getMessage(); // O un mensaje más genérico para producción
    }


    ErrorResponse errorResponse = new ErrorResponse(
            status.value(),
            errorName,
            errorMessage,
            request.getDescription(false).replace("uri=", "")
    );
    return new ResponseEntity<>(errorResponse, status);
  }


  // Manejo de cualquier otra excepción no capturada
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(
          Exception ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "An unexpected error occurred: " + ex.getMessage(), // En producción, evita exponer ex.getMessage()
            request.getDescription(false).replace("uri=", "")
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
