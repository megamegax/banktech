package com.banktech.javachallenge.view;

import com.banktech.javachallenge.service.domain.game.SimpleResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ApiCall {
    String time;
    String method;
    String url;
    SimpleResponse response;

    public ApiCall() {
        time = LocalDateTime.now().toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public ApiCall(String method, String url, SimpleResponse response) {
        this.method = method;
        this.url = url;
        this.response = response;
        time = LocalDateTime.now().toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SimpleResponse getResponse() {
        return response;
    }

    public void setResponse(SimpleResponse response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ApiCall{" +
                "time='" + time + '\'' +
                ", method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", response=" + response +
                '}';
    }
}
