package com.example.maze.cachehttp.Data.retrofit;
/**
 * Created by maze on 2017/2/14.
 */

import com.example.maze.cachehttp.Common.Constant;
import com.example.maze.cachehttp.Data.retrofit.converterfactory.StringConverter;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * >>>>>>>>>>>>>>>>>>>
 * 作者：蜗牛
 * 日期：2017/2/14
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>类描述
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>属性描述
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>方法描述
 */
public abstract class BaseRetrofit {

    protected static Retrofit mRetrofit;
    protected static OkHttpClient mOkHttpClient;

    /**
     * 获取对应的Service
     * @param service Service 的 class
     * @param <E>
     * @return
     */
    public <E> E create(Class<E> service){
        return getRetrofit().create(service);
    }

    /**
     * 获取Retrofit对象
     * @return
     */
    protected static Retrofit getRetrofit() {
        if (null == mRetrofit) {
            if (null == mOkHttpClient) {
                mOkHttpClient = OkHttpClient3.getOkHttpClient();
            }
            // 添加公共参数拦截器
            /*
            HttpCommonInterceptor commonInterceptor = new HttpCommonInterceptor.Builder()
                    .addHeaderParams("","")
                    .build();
            builder.addInterceptor(commonInterceptor);
            */
            // 创建Retrofit
            mRetrofit = new Retrofit.Builder()
                    //设置服务器路径
                    .baseUrl(Constant.API_SERVER)
                    //设置使用okhttp网络请求
                    .client(OkHttpClient3.getOkHttpClient())
                    .addConverterFactory(StringConverter.StringConverterFactory.create())
                    //添加转化库，默认是Gson
//                .addConverterFactory(GsonConverterFactory.create())
                    //添加回调库，采用RxJava
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return mRetrofit;
    }
}
