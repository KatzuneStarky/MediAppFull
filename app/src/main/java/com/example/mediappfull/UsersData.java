package com.example.mediappfull;

public class UsersData {
    private String userId;
    private String username;
    private String email;
    private String gender;
    private String mobile;
    private String age;
    private String weight;
    private String height;
    private String imageUrl;

    public UsersData(String userId, String username, String email, String gender, String mobile, String age, String weight, String height, String imageUrl) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.mobile = mobile;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.imageUrl = imageUrl;
    }

    public UsersData() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getImageURL() {
        return imageUrl;
    }

    public void setImageURL(String imageURL) {
        this.imageUrl = imageURL;
    }
}
