package com.example.maze.cachehttp.Data.retrofit;
/**
 * Created by maze on 2017/2/13.
 */

import com.example.maze.cachehttp.Entity.BaseDto;

/**
 * >>>>>>>>>>>>>>>>>>>
 * 作者：蜗牛
 * 日期：2017/2/13
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>类描述
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>属性描述
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>方法描述
 */
public interface IRetrofitHttp {
    void onSuccess(final BaseDto dto);
    void onThrowable(Throwable throwable);
    void onFinish();

    class SimpleIRetrofitHttp implements IRetrofitHttp{

        @Override
        public void onSuccess(BaseDto dto) {

        }

        @Override
        public void onThrowable(Throwable throwable) {

        }

        @Override
        public void onFinish() {

        }
    }
}
