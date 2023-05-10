package com.comp4521.cdar;

public class User {
    // Constants for the three roles
    public static final String CLIENT = "client";
    public static final String RESOURCES_ENTITY = "resources entity";
    public static final String SERVICE_PROVIDER = "service provider";
    private String uid;
    private String email;
    private String username;
    private String permission;

    public User(String uid, String email, String username, String permission) {
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.permission = permission;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
