package com.erailea.todoappclone.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final String errorCode;
    private final String message;

    protected BaseException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }
} 