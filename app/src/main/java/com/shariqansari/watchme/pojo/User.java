package com.shariqansari.watchme.pojo;

public class User {

    private String userId;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userProfileImage;
    private String userProfileImageThumbnail;
    private String userGender;
    private String userTitle;
    private String userProfileName;

    public User() {
    }

    public User(String userId, String userName, String userEmail, String userPassword, String userProfileImage, String userProfileImageThumbnail, String userGender, String userTitle, String userProfileName) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userProfileImage = userProfileImage;
        this.userProfileImageThumbnail = userProfileImageThumbnail;
        this.userGender = userGender;
        this.userTitle = userTitle;
        this.userProfileName = userProfileName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public String getUserProfileImageThumbnail() {
        return userProfileImageThumbnail;
    }

    public void setUserProfileImageThumbnail(String userProfileImageThumbnail) {
        this.userProfileImageThumbnail = userProfileImageThumbnail;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public String getUserProfileName() {
        return userProfileName;
    }

    public void setUserProfileName(String userProfileName) {
        this.userProfileName = userProfileName;
    }
}
