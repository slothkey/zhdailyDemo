package com.shulan.simplegank.ui.IView;

import com.shulan.simplegank.base.BaseIView;
import com.shulan.simplegank.model.Comment;

import java.util.List;

/**
 * Created by houna on 17/4/20.
 */

public interface ICommentView extends BaseIView {

    void updateUI(List<Comment> longComments, List<Comment> shortComments);

    void updateTitle(int commentCounts);
}
