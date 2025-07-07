package com.coveragex.todoapplication.dto.response;

public class AuthSuccessDTO {
    private boolean isSuccess;
    private Object message;

    public AuthSuccessDTO(boolean isSuccess, Object message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
