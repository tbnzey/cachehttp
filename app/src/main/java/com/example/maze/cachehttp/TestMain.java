package com.example.maze.cachehttp;
/**
 * Created by maze on 2017/2/14.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.maze.cachehttp.Data.retrofit.BaseResponse;
import com.example.maze.cachehttp.Data.retrofit.HttpData2;
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
public class TestMain extends AppCompatActivity implements View.OnClickListener{

    private TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.testmain, null);
        setContentView(view);
        textView = (TextView) findViewById(R.id.textView);
        view.setOnClickListener(this);
        textView.setOnClickListener(this);
        getHttpData();
    }

    private void getHttpData(){
        new HttpData2.Build()
                .setUrl(R.string.test_url)
                .setRetrofitHttp(new IRetrofitHttp<BaseDto>() {
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
                }).create();
//        new HttpData.Build()
//                .setUrl(R.string.test_url)
//                .setRetrofitHttp(new IRetrofitHttp<String>() {
//                    @Override
//                    public void onSuccess(String dto) {
//                        textView.setText(dto);
//                    }
//
//                    @Override
//                    public void onThrowable(Throwable throwable) {
//
//                    }
//
//                    @Override
//                    public void onFinish() {
//
//                    }
//                })
//                .create();
    }

    @Override
    public void onClick(View v) {
        getHttpData();
    }
}
