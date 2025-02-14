package com.inventory.util;

public class ErrorResponse {
    private String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getMessage() {
        return error;
    }

    public void setMessage(String error) {
        this.error = error;
    }
}