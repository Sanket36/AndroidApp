package com.example.securehomes;

import android.app.Application;

public class SecureHomes extends Application {

    private static String flat;

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }
}
