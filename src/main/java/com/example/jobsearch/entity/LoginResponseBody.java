package com.example.jobsearch.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponseBody {
    public String status;
    @JsonProperty("user_id")
    public String userId;
    public String name;
    public LoginResponseBody() {

    }
    public LoginResponseBody(String status, String userId, String name) {
        this.status = status;
        this.name = name;
        this.userId = userId;
    }
}
