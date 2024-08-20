package net.nonworkspace.demo.controller.advice;

import java.util.HashMap;
import java.util.Map;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
        final MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("result", -1L);
        ex.getBindingResult().getAllErrors().stream().map(error -> (FieldError) error)
            .forEach(fieldError -> {
                String fieldName = fieldError.getField();
                String errorMessage = fieldError.getDefaultMessage();
                errors.put("message", errorMessage);
                errors.put("field", fieldName);
            });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(
        final AuthenticationException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("result", -1L);
        errors.put("message", CommonBizExceptionCode.ACCESS_NOT_ALLOWED.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
    }
}
