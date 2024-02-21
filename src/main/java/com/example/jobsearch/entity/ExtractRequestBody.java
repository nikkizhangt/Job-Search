package com.example.jobsearch.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ExtractRequestBody {
    public String text;

    @JsonProperty("max_keywords")
    public int maxKeywords;

    public String language = "en";

    public String providers = "ibm";

    @JsonProperty("response_as_dict")
    public boolean responseAsDict = true;

    public ExtractRequestBody(String text) {
        this.text = text;
    }

}