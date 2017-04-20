package com.shulan.simplegank.ui.IView;

import com.shulan.simplegank.model.detail.StoryDetail;
import com.shulan.simplegank.model.detail.StoryExtra;

/**
 * Created by houna on 17/4/19.
 */

public interface IWebView {
    void refreshUI(StoryDetail story);

    void refreshBar(StoryExtra value);
}
