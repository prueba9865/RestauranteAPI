package com.daw.restauranteapi.errors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.View;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

//        errors representa un JSON "clave" "valor". Vamos añadiendo posiciones que luego se transformará en JSON. Un ejemplo sería el siguiente:
//        errors.put("error", "El objeto no tiene los campos requeridos");
//        errors.put("date", LocalDate.now().toString());

        //Añadimos al mapa errors todos los errores que podemos coger de la excepción
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        //return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errorDetails = new HashMap<>();

        // Si el parámetro 'id' tiene un valor no válido (por ejemplo, '%20' o un espacio)
        if (ex.getValue() != null && ex.getValue().toString().trim().isEmpty()) {
            errorDetails.put("error", "El parámetro no puede estar vacío o contener espacios");
            errorDetails.put("message", "El valor del parámetro debe ser un número entero válido.");
        } else {
            errorDetails.put("error", "El parámetro debe ser un número entero válido.");
            errorDetails.put("message", "El valor proporcionado no se puede convertir a un número.");
        }

        return ResponseEntity.badRequest().body(errorDetails);  // Respuesta con error 400
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatch(DataIntegrityViolationException ex) {
        Map<String, String> errorDetails = new HashMap<>();

        errorDetails.put("error", "Algo fue mal");

        return ResponseEntity.internalServerError().body(errorDetails);  // Respuesta con error 400
    }

    // Manejo de excepciones generales (genéricas)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
//        response.put("error", "Error interno del servidor, contacto con el servicio técnico");
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Error interno del servidor");
        response.put("details", ex.getMessage());
        response.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
