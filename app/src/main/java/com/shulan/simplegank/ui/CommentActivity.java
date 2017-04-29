package com.shulan.simplegank.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shulan.simplegank.R;
import com.shulan.simplegank.adapter.CommentAdapter;
import com.shulan.simplegank.base.BaseActivity;
import com.shulan.simplegank.event.UnfoldShortCommentEvent;
import com.shulan.simplegank.model.Comment;
import com.shulan.simplegank.model.detail.StoryExtra;
import com.shulan.simplegank.presenter.CommentPresenter;
import com.shulan.simplegank.ui.IView.ICommentView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by houna on 17/4/20.
 */

public class CommentActivity extends BaseActivity implements View.OnClickListener, ICommentView {
    public static final String PARAMS_STORY_ID = "params_story_id";
    public static final String PARAMS_STORY_EXTRA = "params_story_extra";

    private TextView title;
    private RecyclerView rv;
    private CommentAdapter adapter;
    private CommentPresenter presenter;

    public static Intent newInstance(Context context, String storyId, StoryExtra commentCounts){
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra(PARAMS_STORY_ID, storyId);
        intent.putExtra(PARAMS_STORY_EXTRA, commentCounts);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        EventBus.getDefault().register(getActivity());

        title = (TextView) findViewById(R.id.title);
        findViewById(R.id.back).setOnClickListener(this);
        rv = (RecyclerView) findViewById(R.id.rv);

        initRv();
        presenter = new CommentPresenter(this, getIntent());
        presenter.loadComments();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(getActivity());
    }

    private void initRv() {
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CommentAdapter(getActivity());
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(!rv.canScrollVertically(1) && newState == SCROLL_STATE_IDLE){
                    boolean isFold = adapter.isFold(); // 如果是折叠的（true），说明应该是加载更多长评论。否则是加载更多短评论
                    if(isFold){
                        presenter.loadMoreLongComments();
                    }else{
                        presenter.loadMoreShortComments();
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void updateUI(List<Comment> longComments, List<Comment> shortComments) {
        adapter.setComments(longComments, shortComments);
    }

    @Override
    public void updateTitle(StoryExtra storyExtra) {
        title.setText(String.format(getResources().getString(R.string.comment_counts), storyExtra.getComments()));
        adapter.setStoryExtra(storyExtra);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUnfoldShortCommentEvent(UnfoldShortCommentEvent event){
        int firstItem = rv.getChildLayoutPosition(rv.getChildAt(0));
        int movePosition = event.getPosition() - firstItem;
        int top = rv.getChildAt(movePosition).getTop();
        rv.smoothScrollBy(0, top);
    }

}
