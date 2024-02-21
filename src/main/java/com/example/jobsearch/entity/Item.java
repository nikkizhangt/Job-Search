package com.example.jobsearch.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true) //indicates other fields can be safely ignored
@JsonInclude(JsonInclude.Include.NON_NULL) //indicates null field can be skipped and not included

public class Item {
    private String id;
    private String title;
    private String company_name;

    private String location;
    private String via;
    private String description;
    private List<String> job_highlights;
    private String url;
    private Set<String> keywords;
    private boolean favorite;



    public Item() {
    }

    public Item(String id, String title, String company_name, String location, String via, String description, List<String> job_highlights, String url, Set<String> keywords, boolean favorite) {
        this.id = id;
        this.title = title;
        this.company_name = company_name;
        this.location = location;
        this.via = via;
        this.description = description;
        this.job_highlights = job_highlights;
        this.url = url;
        this.keywords = keywords;
        this.favorite = favorite;
    }
    @JsonProperty("id") //indicates mapping, not necessary if it's same as property name
    public String getId() {
        return id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }
    @JsonProperty("companey_name")
    public String getCompanyName() {
        return company_name;
    }
    @JsonProperty("location")
    public String getLocation() {

        return location;
    }
    @JsonProperty("via")
    public String getVia() {
        return via;
    }
    @JsonProperty("description")
    public String getDescription() {

        return description;
    }
    @JsonProperty("job_highlights")
    public List<String> getJobHighlights() {
        return job_highlights;
    }
    @JsonProperty("url")
    public String getUrl() {

        return url;
    }
    public Set<String> getKeywords() {

        return keywords;
    }

    public void setKeywords(Set<String> keywords) { //editable

        this.keywords = keywords;
    }
    public boolean getFavorite() {

        return favorite;
    }

    public void setFavorite(boolean favorite) { //editable

        this.favorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return favorite == item.favorite && Objects.equals(id, item.id) && Objects.equals(title, item.title) && Objects.equals(company_name, item.company_name) && Objects.equals(location, item.location) && Objects.equals(via, item.via) && Objects.equals(description, item.description) && Objects.equals(job_highlights, item.job_highlights) && Objects.equals(url, item.url) && Objects.equals(keywords, item.keywords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, company_name, location, via, description, job_highlights, url, keywords, favorite);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", company_name='" + company_name + '\'' +
                ", location='" + location + '\'' +
                ", via='" + via + '\'' +
                ", description='" + description + '\'' +
                ", job_highlights=" + job_highlights +
                ", url='" + url + '\'' +
                ", keywords=" + keywords +
                ", favorite=" + favorite +
                '}';
    }
}
