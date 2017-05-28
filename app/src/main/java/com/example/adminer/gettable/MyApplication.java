package com.example.adminer.gettable;

import android.app.Application;

import com.example.adminer.gettable.volley.VolleyManager;


/**
 * Created by Administrator on 2016/1/6.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {//所有的组件被实例化之前被调用
        super.onCreate();
        VolleyManager.init(this);
    }
}
