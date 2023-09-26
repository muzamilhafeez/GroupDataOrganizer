package com.example.copytotext.Api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClientResponseString {
    private static RetrofitClientResponseString instance = null;
    public static int TimeSet = 10;
    private Api myApi;


    OkHttpClient RequestTimeSet = new OkHttpClient.Builder()
            .connectTimeout(TimeSet, TimeUnit.SECONDS)
            .readTimeout(TimeSet, TimeUnit.SECONDS)
            .build();
    private RetrofitClientResponseString() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL).client(RequestTimeSet)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        myApi = retrofit.create(Api.class);
    }

    public static synchronized RetrofitClientResponseString getInstance() {
        if(TimeSet==10) {
            if (instance == null) {
                instance = new RetrofitClientResponseString();
            }
            return instance;
        }else{
            instance=null;
            if (instance == null) {
                instance = new RetrofitClientResponseString();
                TimeSet=10;
            }
            return instance;
        }
    }
    public Api getMyApi(){
        return myApi;
    }
}
