package com.mimcrea.ping.Model;

public class UserModel {
    private String userName;
    private String userEmail;
    private String userPictureUrl;
    private String userUid;

    public UserModel(){

    }

    public UserModel( String userUid,String userName, String userEmail, String userPictureUrl) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPictureUrl = userPictureUrl;
        this.userUid = userUid;
    }

    public UserModel(String userName, String userUid) {
        this.userName = userName;
        this.userUid = userUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPictureUrl() {
        return userPictureUrl;
    }
}
