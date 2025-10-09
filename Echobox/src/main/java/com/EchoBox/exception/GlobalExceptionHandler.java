package com.EchoBox.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Global exception handler for all controllers.
 * Intercepts exceptions and returns standardized error responses with localized messages.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Handle custom EchoboxException
     */
    @ExceptionHandler(EchoboxException.class)
    public ResponseEntity<ErrorResponse> handleEchoboxException(
            EchoboxException ex,
            HttpServletRequest request) {

        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(
            ex.getErrorCode().getCode(),
            ex.getArgs(),
            ex.getMessage(),
            locale
        );

        HttpStatus status = determineHttpStatus(ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .errorCode(ex.getErrorCode().getCode())
            .message(message)
            .status(status.value())
            .path(request.getRequestURI())
            .build();

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Handle ResourceNotFoundException
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(
            ex.getErrorCode().getCode(),
            ex.getArgs(),
            ex.getMessage(),
            locale
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
            .errorCode(ex.getErrorCode().getCode())
            .message(message)
            .status(HttpStatus.NOT_FOUND.value())
            .path(request.getRequestURI())
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle validation errors from @Valid annotation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Locale locale = LocaleContextHolder.getLocale();

        List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.add(ErrorResponse.FieldError.builder()
                .field(error.getField())
                .message(error.getDefaultMessage())
                .rejectedValue(error.getRejectedValue())
                .build());
        }

        String message = messageSource.getMessage(
            ErrorCode.VALIDATION_ERROR.getCode(),
            null,
            "Validation error",
            locale
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
            .errorCode(ErrorCode.VALIDATION_ERROR.getCode())
            .message(message)
            .status(HttpStatus.BAD_REQUEST.value())
            .path(request.getRequestURI())
            .fieldErrors(fieldErrors)
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle constraint violation exceptions
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        Locale locale = LocaleContextHolder.getLocale();

        List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            fieldErrors.add(ErrorResponse.FieldError.builder()
                .field(violation.getPropertyPath().toString())
                .message(violation.getMessage())
                .rejectedValue(violation.getInvalidValue())
                .build());
        }

        String message = messageSource.getMessage(
            ErrorCode.VALIDATION_ERROR.getCode(),
            null,
            "Validation error",
            locale
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
            .errorCode(ErrorCode.VALIDATION_ERROR.getCode())
            .message(message)
            .status(HttpStatus.BAD_REQUEST.value())
            .path(request.getRequestURI())
            .fieldErrors(fieldErrors)
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(
            ErrorCode.GENERIC_ERROR.getCode(),
            null,
            "An unexpected error occurred",
            locale
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
            .errorCode(ErrorCode.GENERIC_ERROR.getCode())
            .message(message)
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .path(request.getRequestURI())
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Determine HTTP status based on exception type
     */
    private HttpStatus determineHttpStatus(EchoboxException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        // Resource not found errors
        if (errorCode.getCode().endsWith("_NOT_FOUND")) {
            return HttpStatus.NOT_FOUND;
        }

        // Already exists errors
        if (errorCode.getCode().endsWith("_ALREADY_EXISTS")) {
            return HttpStatus.CONFLICT;
        }

        // Validation and invalid data errors
        if (errorCode.getCode().contains("INVALID") ||
            errorCode.getCode().contains("VALIDATION")) {
            return HttpStatus.BAD_REQUEST;
        }

        // Database errors
        if (errorCode.getCode().startsWith("ERR_8")) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        // Default to bad request
        return HttpStatus.BAD_REQUEST;
    }
}
