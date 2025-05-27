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
      String fieldName = ((FieldError) error).getField(); // Asegúrate de que esto sea FieldError
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Error",
            "Validation failed for some fields.",
            request.getDescription(false).replace("uri=", ""),
            errors // Pasa el mapa de errores al constructor
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

  // Manejo de excepciones de "No encontrado"
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(
          RuntimeException ex, WebRequest request) {
    // Podrías tener una lógica aquí para identificar si es un 404 (Not Found)
    // por ejemplo, si el mensaje de la excepción contiene "not found".
    // Para empezar, lo dejaremos como INTERNAL_SERVER_ERROR a menos que sea un NotFoundException explícito.
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    String errorMessage = "An unexpected error occurred.";
    String errorName = "Internal Server Error";

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

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
          ResourceNotFoundException ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
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
