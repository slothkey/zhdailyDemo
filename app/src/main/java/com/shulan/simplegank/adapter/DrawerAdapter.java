package com.shulan.simplegank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shulan.simplegank.R;
import com.shulan.simplegank.config.Constants;
import com.shulan.simplegank.event.ChangeThemeEvent;
import com.shulan.simplegank.model.theme.Theme;
import com.shulan.simplegank.model.theme.ThemeObject;
import com.shulan.simplegank.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by houna on 17/4/18.
 */

public class DrawerAdapter extends RecyclerView.Adapter {

    private final int TYPE_TOP = 101;
    private final int TYPE_HOME = 102;
    private final int TYPE_THEME = 103;

    private int selectPosition = 1;
    private Context context;
    private ThemeObject datas;
    private List<Theme> subscribes;
    private List<Theme> themes;

    public DrawerAdapter(Context context){
        this.context = context;
    }

    public void setData(ThemeObject obj) {
        this.datas = obj;
        this.subscribes = datas.getSubscribed();
        this.themes = datas.getOthers();
        notifyDataSetChanged();
    }

    public ThemeObject getData(){
        if(datas == null){
            datas = new ThemeObject();
        }
        return datas;
    }

    public List<Theme> getSubscribes(){
        if(subscribes == null){
            subscribes = new ArrayList<>();
        }
        return subscribes;
    }

    public List<Theme> getThemes(){
        if(themes == null){
            themes = new ArrayList<>();
        }
        return themes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_TOP){
            return new TopHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer_top, parent, false));
        }else if(viewType == TYPE_HOME){
            return new HomeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer_home, parent, false));
        }else {
            return new ThemeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer_theme, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof TopHolder){

        }else if(holder instanceof HomeHolder){
            holder.itemView.setBackgroundColor(context.getResources().getColor(position == selectPosition ? R.color.gray : R.color.white));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectPosition = position;
                    EventBus.getDefault().post(new ChangeThemeEvent(Constants.HOME));
                    notifyDataSetChanged();
                }
            });
        }else if(holder instanceof ThemeHolder){
            ThemeHolder themeHolder = (ThemeHolder) holder;
            final Theme theme;
            if(position - 2 < getSubscribes().size()){
                theme = getSubscribes().get(position - 2);
                themeHolder.status.setImageResource(R.mipmap.menu_arrow);
                themeHolder.status.setOnClickListener(null);
            }else{
                theme = getThemes().get(position - 2 - getSubscribes().size());
                final int id = theme.getId();
                themeHolder.status.setImageResource(R.mipmap.menu_follow);
                themeHolder.status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        follow(String.valueOf(id));
                    }
                });
            }
            final int id = theme.getId();
            themeHolder.name.setText(theme.getName());
            holder.itemView.setBackgroundColor(context.getResources().getColor(position == selectPosition ? R.color.gray : R.color.white));
            themeHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // todo  (现在先写好，再通过看mvp rxjava 思考应该怎么写)
                    selectPosition = position;
                    EventBus.getDefault().post(new ChangeThemeEvent(String.valueOf(id)));
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 2 + getThemes().size() + getSubscribes().size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_TOP;
        }else if(position == 1){
            return TYPE_HOME;
        }
        return TYPE_THEME;
    }

    public void follow(String id) {
        boolean followStatus = !SpUtils.isFollow(id);
        SpUtils.saveFollow(id, followStatus);

        if(followStatus){ // 如果现在关注某个了
            for(int i = 0; i < getThemes().size(); i++){
                Theme theme = getThemes().get(i);
                if(TextUtils.equals(id, String.valueOf(theme.getId()))){ // 其实有些地方是int，有些地方是String，并不好
                    getThemes().remove(i);
                    getSubscribes().add(theme);
                    selectPosition = getSubscribes().size() +1;
                    Toast.makeText(context, "关注成功", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }else{
            for(int i = 0; i < getSubscribes().size(); i++){
                Theme theme = getSubscribes().get(i);
                if(TextUtils.equals(id, String.valueOf(theme.getId()))){ // 其实有些地方是int，有些地方是String，并不好
                    getSubscribes().remove(i);
                    getThemes().add(theme);
                    selectPosition = getSubscribes().size() + getThemes().size() +1;
                    Toast.makeText(context, "已取消关注", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    public class TopHolder extends RecyclerView.ViewHolder{


        public TopHolder(View itemView) {
            super(itemView);
        }
    }

    public class HomeHolder extends RecyclerView.ViewHolder{



        public HomeHolder(View itemView) {
            super(itemView);
        }
    }

    public class ThemeHolder extends RecyclerView.ViewHolder{

        private ImageView status;
        private TextView name;

        public ThemeHolder(View itemView) {
            super(itemView);
            status = (ImageView) itemView.findViewById(R.id.status);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
