package com.shulan.simplegank.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.shulan.simplegank.config.App;

/**
 * Created by houna on 17/5/1.
 */

public class SpUtils {

    public static final String HOME = "home_readed";
    public static final String THEME = "theme_readed";

    public static final String TYPE_READED = "type_readed";

    public static void saveReaded(String key, int id){
        SharedPreferences sp = App.getContext().getSharedPreferences(TYPE_READED, Context.MODE_PRIVATE);
        String readed = sp.getString(key, "");
        sp.edit().putString(key, readed + id + ",").commit();
    }

    public static boolean isReaded(String key, int id){
        SharedPreferences sp = App.getContext().getSharedPreferences(TYPE_READED, Context.MODE_PRIVATE);
        String readed = sp.getString(key, "");
        if(readed.contains(String.valueOf(id))){
            return true;
        }
        return false;
    }


}
