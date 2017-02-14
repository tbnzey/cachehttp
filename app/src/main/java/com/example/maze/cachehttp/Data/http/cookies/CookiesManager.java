package com.example.maze.cachehttp.Data.http.cookies;
/**
 * Created by maze on 2017/2/13.
 */

import com.example.maze.cachehttp.Application.BaseApplication;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * >>>>>>>>>>>>>>>>>>>
 * 作者：zey
 * 日期：2017/2/13
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>类描述
 * 自动管理Cookies
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>属性描述
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>方法描述
 */
public class CookiesManager implements CookieJar {
    private final PersistentCookieStore cookieStore = new PersistentCookieStore(BaseApplication.getAppContext());

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }
}
