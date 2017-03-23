package com.bytebuilding.affairmanager.model.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by atlas on 15.03.17.
 */

public class User extends RealmObject {

    @PrimaryKey
    private long userId;
    @Required
    private String userLogin;
    @Required
    private String userPassword;
    private String userOrganization;
    private RealmList<UserGroup> userGroups;
    private RealmList<UserAffair> userAffairs;

    public User() {
        this.userId = -1;
        this.userLogin = "userLogin";
        this.userPassword = "userPass";
    }

    public User(long userId, String login, String password, String organization,
                RealmList<UserGroup> userGroups, RealmList<UserAffair> userAffairs) {
        this.userId = userId;
        this.userLogin = login;
        this.userPassword = password;
        this.userGroups = userGroups;
        this.userAffairs = userAffairs;
        this.userOrganization = organization;
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

    public RealmList<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(RealmList<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    public RealmList<UserAffair> getUserAffairs() {
        return userAffairs;
    }

    public void setUserAffairs(RealmList<UserAffair> userAffairs) {
        this.userAffairs = userAffairs;
    }

    public String getUserOrganization() {
        return userOrganization;
    }

    public void setUserOrganization(String userOrganization) {
        this.userOrganization = userOrganization;
    }
}
