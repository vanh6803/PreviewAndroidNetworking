package com.example.review2.api;

import com.example.review2.models.Comic;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    String BASE_URL = "https://64d0dddbff953154bb799939.mockapi.io/";

    ApiService instance = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService.class);

    @GET("comic")
    Call<List<Comic>> getAllComic();

    @POST("comic")
    Call<Comic> addComic(@Body Comic comic);

    @PUT("comic/{id}")
    Call<Comic> editComic(@Path("id") String id, @Body Comic comic);

    @DELETE("comic/{id}")
    Call<Void> deleteComic(@Path("id") String id);

}
