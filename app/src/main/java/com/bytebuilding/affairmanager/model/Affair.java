package com.bytebuilding.affairmanager.model;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.utils.DateUtils;

public class Affair implements Item {

    public static final int STATUS_OVERDUE = 0;
    public static final int STATUS_CURRENT = 1;
    public static final int STATUS_DONE = 2;

    private String title;
    private String description;
    private long date;
    private long time;
    private int priority;
    private String object;
    private String type;
    private String place;
    private long repeatTimestamp;
    private long timestamp;
    private int status;
    private int affairDateStatus;

    public Affair() {
    }

    public Affair(String title, String description, long date, long time, int priority,
                  String object, String type, String place, long repeatTimestamp, long timestamp,
                  int status) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.priority = priority;
        this.object = object;
        this.type = type;
        this.place = place;
        this.repeatTimestamp = repeatTimestamp;
        this.timestamp = timestamp;
        this.status = status;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getColor() {
        switch (getPriority()) {
            case 2:
                if (getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE) {
                    return R.color.md_red_400;
                } else {
                    return R.color.md_red_800;
                }
            case 1:
                if (getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE) {
                    return R.color.md_orange_400;
                } else {
                    return R.color.md_orange_800;
                }
            case 0:
                if (getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE) {
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

    public int getAffairDateStatus() {
        return affairDateStatus;
    }

    public void setAffairDateStatus(int affairDateStatus) {
        this.affairDateStatus = affairDateStatus;
    }

    @Override
    public boolean isAffair() {
        return true;
    }
}
