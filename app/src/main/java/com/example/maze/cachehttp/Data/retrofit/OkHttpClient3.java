package com.example.maze.cachehttp.Data.retrofit;
/**
 * Created by maze on 2017/2/14.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.maze.cachehttp.Application.BaseApplication;
import com.example.maze.cachehttp.Data.retrofit.cookies.CookiesManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * >>>>>>>>>>>>>>>>>>>
 * 作者：蜗牛
 * 日期：2017/2/14
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>类描述
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>属性描述
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>方法描述
 */
public class OkHttpClient3 {

    private static final int DEFAULT_TIME_OUT = 10;
    private static final int DEFAULT_WRITE_TIME_OUT = 10;
    private static final int DEFAULT_READ_TIME_OUT = 10;
    private static OkHttpClient mOkHttpClient;

    //设置缓存目录
//    private static File cacheDirectory = new File(BaseApplication.getAppContext().getExternalCacheDir(), "MyCache");
//    private static Cache cache = new Cache(cacheDirectory, 100 * 1024 * 1024);


    /**
     * 获取OkHttpClient对象
     */
    public static OkHttpClient getOkHttpClient() {
        if (null == mOkHttpClient) {
            //同样okhttp3后也使用build设计模式
            mOkHttpClient = new OkHttpClient.Builder()
                    //设置一个自动管理cookies的管理器
                    .cookieJar(new CookiesManager())
                    .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)//连接超时时间
                    .writeTimeout(DEFAULT_WRITE_TIME_OUT, TimeUnit.SECONDS)//写操作超时时间
                    .readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)//读操作超时时间
                    //添加拦截器
                    .addInterceptor(mTokenInterceptor)
//                    .addNetworkInterceptor(TestInterceptor)
//                    .addInterceptor(TestInterceptor)
                    //添加网络连接器
//                    .addNetworkInterceptor(new CookiesInterceptor(BaseApplication.getAppContext()))
//                    .cache(cache)
                    .build();
        }
        return mOkHttpClient;
    }
    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private static final Interceptor TestInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if(!isNetworkReachable(BaseApplication.getAppContext())){
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if(isNetworkReachable(BaseApplication.getAppContext())){
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            }else{
                int maxStale = 60 * 60 * 24 * 365; // 无网络时间
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale="+maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };
    /**
     * 云端响应头拦截器
     * 用于添加统一请求头  请按照自己的需求添加
     */
    private static final Interceptor mTokenInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request authorised = originalRequest.newBuilder()
//                    .header("App_Sign", sysUtil.youzySing())
                    .header("App_FromSource", "android-2.65")
                    .header("App_IP", getLocalIpAddress())
                    .header("Content-Type","application/json; charset=utf-8")
                    .build();
            return chain.proceed(authorised);
        }
    };

    /**
     * 判断网络是否可用
     *
     * @param context Context对象
     */
    public static Boolean isNetworkReachable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo current = cm.getActiveNetworkInfo();
        if (current == null) {
            return false;
        }
        return (current.isAvailable());
    }
    /*
     *获取设备IP地址
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    //加上这个地址获取的一定是IPV4地址  不加的话 有可能是IPV6地址
                    if (!inetAddress.isLoopbackAddress()&& !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("VOLLEY", ex.toString());
        }
        return "127.0.0.1(error)";
    }
}
