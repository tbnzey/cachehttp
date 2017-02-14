package com.example.maze.cachehttp.Data.http;

import android.util.Log;

import com.example.maze.cachehttp.Application.BaseApplication;
import com.example.maze.cachehttp.Common.Constant;
import com.example.maze.cachehttp.Data.http.cookies.CookiesManager;
import com.example.maze.cachehttp.Entity.BaseDto;
import com.example.maze.cachehttp.Entity.CacheDto;
import com.example.maze.cachehttp.Data.http.converterfactory.StringConverter;
import com.example.maze.cachehttp.Data.http.service.Service;
import com.example.maze.cachehttp.Utils.Tools;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by zhouwei on 16/11/9.
 */

public class RetrofitServiceManager {
    private static final int DEFAULT_TIME_OUT = 5;//超时时间 5s
    private static final int DEFAULT_READ_TIME_OUT = 10;
    private Retrofit mRetrofit;

    private OkHttpClient buildClinet(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)//连接超时时间
                .writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)//写操作 超时时间
                .readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)//读操作超时时间
                .cookieJar(new CookiesManager())
                .build();
        return okHttpClient;
    }

    private RetrofitServiceManager(){
        // 添加公共参数拦截器
        /*
        HttpCommonInterceptor commonInterceptor = new HttpCommonInterceptor.Builder()
                .addHeaderParams("","")
                .build();
        builder.addInterceptor(commonInterceptor);
        */

        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .client(buildClinet())
                .addConverterFactory(StringConverter.StringConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constant.API_SERVER)
                .build();
    }

    private static class SingletonHolder{
        private static final RetrofitServiceManager INSTANCE = new RetrofitServiceManager();
    }

    /**
     * 获取RetrofitServiceManager
     * @return
     */
    public static RetrofitServiceManager getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取对应的Service
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service){
        return mRetrofit.create(service);
    }

    public static class Build{

        private boolean isReadCache = true;// true 优先读取缓存
        private boolean isSaveCache = true;// true 开启缓存存储
        private String url;
        private Map map;
        private IRetrofitHttp retrofitHttp;

        public boolean isReadCache() {
            return isReadCache;
        }

        public Build noReadCache(){
            isReadCache = false;
            return this;
        }

        public Build setReadCache(boolean readCache) {
            isReadCache = readCache;
            return this;
        }

        public boolean isSaveCache() {
            return isSaveCache;
        }

        public Build noSaveCache(){
            isSaveCache = false;
            return this;
        }

        public Build setSaveCache(boolean saveCache) {
            isSaveCache = saveCache;
            return this;
        }

        public Build noRSCache(){
            isReadCache = false;
            isSaveCache = false;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public Build setUrl(String url) {
            this.url = url;
            return this;
        }

        public Build setUrl(int url) {
            this.url = BaseApplication.getRs().getString(url);
            return this;
        }

        public Map getMap() {
            return map;
        }

        public Build setMap(Map map) {
            this.map = map;
            return this;
        }

        public IRetrofitHttp getRetrofitHttp() {
            return retrofitHttp;
        }

        public Build setRetrofitHttp(IRetrofitHttp retrofitHttp) {
            this.retrofitHttp = retrofitHttp;
            return this;
        }

        public void create(){
            getInstance().subscribe(this);
        }
    }

    public static void subscribe(Build build){
        subscribe(build.isReadCache(), build.isSaveCache(), build.getUrl(), build.getMap(), build.getRetrofitHttp());
    }

    public static void subscribe(final boolean isReadCache, final boolean isSaveCache, final String url, final Map map, final IRetrofitHttp retrofitHttp){
        String mapStr = map == null ? "" : "_" + Tools.map2Str(map);
        final String whereParms = url + mapStr;
        CacheDto dto = CacheDto.find(whereParms);
        Observable retrofitObservable = map == null ?
                getInstance().create(Service.class).postPath(url) : getInstance().create(Service.class).postPath(url, map);
        Observable observable = isReadCache ? // 判断是否优先读取缓存 true 判断保鲜期
                dto != null && dto.isOverdue() ? // 判断是否过期 true 读取缓存 false 请求网络数据
                        dto.ObservableStr() : retrofitObservable
                : retrofitObservable;
        if(retrofitHttp != null) {
            observable
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String t) {
                            BaseDto baseBean = new BaseDto(t);
                            if(isSaveCache){// && baseBean.isCode()){
                                CacheDto.save(whereParms, t);
                            }
                            retrofitHttp.onSuccess(baseBean);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            retrofitHttp.onThrowable(throwable);
                        }
                    }, new Action0() {
                        @Override
                        public void call() {
                            retrofitHttp.onFinish();
                        }
                    });
        } else {
            Log.i("TAGSS", "retrofitHttp is NULL");
        }
    }
}
