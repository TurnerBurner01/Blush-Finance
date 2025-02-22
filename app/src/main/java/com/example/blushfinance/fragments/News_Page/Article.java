package com.example.blushfinance.fragments.News_Page;

import com.google.gson.annotations.SerializedName;

public class Article {
    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("urlToImage")
    private String urlToImage;

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }
}