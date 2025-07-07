package com.coveragex.todoapplication.dto.response;

public class ErrorResponse {
    private String message;
    private Long code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public ErrorResponse(String message, Long code) {
        this.message = message;
        this.code = code;
    }



}
