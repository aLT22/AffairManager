package com.bytebuilding.affairmanager.model.online;

/**
 * Created by Turkin A. on 15.05.17.
 */

public class Coworker {

    private String username;

    private String job;

    public Coworker() {
    }

    public Coworker(String username, String job) {
        this.username = username;
        this.job = job;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
