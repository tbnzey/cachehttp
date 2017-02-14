package com.example.maze.cachehttp.Utils;
/**
 * Created by maze on 2017/2/14.
 */

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.maze.cachehttp.Application.BaseApplication;

import java.util.Iterator;
import java.util.Map;

/**
 * >>>>>>>>>>>>>>>>>>>
 * 作者：蜗牛
 * 日期：2017/2/14
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>类描述
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>属性描述
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>方法描述
 */
public class Tools {

    /**
     * str == null 或者 "" 为 false
     */
    public static boolean notBlank(String str) {
        return str == null || str.trim().length() == 0 ? false : true;
    }

    /**
     * 检测网络连接
     * @return
     */
    public static boolean isConnection() {
        boolean isConnection;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) BaseApplication.getAppContext().getSystemService(
                        BaseApplication.getAppContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnection = networkInfo != null && networkInfo.isAvailable();
        return isConnection;
    }

    public static String map2Str(Map map) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = entry.getKey().toString();
                String val = entry.getValue() != null ? entry.getValue().toString() : "";
                stringBuffer.append(key + "/" + val + "//");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }
}
