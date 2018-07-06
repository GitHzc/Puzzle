package com.example.puzzle.utils;

import com.example.puzzle.PuzzleApplication;
import com.example.puzzle.model.HistoryBean;
import com.example.puzzle.model.LoginBean;
import com.example.puzzle.model.LogoutBean;
import com.example.puzzle.model.RankBean;
import com.example.puzzle.model.SubmitScoreBean;
import com.example.puzzle.model.UserInfoBean;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2018/7/2 0002.
 */

public class HttpUtils {
    public interface Myapi {
        @FormUrlEncoded
        @POST("user")
        @Headers("Content-Type:application/x-www-form-urlencoded")
        Observable<Response<LoginBean>> login(@Field("username")String username, @Field("passwd")String password, @Field("action")String action);

        @FormUrlEncoded
        @POST("user")
        @Headers("Content-Type:application/x-www-form-urlencoded")
        Observable<LoginBean> register(@Field("username")String username, @Field("passwd")String password, @Field("action")String action);

        @FormUrlEncoded
        @POST("user")
        @Headers("Content-Type:application/x-www-form-urlencoded")
        Observable<LogoutBean> logout(@Field("action")String action);

        @GET("record") //请求所有战绩
        Observable<RankBean> getAllRecord();

        @GET("record") //请求Mode对应战绩
        @Headers("Content-Type:application/x-www-form-urlencoded")
        Observable<RankBean> getRecordWithMode(@Query("mode")String mode);

        @GET("user/record") //请求用户所有历史战绩
        Observable<HistoryBean> getAllHistory(@Header("Cookie") String cookie);

        @GET("user/record") //请求用户Mode对应战绩
        @Headers("Content-Type:application/x-www-form-urlencoded")
        Observable<HistoryBean> getHistoryWithMode(@Header("Cookie") String cookie, @Query("mode")String mode);

        @GET("user/info") //获取用户信息 rank username
        @Headers("Content-Type:application/x-www-form-urlencoded")
        Observable<UserInfoBean> getUserInfo(@Header("Cookie") String cookie);

        @POST("record")
        @FormUrlEncoded
        @Headers("Content-Type:application/x-www-form-urlencoded")
        Observable<SubmitScoreBean> submit(@Header("Cookie")String cookie, @Field("score")int score, @Field("time")String time, @Field("mode")int level);
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
