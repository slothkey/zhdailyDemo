package com.shulan.simplegank.config;

import android.app.Application;
import android.content.Context;

/**
 * Created by houna on 17/4/30.
 */

public class App extends Application{

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}
