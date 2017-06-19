package com.yomii.http_retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yomii.http_retrofit.cookie.CookieJarImpl;
import com.yomii.http_retrofit.cookie.store.DBCookieStore;

import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 维护Retrofit
 * Created by Yomii on 2017/3/21.
 */

public class RetrofitManager {

    private static final String URL_DOUBAN = "https://api.douban.com/v2/";
    private static final String URL_GITHUB = "https://api.github.com/";

    private static class SingletonHolder{
        private static final RetrofitManager INSTANCE = new RetrofitManager();
    }

    public static RetrofitManager getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private RetrofitManager(){
        generateRetrofit();
    }

    private Context fakeContext;

    private Retrofit doubanRetro;
    private Retrofit githubRetro;

    private Retrofit.Builder generateCommonRetrofitBuilder() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // http log
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC));
        }
        // timeout
        builder.connectTimeout(20, TimeUnit.SECONDS);

        // 使用数据库保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(fakeContext)));

        OkHttpClient client = builder.build();
        //gson client
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        return new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson));
    }

    private void generateRetrofit(){
        Retrofit.Builder builder = generateCommonRetrofitBuilder();
        doubanRetro = builder.baseUrl(URL_DOUBAN).build();
        githubRetro = builder.baseUrl(URL_GITHUB).build();
    }

    public Retrofit getDoubanRetro() {
        return doubanRetro;
    }

    public Retrofit getGithubRetro() {
        return githubRetro;
    }
}
