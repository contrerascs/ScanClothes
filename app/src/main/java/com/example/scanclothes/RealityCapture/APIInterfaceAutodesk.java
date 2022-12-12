package com.example.scanclothes.RealityCapture;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIInterfaceAutodesk {

    @GET("/api/forge/oauth")
    Call<Capture> getOauth();

    @GET("/api/forge/recap/photoscene/add")
    Call<Capture> getPhotosceneAdd();


    @POST("/api/forge/recap/photoscene/upload")
    Call<ReqBody> getPhotosceneID(@Body ReqBodyID reqBodyID);

    @POST("/api/forge/recap/photoscene/process")
    Call<ReqBody> getProcess(@Body ReqBodyID reqBodyID);

    @POST("/api/forge/recap/photoscene/checkprogress")
    Call<ReqBody> getCheckProgress(@Body ReqBodyID reqBodyID);

    @POST("/api/forge/recap/photoscene/result")
    Call<ReqBody> getResult(@Body ReqBodyID reqBodyID);
}

