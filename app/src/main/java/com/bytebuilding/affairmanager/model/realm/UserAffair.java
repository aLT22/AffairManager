package com.bytebuilding.affairmanager.model.realm;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.model.Affair;
import com.bytebuilding.affairmanager.utils.DateUtils;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by atlas on 23.03.17.
 */

public class UserAffair extends RealmObject {

    @PrimaryKey
    private long timestamp;

    private String title;
    private String description;
    private long date;
    private long time;
    private int priority;
    private String object;
    private String type;
    private String place;
    private long repeatTimestamp;
    private long status;

    private long userGroupId;

    private long userId;

    public UserAffair() {
    }

    public UserAffair(long timestamp, String title, String description, long date,
                      long time, int priority, String object, String type,
                      String place, long repeatTimestamp, int status, long userGroupId, long userId) {
        this.timestamp = timestamp;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.priority = priority;
        this.object = object;
        this.type = type;
        this.place = place;
        this.repeatTimestamp = repeatTimestamp;
        this.status = status;
        this.userGroupId = userGroupId;
        this.userId = userId;
    }

    public static void create(Realm realm, UserAffair userAffair) {
        realm.createObject(UserAffair.class, userAffair.getTimestamp());
    }

    public static void delete(Realm realm, long id) {
        UserAffair userAffair = realm.where(UserAffair.class).equalTo("id", id).findFirst();

        if (userAffair != null) {
            userAffair.deleteFromRealm();
        }
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public long getRepeatTimestamp() {
        return repeatTimestamp;
    }

    public void setRepeatTimestamp(long repeatTimestamp) {
        this.repeatTimestamp = repeatTimestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public int getColor() {
        switch (getPriority()) {
            case 2:
                if (getStatus() == Affair.STATUS_CURRENT || getStatus() == Affair.STATUS_OVERDUE) {
                    return R.color.md_red_400;
                } else {
                    return R.color.md_red_800;
                }
            case 1:
                if (getStatus() == Affair.STATUS_CURRENT || getStatus() == Affair.STATUS_OVERDUE) {
                    return R.color.md_orange_400;
                } else {
                    return R.color.md_orange_800;
                }
            case 0:
                if (getStatus() == Affair.STATUS_CURRENT || getStatus() == Affair.STATUS_OVERDUE) {
                    return R.color.md_green_400;
                } else {
                    return R.color.md_green_800;
                }
            default:
                return 0;
        }
    }

    public String getFullDate() {
        return DateUtils.getFullDate(this.timestamp);
    }

    public long getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(long userGroupId) {
        this.userGroupId = userGroupId;
    }
}
