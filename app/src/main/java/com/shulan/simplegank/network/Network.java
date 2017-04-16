package com.shulan.simplegank.network;

import com.shulan.simplegank.model.Config;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by houna on 17/4/15.
 */
public class Network {

    public static Retrofit getManager() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        // 目前log 是 debug OkHttp 就可以看了
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Config.ZHIHU_API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }


}
