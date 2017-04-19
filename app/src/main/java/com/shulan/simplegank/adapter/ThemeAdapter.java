package com.shulan.simplegank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shulan.simplegank.R;
import com.shulan.simplegank.adapter.holder.NormalHolder;
import com.shulan.simplegank.model.theme.ThemeDetail;
import com.shulan.simplegank.model.zhihu.ZhiHuStory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by houna on 17/4/18.
 */

public class ThemeAdapter extends RecyclerView.Adapter {

    private final int TYPE_TOP = 101;
    private final int TYPE_NORMAL = 102;

    private Context context;
    private ThemeDetail themeDetail;
    private List<ZhiHuStory> stories;

    public ThemeAdapter(Context context){
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_TOP){
            return new TopHolder(LayoutInflater.from(context).inflate(R.layout.item_top_theme, parent, false));
        }
        return new NormalHolder(LayoutInflater.from(context).inflate(R.layout.item_gank, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TopHolder){
            // todo
            TopHolder topHolder = (TopHolder) holder;
            topHolder.des.setText(themeDetail.getDescription());
            Glide.with(context).load(themeDetail.getBackground()).into(topHolder.iv);
        }else if(holder instanceof NormalHolder){
            ZhiHuStory data = stories.get(position - 1);
            NormalHolder normalHolder = (NormalHolder) holder;
            if(TextUtils.isEmpty(data.getDate())){
                normalHolder.date.setVisibility(View.GONE);
            }else{
                normalHolder.date.setVisibility(View.VISIBLE);
                normalHolder.date.setText(data.getDate());
            }
            normalHolder.title.setText(data.getTitle());
            List<String> images = data.getImages();
            if(images != null && images.size() > 0){
                normalHolder.img.setVisibility(View.VISIBLE);
                Glide.with(context).load(images.get(0)).into(normalHolder.img);
            }else{
                normalHolder.img.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_TOP : TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        if(getStories().size() == 0){
            return 0;
        }
        return getStories().size() + 1;
    }

    public List<ZhiHuStory> getStories(){
        if(stories == null){
            stories = new ArrayList<>();
        }
        return stories;
    }


    public void setData(ThemeDetail value) {
        themeDetail = value;
        stories = value.getStories();
        notifyDataSetChanged();
    }

    public class TopHolder extends RecyclerView.ViewHolder{

        ImageView iv;
        TextView des;

        public TopHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            des = (TextView) itemView.findViewById(R.id.des);
        }
    }

}
