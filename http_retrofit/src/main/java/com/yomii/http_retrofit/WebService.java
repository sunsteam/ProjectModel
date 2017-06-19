package com.yomii.http_retrofit;

import com.yomii.base.bean.ListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Yomii on 2017/6/19.
 */

public interface WebService {
    /**
     * 获取正在上映电影
     *
     * @param count 单页条数
     * @param start 初始索引
     */
    @GET("movie/in_theaters")
    Call<ListResponse<Object>> getCurrentMovies(
            @Query("count") int count,
            @Query("start") int start);
}
