package com.example.scanclothes.RealityCapture;

import com.example.scanclothes.Retrofit.Model;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {
    @GET("fact")
    Call<Model> getModel();
}
