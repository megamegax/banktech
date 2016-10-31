package com.banktech.javachallenge;

import com.banktech.javachallenge.service.Api;

/**
 *
 */
public class Main {

    public static void main(String[] args) {
        if (args.length == 2) {
            setUpApi(args[0], args[1]);
        } else {
            setUpApi();
        }
    }

    private static void setUpApi() {
        Api.initialize();
    }

    private static void setUpApi(String baseUrl, String port) {
        Integer portNumber = convertToInt(port);
        Api.initialize(baseUrl, portNumber);
    }

    private static Integer convertToInt(String port) {
        return Integer.parseInt(port);
    }
}
