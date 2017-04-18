package com.shulan.simplegank.ui.IView;

import com.shulan.simplegank.base.BaseIView;
import com.shulan.simplegank.model.theme.ThemeObject;
import com.shulan.simplegank.model.zhihu.ZhiHuStory;
import com.shulan.simplegank.model.zhihu.ZhiHuTopStory;

import java.util.List;

/**
 * Created by houna on 17/4/10.
 */
public interface IGankView extends BaseIView{

    public void refreshSuccess(List<ZhiHuStory> dataList, List<ZhiHuTopStory> topList);

    public void refreshThemes(ThemeObject obj);
}
