package com.example.puzzle.utils;

import com.example.puzzle.model.LoginBean;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2018/7/2 0002.
 */

public class HttpUtils {
    public interface Myapi {
        @FormUrlEncoded
        @POST("user")
        @Headers("Content-Type:application/x-www-form-urlencoded")
        Observable<LoginBean> login(@Field("username")String username, @Field("passwd")String password, @Field("action")String action);

        @FormUrlEncoded
        @POST("user")
        @Headers("Content-Type:application/x-www-form-urlencoded")
        Observable<LoginBean> register(@Field("username")String username, @Field("passwd")String password, @Field("action")String action);
    }

    public static Retrofit getRetrofit() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .readTimeout(3, TimeUnit.MINUTES)
                .connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://xiaomi388.cn:18080/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
