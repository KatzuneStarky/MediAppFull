package com.example.mediappfull;

public class ImagesList {
    private String imageURL;

    public ImagesList(String imageURL) {
        this.imageURL = imageURL;
    }

    public ImagesList() {
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
