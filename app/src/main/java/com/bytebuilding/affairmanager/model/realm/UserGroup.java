package com.bytebuilding.affairmanager.model.realm;

import java.util.ArrayList;
import java.util.List;

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

    private List<Long> userIds;
    private List<Long> affairIds;

    public UserGroup() {
        this.userGroupId = -1;
        this.userGroupName = "userGroupName";
        this.userGroupDescription = "userGroupDescription";
        this.userIds = new ArrayList<>();
        this.affairIds = new ArrayList<>();
    }

    public UserGroup(long userGroupId, String userGroupName, String userGroupDescription,
                     RealmList<User> users, RealmList<UserAffair> userAffairs) {
        this.userGroupId = userGroupId;
        this.userGroupName = userGroupName;
        this.userGroupDescription = userGroupDescription;
        this.userIds = new ArrayList<>();
        this.affairIds = new ArrayList<>();
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

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public void addUser(long identifier) {
        this.userIds.add(identifier);
    }

    public void removeUser(long identifier) {
        this.userIds.remove(identifier);
    }

    public void removeAllUsers() {
        this.userIds.clear();
    }

    public List<Long> getAffairIds() {
        return affairIds;
    }

    public void setAffairIds(List<Long> affairIds) {
        this.affairIds = affairIds;
    }

    public void addAffair(long identifier) {
        this.affairIds.add(identifier);
    }

    public void removeAffair(long identifier) {
        this.affairIds.remove(identifier);
    }

    public void removeAllAffairs() {
        this.affairIds.clear();
    }
}
