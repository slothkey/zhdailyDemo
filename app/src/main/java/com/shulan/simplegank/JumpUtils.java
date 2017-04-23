package com.shulan.simplegank;

import android.content.Context;

import com.shulan.simplegank.ui.common.WebActivity;

/**
 * Created by houna on 17/4/22.
 */

public class JumpUtils {

    public static void startWebActivity(Context context, String storyId){
        context.startActivity(WebActivity.newInstance(context, storyId));
    }

}
