package com.example.maze.cachehttp.Entity;

import com.example.maze.cachehttp.Application.BaseApplication;
import com.example.maze.cachehttp.Data.db.DBHelper;
import com.example.maze.cachehttp.Data.retrofit.BaseResponse;
import com.example.maze.cachehttp.Utils.Tools;

import java.util.Map;

import io.realm.Realm;
import io.realm.RealmObject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by maze on 2016/12/28.
 */

public class CacheDto extends RealmObject {

    private String key;
    private String value;
    private long duration = 0;// 过期时间

    public static long MINUTES = 60 * 1000;
    public static long HOURS = 60 * MINUTES;
    public static long DAY = 24 * HOURS;
    public static long DEFAULTDURATION = HOURS;

    public static void  save(String key, Map map, String value){
        save(key, map, value, DEFAULTDURATION);
    }

    public static void  save(String key, String value){
        save(key, value, DEFAULTDURATION);
    }

    public static void  save(String key, Map map, String value, long duration){
        save(key + "_" + Tools.map2Str(map), value, duration);
    }

    public static void  save(String key, String value, long duration) {
        CacheDto dto = new CacheDto();
        dto.setKey(key);
        dto.setValue(value);
        dto.setDuration(duration);
        DBHelper.save(dto);
    }

    public static CacheDto find(int parms){
        return find(BaseApplication.getRs().getString(parms));
    }

    public static CacheDto find(int parms, Map map){
        return find(BaseApplication.getRs().getString(parms) + "_" + Tools.map2Str(map));
    }

    public static CacheDto find(String parms, Map map){
        return find(parms + "_" + Tools.map2Str(map));
    }

    /**
     * 查询缓存数据
     * @param parms
     * @return
     */
    public static CacheDto find(String parms){
//        Log.i("TAGSS", "s time : " + System.currentTimeMillis());
        Realm realm = Realm.getDefaultInstance();
        CacheDto first = realm.where(CacheDto.class).equalTo("key", parms).findFirst();
        CacheDto dto = null;
        if(first != null) dto = Realm.getDefaultInstance().copyFromRealm(first);
        realm.close();
//        Log.i("TAGSS", "e time : " + System.currentTimeMillis());
        return dto;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public long getDuration() {
        return duration;
    }

    public CacheDto setKey(String key) {
        this.key = key;
        return this;
    }

    public CacheDto setValue(String value) {
        this.value = value;
        return this;
    }

    public CacheDto setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    /**
     * 判断是否过期
     * @return
     */
    public boolean isOverdue(){
        if(!Tools.isConnection() || (Long.valueOf(duration) - System.currentTimeMillis()) >= 0 && Tools.notBlank(getValue())){
            return true;
        } else {
            return false;
        }
    }

    public Observable<BaseResponse<String>> ObservableStr(){
//        return Observable.just(getValue());
        return null;
    }

    public <T> Observable<T> Observable(){
        return (Observable<T>) Observable.just(getValue())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}