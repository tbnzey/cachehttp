package com.example.maze.cachehttp.Data.retrofit.service;

import com.example.maze.cachehttp.Data.retrofit.BaseResponse;
import com.example.maze.cachehttp.Entity.BaseDto;

import java.util.Map;

import okhttp3.internal.http.RealResponseBody;
import retrofit2.Call;
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

public interface Service<T> {

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
    Observable<BaseDto> postPath(@Path("path") String path);

    @FormUrlEncoded
    @POST("{path}")
    Observable<BaseDto> postPath(@Path("path") String path, @FieldMap Map<String, Object> map);

    @POST("{path}")
     Observable<BaseResponse<String>> postPath2(@Path("path") String path);

    @FormUrlEncoded
    @POST("{path}")
     Observable<BaseResponse<String>> postPath2(@Path("path") String path, @FieldMap Map<String, Object> map);


//    @POST("{path}")
//    Call<T> postPath2(@Path("path") String path);
//
//    @FormUrlEncoded
//    @POST("{path}")
//    Call<T> postPath2(@Path("path") String path, @FieldMap Map<String, Object> map);

    //*******  提交多参数表单数据（multi-part form data），可以使用@Multipart与@Part注解
//    @Multipart
//    @POST("/some/endpoint")
//    Call<RealResponseBody> someEndpoint(@Part("name1") String name1, @Part("name2") String name2)
}
