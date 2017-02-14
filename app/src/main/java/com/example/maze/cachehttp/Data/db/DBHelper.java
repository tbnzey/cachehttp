package com.example.maze.cachehttp.Data.db;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by maze on 2016/12/27.
 * 常见的条件如下（详细资料请查官方文档）：
 * between(), greaterThan(), lessThan(), greaterThanOrEqualTo() & lessThanOrEqualTo()
 * equalTo() & notEqualTo()
 * contains(), beginsWith() & endsWith()
 * isNull() & isNotNull()
 * isEmpty() & isNotEmpty()
 */

public class DBHelper {

    public static <T extends RealmObject> void save(final T t){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(t);
        realm.commitTransaction();
        realm.close();
    }

    /**
     * 删除第一个数据
     * @param realmResults
     */
    public static <T extends RealmObject> void deleteFromRealm(
            final RealmResults<T> realmResults, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                T t = realmResults.get(5);
                t.deleteFromRealm();
            }
        }, onSuccess, onError);
    }

    public static void delAll(){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }

    /**
     * 删除最后一个数据
     * @param realmResults
     */
    public static <T extends RealmObject> void deleteFirstFromRealm(
            final RealmResults<T> realmResults, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmResults.deleteFirstFromRealm();
            }
        }, onSuccess, onError);
    }

    /**
     * 删除位置为index的数据
     * @param realmResults
     * @param index
     */
    public static <T extends RealmObject> void deleteFromRealm(
            final RealmResults<T> realmResults, final int index, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmResults.deleteFromRealm(index);
            }
        }, onSuccess, onError);
    }

    /**
     * 删除所有数据
     * @param realmResults
     */
    public static <T extends RealmObject> void deleteAllFromRealm(
            final RealmResults<T> realmResults, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmResults.deleteAllFromRealm();
            }
        }, onSuccess, onError);
    }

    /**
     * 更新先有数据或者插入新数据
     * @param t
     */
    public static <T extends RealmObject>  void update(
            final T t, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(t);
                realm.close();
            }
        }, onSuccess, onError);
    }

    /**
     * 查询指定class全部数据
     * @param t
     * @return
     */
    public static <T extends RealmObject>  List<T> findAll(T t){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<T> findParms = (RealmResults<T>) realm.where(t.getClass()).findAll();
        List<T> dto = null;
        if(findParms != null) dto = realm.copyFromRealm(findParms);
        realm.close();
        return dto;
    }

    /**
     * 查询指定class 单个条件数据
     * @param t
     * @param key
     * @param parms
     * @return
     */
    public static <T extends RealmObject>  T find(T t, String key, String parms){
        Realm realm = Realm.getDefaultInstance();
        T first = (T) realm.where(t.getClass()).equalTo(key, parms).findFirst();
        T dto = null;
        if(first != null) dto = Realm.getDefaultInstance().copyFromRealm(first);
        realm.close();
        return dto;
    }
}