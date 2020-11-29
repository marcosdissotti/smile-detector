package com.dissotti.smiledetector;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class AppConfig {


    static Retrofit getRetrofit() {

        return new Retrofit.Builder()
                .baseUrl("http://192.168.0.10:3333/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
