package com.EchoBox.exception;

import lombok.Getter;

/**
 * Enumeration of all error codes in the system.
 * Each error has a unique code that can be used to fetch localized messages.
 */

@Getter
public enum ErrorCode {
    // Generic errors (1000-1099)
    GENERIC_ERROR("ERR_1000"),
    VALIDATION_ERROR("ERR_1001"),
    INVALID_INPUT("ERR_1002"),
    RESOURCE_NOT_FOUND("ERR_1003"),
    RESOURCE_ALREADY_EXISTS("ERR_1004"),

    // User errors (2000-2099)
    USER_NOT_FOUND("ERR_2000"),
    USER_ALREADY_EXISTS("ERR_2001"),
    USER_INVALID_EMAIL("ERR_2002"),
    USER_INVALID_DATA("ERR_2003"),

    // Feedback errors (3000-3099)
    FEEDBACK_NOT_FOUND("ERR_3000"),
    FEEDBACK_INVALID_DATA("ERR_3001"),
    FEEDBACK_CANNOT_DELETE("ERR_3002"),

    // Reply errors (4000-4099)
    REPLY_NOT_FOUND("ERR_4000"),
    REPLY_INVALID_DATA("ERR_4001"),

    // Company errors (5000-5099)
    COMPANY_NOT_FOUND("ERR_5000"),
    COMPANY_ALREADY_EXISTS("ERR_5001"),
    COMPANY_INVALID_DATA("ERR_5002"),

    // Category errors (6000-6099)
    CATEGORY_NOT_FOUND("ERR_6000"),
    CATEGORY_ALREADY_EXISTS("ERR_6001"),
    CATEGORY_INVALID_DATA("ERR_6002"),

    // Status errors (7000-7099)
    STATUS_NOT_FOUND("ERR_7000"),
    STATUS_INVALID_DATA("ERR_7001"),

    // Database errors (8000-8099)
    DATABASE_ERROR("ERR_8000"),
    DATABASE_CONNECTION_ERROR("ERR_8001");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

}
