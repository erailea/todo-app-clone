package com.erailea.todoappclone.exception;

public class BusinessException extends BaseException {
    public BusinessException(String errorCode, String message) {
        super(errorCode, message);
    }
} 