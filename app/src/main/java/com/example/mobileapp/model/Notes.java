package com.example.mobileapp.model;

import com.google.firebase.firestore.Exclude;
import java.util.List;

public class Notes {
    private String id;
    private String name;
    private String url;
    private String subject;
    private int pages;
    private int views;
    private List<Integer> tags;

    public Notes() {
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setTags(List<Integer> tags) {
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getSubject() {
        return subject;
    }

    public int getPages() {
        return pages;
    }

    public int getViews() {
        return views;
    }

    public List<Integer> getTags() {
        return tags;
    }
}
