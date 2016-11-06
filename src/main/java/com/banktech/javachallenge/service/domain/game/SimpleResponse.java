package com.banktech.javachallenge.service.domain.game;


public class SimpleResponse {
    protected String message;
    protected Integer code;

    public SimpleResponse() {
    }

    public SimpleResponse(String message, int code) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "{" +
                "message='" + message +
                ", code=" + code +
                '}';
    }
}
