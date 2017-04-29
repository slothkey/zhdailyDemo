package com.shulan.simplegank.ui.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shulan.simplegank.R;
import com.shulan.simplegank.base.BaseActivity;
import com.shulan.simplegank.model.detail.StoryDetail;
import com.shulan.simplegank.model.detail.StoryExtra;
import com.shulan.simplegank.presenter.WebPresenter;
import com.shulan.simplegank.ui.CommentActivity;
import com.shulan.simplegank.ui.IView.IWebView;

import java.math.BigDecimal;

/**
 * Created by houna on 17/4/19.
 */

public class WebActivity extends BaseActivity implements IWebView, View.OnClickListener {

    public static final String PARAMS_STORY_ID = "params_story_id";

    private WebView webView;
    private ImageView img;
    private TextView title;
    private TextView imageSource;
    private TextView comments;
    private TextView popularities;

    private WebPresenter presenter;

    public static Intent newInstance(Context context, String id) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(PARAMS_STORY_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 我觉得可以 把toolbar 和 其他参数都作为配置传进来（我应该看下环信的）    可以先写出来一种详情的页面，之后再按照环信写
        setContentView(R.layout.activity_web);

        webView = (WebView) findViewById(R.id.webview);
        img = (ImageView) findViewById(R.id.img);
        title = (TextView) findViewById(R.id.title);
        imageSource = (TextView) findViewById(R.id.image_source);
        comments = (TextView) findViewById(R.id.comments);
        popularities = (TextView) findViewById(R.id.popularities);
        findViewById(R.id.comment_container).setOnClickListener(this);
        findViewById(R.id.popularities_container).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);


//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                return false;
//            }
//        });

        presenter = new WebPresenter(this, getIntent());
        presenter.getStoryDetail();
        presenter.getStoryExtra();
    }


    @Override
    public void refreshUI(StoryDetail story) {
        String htmlData = "<link rel=\"stylesheet\" type=\"text/css\" href=\"news_qa.min.css\" />" + story.getBody();
        webView.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "utf-8", null);

        Glide.with(this).load(story.getImage()).into(img);
        title.setText(story.getTitle());
        if(TextUtils.isEmpty(story.getImage_source())){ // 因为不知道之后用viewpager的复用情况，所以暂时先这么写
            imageSource.setVisibility(View.GONE);
        }else {
            imageSource.setVisibility(View.VISIBLE);
            imageSource.setText(story.getImage_source());
        }

    }

    @Override
    public void refreshBar(StoryExtra value) {
        comments.setText(String.valueOf(value.getComments()));
        popularities.setText(showPopularities(value.getPopularity()));
    }

    private String showPopularities(int popularities) {
        String count;
        if(popularities < 1000){
            count = String.valueOf(popularities);
        }else{
            double d = popularities * 1.0 / 1000;
            BigDecimal bd = new BigDecimal(d);
            bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
            count = bd + "k";
        }
        return count;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comment_container:
                startActivity(CommentActivity.newInstance(getActivity(), presenter.getStoryId(), presenter.getStoryExtraInfo()));
                break;
            case R.id.popularities_container:
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
