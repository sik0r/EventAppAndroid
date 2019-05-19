package com.example.financemanager.Models;

public class JwtTokenModel {

    private String token;

    public JwtTokenModel(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
