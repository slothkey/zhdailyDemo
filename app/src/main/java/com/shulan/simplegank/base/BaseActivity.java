package com.shulan.simplegank.base;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by houna on 17/4/15.
 */
public class BaseActivity extends AppCompatActivity implements BaseIView{


    public void toast(String text){
        Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void logE(String tag, String content){
        Log.e(tag, content);
    }

    @Override
    public Activity getContext() {
        return this;
    }
}
