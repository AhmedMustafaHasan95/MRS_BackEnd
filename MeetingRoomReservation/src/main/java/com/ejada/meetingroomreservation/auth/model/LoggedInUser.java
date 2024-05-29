package com.ejada.meetingroomreservation.auth.model;

import com.ejada.meetingroomreservation.entity.User;

import java.util.List;

public class LoggedInUser {

    private User user;
    private List<String> permissions;

    public LoggedInUser() {
    }

    public LoggedInUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

}
