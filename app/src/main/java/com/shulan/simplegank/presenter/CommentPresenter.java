package com.shulan.simplegank.presenter;

import android.content.Intent;

import com.shulan.simplegank.model.Comment;
import com.shulan.simplegank.model.CommentObj;
import com.shulan.simplegank.model.detail.StoryExtra;
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
    private StoryExtra storyExtra;
    private String storyId;
    private List<Comment> longComments;
    private List<Comment> shortComments;

    public CommentPresenter(ICommentView view, Intent intent){
        this.view = view;
        storyId = intent.getStringExtra(CommentActivity.PARAMS_STORY_ID);
        storyExtra = (StoryExtra) intent.getSerializableExtra(CommentActivity.PARAMS_STORY_EXTRA);
        if(storyExtra != null){
            view.updateTitle(storyExtra);
        }
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
                }, getErrorConsumer());
    }

    public Consumer<Throwable> getErrorConsumer(){
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                // todo 应该提示联网错误，但是现在没有一个好的toast ，所以暂时先不写（跳很多个toast很丑）
                view.updateUI(getLongComments(), getShortComments());
            }
        };
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
                }, getErrorConsumer());
    }

    public void loadMoreLongComments() {
        if(longComments.size() == 0){
            return;
        }
        Network.getManager()
                .create(GankService.class)
                .getMoreLongComments(storyId, longComments.get(longComments.size() - 1).getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CommentObj>() {
                    @Override
                    public void accept(CommentObj commentObj) throws Exception {
                        longComments.addAll(commentObj.getComments());
                        view.updateUI(getLongComments(), getShortComments());
                    }
                }, getErrorConsumer());
    }

    public void loadMoreShortComments() {
        if(shortComments.size() == 0){
            return;
        }
        Network.getManager()
                .create(GankService.class)
                .getMoreShortComments(storyId, shortComments.get(shortComments.size() - 1).getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CommentObj>() {
                    @Override
                    public void accept(CommentObj commentObj) throws Exception {
                        shortComments.addAll(commentObj.getComments());
                        view.updateUI(getLongComments(), getShortComments());
                    }
                }, getErrorConsumer());
    }
}
