package com.coveragex.todoapplication.exception;

public class CustomException extends RuntimeException{
    private final long code;
    private final String message;

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public CustomException(long code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

}
