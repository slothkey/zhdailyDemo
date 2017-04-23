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
import com.shulan.simplegank.model.Comment;
import com.shulan.simplegank.presenter.CommentPresenter;
import com.shulan.simplegank.ui.IView.ICommentView;

import java.util.List;

/**
 * Created by houna on 17/4/20.
 */

public class CommentActivity extends BaseActivity implements View.OnClickListener, ICommentView {
    public static final String PARAMS_STORY_ID = "params_story_id";
    public static final String PARAMS_COMMENT_COUNTS = "params_comment_counts";

    private TextView title;
    private RecyclerView rv;
    private CommentAdapter adapter;
    private CommentPresenter presenter;

    public static Intent newInstance(Context context, String storyId, int commentCounts){
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra(PARAMS_STORY_ID, storyId);
        intent.putExtra(PARAMS_COMMENT_COUNTS, commentCounts);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        title = (TextView) findViewById(R.id.title);
        findViewById(R.id.back).setOnClickListener(this);
        rv = (RecyclerView) findViewById(R.id.rv);

        initRv();
        presenter = new CommentPresenter(this, getIntent());
        presenter.loadComments();
    }

    private void initRv() {
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CommentAdapter(getActivity());
        rv.setAdapter(adapter);
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
    public void updateTitle(int commentCounts) {
        title.setText(String.format(getResources().getString(R.string.comment_counts), commentCounts));
    }
}
