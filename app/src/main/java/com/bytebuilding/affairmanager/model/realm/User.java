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

    private long userGroupId;

    public User() {
        this.userId = -1;
        this.userLogin = "userLogin";
        this.userPassword = "userPass";
    }

    public User(long userId, String userLogin, String userPassword, String userOrganization, long userGroupId) {
        this.userId = userId;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.userOrganization = userOrganization;
        this.userGroupId = userGroupId;
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

    public long getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(long userGroupId) {
        this.userGroupId = userGroupId;
    }
}
