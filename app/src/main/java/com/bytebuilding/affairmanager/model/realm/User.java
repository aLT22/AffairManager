package com.bytebuilding.affairmanager.model.realm;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by atlas on 15.03.17.
 */

public class User extends RealmObject {

    private long userId;
    @Required
    private String userLogin;
    @Required
    private String userPassword;
    private String userOrganization;

    private RealmList<UserGroup> userGroups;

    public User() {
        this.userId = -1;
        this.userLogin = "userLogin";
        this.userPassword = "userPass";
        this.userGroups = new RealmList<>();
    }

    public User(long userId, String userLogin, String userPassword, String userOrganization, RealmList<UserGroup> userGroups) {
        this.userId = userId;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.userOrganization = userOrganization;
        this.userGroups = userGroups;
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

    public String getUserOrganization() {
        return userOrganization;
    }

    public void setUserOrganization(String userOrganization) {
        this.userOrganization = userOrganization;
    }

    public RealmList<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(RealmList<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    public void addUserGroup(UserGroup userGroup) {
        this.userGroups.add(userGroup);
    }
}