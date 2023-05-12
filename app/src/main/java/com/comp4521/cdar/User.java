package com.comp4521.cdar;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class User {
    // Constants for the three roles
    public static final String CLIENT = "Client";
    public static final String RESOURCES_ENTITY = "ResourcesEntity";
    public static final String SERVICE_PROVIDER = "ServiceProvider";
    private String uid;
    private String email;
    private String username;
    private String permission;

    public User(String uid, String email, String permission) {
        this.uid = uid;
        this.email = email;
        this.username = email.split("@")[0];
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
