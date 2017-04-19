package com.shulan.simplegank.ui.IView;

import com.shulan.simplegank.base.BaseIView;
import com.shulan.simplegank.model.zhihu.ZhiHuStory;
import com.shulan.simplegank.model.zhihu.ZhiHuTopStory;

import java.util.List;

/**
 * Created by houna on 17/4/18.
 */

public interface IHomeView extends BaseIView {

    public void refreshSuccess(List<ZhiHuStory> dataList, List<ZhiHuTopStory> topList);

}
