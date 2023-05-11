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
    private List<String> cids;

    public User(String uid, String email, String username, String permission, String cid) {
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.permission = permission;
        this.cids = new ArrayList<>();
        String msg = "DEBUG: ";
        if (permission.equals(CLIENT)) {
            Log.d(msg, "client CID");
            cids.add(cid);
        }
        else if (permission.equals(SERVICE_PROVIDER) || permission.equals(RESOURCES_ENTITY)) {
            Log.d(msg, "random CID");
            cids.add(UUID.randomUUID().toString());
        }
        else{
            cids.add("NA");
        }
        Log.d(msg, "END of User()");
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
