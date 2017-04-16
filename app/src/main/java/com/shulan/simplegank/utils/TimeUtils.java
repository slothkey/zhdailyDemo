package com.shulan.simplegank.utils;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by houna on 17/4/15.
 */
public class TimeUtils {

    public static String networkDate(int beforeDay){
        Calendar calendar = Calendar.getInstance();
        if(beforeDay != 0){
            calendar.setTimeInMillis(calendar.getTimeInMillis() - beforeDay * 24 * 60 * 60 * 1000l);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = "" + year + (month <= 9 ? "0" + month : month) + day;
//        Log.e("timeutils", calendar.toString());
        Log.e("timeutils", date);
        return date;
    }

}
