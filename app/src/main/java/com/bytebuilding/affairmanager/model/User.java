package com.bytebuilding.affairmanager.model;

/**
 * Created by atlas on 15.03.17.
 */

public class User {

    private long userId;
    private String userLogin;
    private String userPassword;
    private UserGroup userGroup;

    public User() {
        this.userId = -1;
        this.userLogin = "userLogin";
        this.userPassword = "userPass";
        this.userGroup = new UserGroup();
    }

    public User(long userId, String login, String password, UserGroup userGroup) {
        this.userId = userId;
        this.userLogin = login;
        this.userPassword = password;
        this.userGroup = userGroup;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }
}
