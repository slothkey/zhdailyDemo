package com.shulan.simplegank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shulan.simplegank.JumpUtils;
import com.shulan.simplegank.R;
import com.shulan.simplegank.adapter.holder.NormalHolder;
import com.shulan.simplegank.model.theme.ThemeDetail;
import com.shulan.simplegank.model.zhihu.ZhiHuStory;
import com.shulan.simplegank.utils.DensityUtils;
import com.shulan.simplegank.utils.GlideUtils;

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
            TopHolder topHolder = (TopHolder) holder;
            topHolder.des.setText(themeDetail.getDescription());
            Glide.with(context).load(themeDetail.getBackground()).into(topHolder.iv);

            if(themeDetail.getEditors() != null ){
                for(int i = 0; i < themeDetail.getEditors().size(); i++){
                    final ImageView iv = new ImageView(context);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DensityUtils.dip2px(context, 30), DensityUtils.dip2px(context, 30));
                    layoutParams.leftMargin = DensityUtils.dip2px(context, 15);
                    iv.setLayoutParams(layoutParams);
                    topHolder.editors.addView(iv);
                    GlideUtils.loadCircle(context, themeDetail.getEditors().get(i).getAvatar(), iv);
                }
            }


        }else if(holder instanceof NormalHolder){
            final ZhiHuStory data = stories.get(position - 1);
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
            normalHolder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JumpUtils.startWebActivity(context, String.valueOf(data.getId()));
                }
            });
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

    public String getLastId(){
        return String.valueOf(getStories().get(getStories().size() - 1).getId());
    }


    public void setData(ThemeDetail value) {
        themeDetail = value;
        stories = value.getStories();
        notifyDataSetChanged();
    }

    public void setDataStories(List<ZhiHuStory> stories){
        this.stories.addAll(stories);
        notifyDataSetChanged();
    }

    public class TopHolder extends RecyclerView.ViewHolder{

        ImageView iv;
        TextView des;
        LinearLayout editors;

        public TopHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            des = (TextView) itemView.findViewById(R.id.des);
            editors = (LinearLayout) itemView.findViewById(R.id.editors);
        }
    }

}
