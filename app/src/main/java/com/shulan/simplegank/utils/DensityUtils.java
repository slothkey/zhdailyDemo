package com.shulan.simplegank.utils;

import android.content.Context;

/**
 * Created by houna on 17/4/29.
 */

public class DensityUtils {

    // int px = (int) TypedValue.applyDimension(
//    TypedValue.COMPLEX_UNIT_DIP, 200, r.getDisplayMetrics());
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int r = (int) (dpValue * scale + 0.5f);
        return r;
    }


}
