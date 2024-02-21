package com.example.jobsearch.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HistoryRequestBody {
    @JsonProperty("user_id")
    public String userId;
    public Item favorite;
}

