package net.nonworkspace.demo.controller.advice;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("result", -1L);
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            FieldError fieldError = (FieldError) error;
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            errors.put("message", errorMessage);
            errors.put("field", fieldName);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
