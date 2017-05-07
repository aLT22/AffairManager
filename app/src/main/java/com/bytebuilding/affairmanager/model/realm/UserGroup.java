package com.bytebuilding.affairmanager.model.realm;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by atlas on 22.03.17.
 */

public class UserGroup extends RealmObject {

    @PrimaryKey
    private long userGroupId;
    @Required
    private String userGroupName;
    private String userGroupDescription;

    private RealmList<User> users;
    private RealmList<UserAffair> affairs;

    public UserGroup() {
        this.userGroupId = -1;
        this.userGroupName = "userGroupName";
        this.userGroupDescription = "userGroupDescription";
        this.users = new RealmList<>();
        this.affairs = new RealmList<>();
    }

    public UserGroup(long userGroupId, String userGroupName, String userGroupDescription,
                     RealmList<User> users, RealmList<UserAffair> userAffairs) {
        this.userGroupId = userGroupId;
        this.userGroupName = userGroupName;
        this.userGroupDescription = userGroupDescription;
        this.users = new RealmList<>();
        this.affairs = new RealmList<>();
    }

    public static void delete(Realm realm, long id) {
        UserGroup userGroup = realm.where(UserGroup.class).equalTo("id", id).findFirst();

        if (userGroup != null) {
            userGroup.deleteFromRealm();
        }
    }

    public long getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(long userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    public String getUserGroupDescription() {
        return userGroupDescription;
    }

    public void setUserGroupDescription(String userGroupDescription) {
        this.userGroupDescription = userGroupDescription;
    }

    public RealmList<User> getUsers() {
        return users;
    }

    public void setUsers(RealmList<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }

    public void removeAllUsers() {
        this.users.clear();
    }

    public RealmList<UserAffair> getAffairs() {
        return affairs;
    }

    public void setAffairs(RealmList<UserAffair> affairs) {
        this.affairs = affairs;
    }

    public void addAffair(UserAffair userAffair) {
        this.affairs.add(userAffair);
    }

    public void removeAffair(UserAffair userAffair) {
        this.affairs.remove(userAffair);
    }

    public void removeAllAffairs() {
        this.affairs.clear();
    }
}
