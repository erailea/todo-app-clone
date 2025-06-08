package com.erailea.todoappclone.exception;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super("RESOURCE_NOT_FOUND",
                String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
} 