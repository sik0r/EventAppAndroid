package com.example.financemanager;

public class Constants {

    public static final String JWT_NAME = "jwt_token";

    public static class Url {
        public static final String BASE_PATH = "http://10.0.2.2:5000";
        public static final String LOGIN = BASE_PATH + "/account/login";
        public static final String EVENTS = BASE_PATH + "/events";
        public static final String TICKETS = BASE_PATH + "/tickets";
    }
}
