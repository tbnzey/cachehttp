package com.example.maze.cachehttp.Data.retrofit.service;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2017/1/16.
 *
 * ResponseBody 基础类型
 *
 * ：@Query或QueryMap将参数拼接在url后面
 * @Query //少数参数
 * @QueryMap //参数较多
 *
 * ：@Field或@FieldMap传递的参数时放在请求体
 * @Field //少数参数
 * @FieldMap //参数较多
 *
 * @FormUrlEncoded //必须带参数(方法注解)
 * @Path //路径拼接(path对应的路径不能包含”/”，否则会将其转化为%2F)
 * @Url //不同域网址(当@GET或@POST注解的url为全路径时（可能和baseUrl不是一个域），会直接使用注解的url的域)
 */

public interface Service {

    @FormUrlEncoded
    @POST("app/cmd/list.do")
    Observable<String> getRecommend(@Field("pageNo") Integer page);

    @POST("auction/{id}/city.do")
    Observable<String> getCity(@Path("id") int id);

    //无参数
    @POST("users")
    Observable<String> listRepos();

    //少数参数
    @FormUrlEncoded
    @POST("users")
    Observable<String> listRepos(@Field("time") long time);

    //参数较多
    @FormUrlEncoded
    @POST("users")
    Observable<String> listRepos(@FieldMap Map<String, Object> parms);

    @POST("{path}")
    Observable<String> postPath(@Path("path") String path);

    @FormUrlEncoded
    @POST("{path}")
    Observable<String> postPath(@Path("path") String path, @FieldMap Map<String, Object> map);

}
