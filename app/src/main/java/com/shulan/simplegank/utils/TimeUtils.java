package com.shulan.simplegank.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by houna on 17/4/15.
 */
public class TimeUtils {

    /**
     * 获得现在的前几（beforeDay）天。加入今天是2017年4月18日，beforeday=3，return 20170415
     * @param beforeDay
     * @return
     */
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

    public static String getMDWeek(String time){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(time));
            String week = "";
            switch (c.get(Calendar.DAY_OF_WEEK)){
                case Calendar.MONDAY:
                    week = "星期一";
                    break;
                case Calendar.TUESDAY:
                    week = "星期二";
                    break;
                case Calendar.WEDNESDAY:
                    week = "星期三";
                    break;
                case Calendar.THURSDAY:
                    week = "星期四";
                    break;
                case Calendar.FRIDAY:
                    week = "星期五";
                    break;
                case Calendar.SATURDAY:
                    week = "星期六";
                    break;
                case Calendar.SUNDAY:
                    week = "星期日";
                    break;
            }
            int month = c.get(Calendar.MONTH) + 1;
            int day = c.get(Calendar.DAY_OF_MONTH);
            return month + "月" + day + "日 " + week;
        } catch (ParseException e) {
            e.printStackTrace();
            return time;
        }
    }


}
