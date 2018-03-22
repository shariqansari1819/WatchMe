package com.shariqansari.watchme.pojo;

public class Posts {
    private String postId;
    private String postName;
    private String postUrl;
    private String userId;

    public Posts() {
    }

    public Posts(String postId, String postName, String postUrl, String userId) {
        this.postId = postId;
        this.postName = postName;
        this.postUrl = postUrl;
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
