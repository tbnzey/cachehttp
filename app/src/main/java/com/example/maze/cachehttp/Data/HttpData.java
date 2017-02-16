package com.example.maze.cachehttp.Data;

import android.util.Log;

import com.example.maze.cachehttp.Application.BaseApplication;
import com.example.maze.cachehttp.Data.retrofit.BaseResponse;
import com.example.maze.cachehttp.Data.retrofit.BaseRetrofit;
import com.example.maze.cachehttp.Data.retrofit.IRetrofitHttp;
import com.example.maze.cachehttp.Entity.BaseDto;
import com.example.maze.cachehttp.Entity.CacheDto;
import com.example.maze.cachehttp.Data.retrofit.service.Service;
import com.example.maze.cachehttp.Utils.Tools;

import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by zhouwei on 16/11/9.
 */

public class HttpData extends BaseRetrofit {

    protected static final Service service = getRetrofit().create(Service.class);

    private static class SingletonHolder{
        private static final HttpData INSTANCE = new HttpData();
    }

    public static HttpData getInstance(){
        return SingletonHolder.INSTANCE;
    }

    //********************************************* Build
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

    public static void subscribe(final boolean isReadCache, final boolean isSaveCache,
                                    final String url, final Map map, final IRetrofitHttp<String> retrofitHttp){
        String mapStr = map == null ? "" : "_" + Tools.map2Str(map);
        final String whereParms = url + mapStr;
        CacheDto dto = CacheDto.find(whereParms);
        Observable<BaseResponse<String>> retrofitObservable = map == null ? service.postPath2(url) : service.postPath2(url, map);
        Observable<BaseResponse<String>> observable = isReadCache ? // 判断是否优先读取缓存 true 判断保鲜期
                dto != null && dto.isOverdue() ? // 判断是否过期 true 读取缓存 false 请求网络数据
                        dto.<BaseResponse<String>>ObservableStr() : retrofitObservable
                : retrofitObservable;
        if(retrofitHttp != null) {
            observable
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<BaseResponse<String>>() {
                        @Override
                        public void call(BaseResponse<String> t) {
                            if(isSaveCache && t.isOk()){
                                CacheDto.save(whereParms, t.toString());
                                retrofitHttp.onSuccess(t.getData());
                            } else {

                            }
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
