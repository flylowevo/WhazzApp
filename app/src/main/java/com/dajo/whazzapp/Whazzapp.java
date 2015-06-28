package com.dajo.whazzapp;

import android.app.Application;

import com.shephertz.app42.paas.sdk.android.App42API;

public class Whazzapp extends Application {
    private static final String API_KEY = "d81bcbf6ffb4f8f72afbfa7bc44aed09a64d0f472e3a1a39132bab3f465033ed";
    private static final String SECRET_KEY = "20878b86bbeb3132a4dd1b1c7e5de50f587eeb54298523f234bb8f6b9adeec4a";

    public void onCreate() {
        App42API.initialize(this, API_KEY, SECRET_KEY);
    }
}
