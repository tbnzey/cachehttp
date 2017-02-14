package com.example.maze.cachehttp;
/**
 * Created by maze on 2017/2/14.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.maze.cachehttp.Data.retrofit.IRetrofitHttp;
import com.example.maze.cachehttp.Data.HttpData;
import com.example.maze.cachehttp.Entity.BaseDto;

/**
 * >>>>>>>>>>>>>>>>>>>
 * 作者：蜗牛
 * 日期：2017/2/14
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>类描述
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>属性描述
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>方法描述
 */
public class TestMain extends AppCompatActivity {

    private TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.testmain);
        textView = (TextView) findViewById(R.id.textView);

        new HttpData.Build()
                .setUrl(R.string.test_url)
                .setRetrofitHttp(new IRetrofitHttp() {
                    @Override
                    public void onSuccess(BaseDto dto) {
                        textView.setText(dto.getJsonStr());
                    }

                    @Override
                    public void onThrowable(Throwable throwable) {

                    }

                    @Override
                    public void onFinish() {

                    }
                })
                .create();
    }
}
