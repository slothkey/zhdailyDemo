package com.shulan.simplegank.ui.IView;

import com.shulan.simplegank.base.BaseIView;
import com.shulan.simplegank.model.theme.ThemeDetail;

/**
 * Created by houna on 17/4/18.
 */

public interface IThemeView extends BaseIView {


    void refreshTheme(ThemeDetail value);

    void loadMoreThemes(ThemeDetail value);
}
