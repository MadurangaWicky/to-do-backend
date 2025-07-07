package com.coveragex.todoapplication.dto.response;

public class StandardResponse {
    private boolean isSuccess;

    public StandardResponse(boolean isSuccess, Object object) {
        this.isSuccess = isSuccess;
        this.object = object;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    private Object object;
}
