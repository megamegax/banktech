package com.banktech.javachallenge.view.domain;

import com.banktech.javachallenge.service.domain.game.SimpleResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ApiCall {
    private String time;
    private String command;
    private String extra;
    SimpleResponse response;

    public ApiCall() {
        time = LocalDateTime.now().toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public ApiCall(String command, String extra, SimpleResponse response) {
        this();
        this.response = response;
        this.command = command;
        this.extra = extra;
    }

    public ApiCall(String command, Number extra, SimpleResponse response) {
        this();
        this.response = response;
        this.command = command;
        this.extra = String.valueOf(extra);

    }


    public SimpleResponse getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "ApiCall{" +
                "time='" + time + '\'' +
                ", command='" + command +
                " (" + extra + ")'" +
                ", response=" + response +
                '}';
    }
}
