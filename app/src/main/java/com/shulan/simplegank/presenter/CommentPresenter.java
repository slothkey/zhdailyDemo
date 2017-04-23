package com.shulan.simplegank.presenter;

import android.content.Intent;

import com.shulan.simplegank.model.Comment;
import com.shulan.simplegank.model.CommentObj;
import com.shulan.simplegank.model.service.GankService;
import com.shulan.simplegank.network.Network;
import com.shulan.simplegank.ui.CommentActivity;
import com.shulan.simplegank.ui.IView.ICommentView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by houna on 17/4/20.
 */

public class CommentPresenter {

    private ICommentView view;
    private int commentCounts;
    private String storyId;
    private List<Comment> longComments;
    private List<Comment> shortComments;

    public CommentPresenter(ICommentView view, Intent intent){
        this.view = view;
        storyId = intent.getStringExtra(CommentActivity.PARAMS_STORY_ID);
        commentCounts = intent.getIntExtra(CommentActivity.PARAMS_COMMENT_COUNTS, 0);
        view.updateTitle(commentCounts);
    }

    public List<Comment> getLongComments(){
        if(longComments == null){
            longComments = new ArrayList<>();
        }
        return longComments;
    }

    public List<Comment> getShortComments(){
        if(shortComments == null){
            shortComments = new ArrayList<>();
        }
        return shortComments;
    }

    public void loadComments(){
        loadLongComments();
        loadShortComments();
    }

    public void loadShortComments() {
        Network.getManager()
                .create(GankService.class)
                .getShortComments(storyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CommentObj>() {
                    @Override
                    public void accept(CommentObj commentObj) throws Exception {
                        shortComments = commentObj.getComments();
                        view.updateUI(getLongComments(), getShortComments());
                    }
                });
    }

    public void loadLongComments() {
        Network.getManager()
                .create(GankService.class)
                .getLongComments(storyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CommentObj>() {
                    @Override
                    public void accept(CommentObj commentObj) throws Exception {
                        longComments = commentObj.getComments();
                        view.updateUI(getLongComments(), getShortComments());
                    }
                });
    }
}
