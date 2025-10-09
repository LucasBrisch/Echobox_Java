package com.EchoBox.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard error response structure returned to clients.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * Unique error code for identification
     */
    private String errorCode;

    /**
     * Human-readable error message (localized)
     */
    private String message;

    /**
     * HTTP status code
     */
    private int status;

    /**
     * Timestamp of when the error occurred
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Request path where the error occurred
     */
    private String path;

    /**
     * Additional validation errors (for field-level errors)
     */
    private List<FieldError> fieldErrors;

    /**
     * Represents a field-level validation error
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String message;
        private Object rejectedValue;
    }
}

