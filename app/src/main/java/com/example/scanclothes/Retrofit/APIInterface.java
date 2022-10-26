package com.example.scanclothes.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {
    @GET("fact")
    Call<Model> getModel();
}
