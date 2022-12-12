package com.example.scanclothes.RealityCapture;

import com.google.gson.annotations.SerializedName;

public class ReqBodyID {
    @SerializedName("photosceneid")
    private String photosceneid;


    public ReqBodyID(String photosceneid) {
        this.photosceneid = photosceneid;
    }

    public String getPhotosceneid() {
        return photosceneid;
    }

    public void setPhotosceneid(String photosceneid) {
        this.photosceneid = photosceneid;
    }
}
