package com.dissotti.smiledetector;

import com.google.gson.annotations.SerializedName;

class ServerResponse {

    // variable name should be same as in the json response from php
    @SerializedName("joy")
    boolean joy;
    @SerializedName("sorrow")
    boolean sorrow;

    @SerializedName("detectionConfidence")
    String detectionConfidence;

    public boolean getJoy() {
        return joy;
    }

    public boolean getSorrow() {
        return sorrow;
    }

    public String getDetectionConfidence() {
        return detectionConfidence;
    }
}
