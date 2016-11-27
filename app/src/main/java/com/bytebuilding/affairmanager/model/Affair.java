package com.bytebuilding.affairmanager.model;

/**
 * Created by Alexey on 07.11.2016.
 */

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

    @Override
    public boolean isAffair() {
        return true;
    }
}
