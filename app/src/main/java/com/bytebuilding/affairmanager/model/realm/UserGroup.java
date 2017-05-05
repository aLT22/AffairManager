package com.bytebuilding.affairmanager.model.realm;

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

    public UserGroup() {
        this.userGroupId = -1;
        this.userGroupName = "userGroupName";
        this.userGroupDescription = "userGroupDescription";
    }

    public UserGroup(long userGroupId, String userGroupName, String userGroupDescription,
                     RealmList<User> users, RealmList<UserAffair> userAffairs) {
        this.userGroupId = userGroupId;
        this.userGroupName = userGroupName;
        this.userGroupDescription = userGroupDescription;
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
}
