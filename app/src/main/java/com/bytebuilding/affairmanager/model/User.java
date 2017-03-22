package com.bytebuilding.affairmanager.model;

/**
 * Created by atlas on 15.03.17.
 */

public class User {

    private String login;
    private String password;
    private UserGroup userGroup;

    public User() {
    }

    public User(String login, String password, UserGroup userGroup) {
        this.login = login;
        this.password = password;
        this.userGroup = userGroup;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }
}
