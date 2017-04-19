package com.shulan.simplegank.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.shulan.simplegank.R;
import com.shulan.simplegank.adapter.DrawerAdapter;
import com.shulan.simplegank.base.BaseActivity;
import com.shulan.simplegank.event.ChangeThemeEvent;
import com.shulan.simplegank.model.theme.ThemeObject;
import com.shulan.simplegank.presenter.GankPresenter;
import com.shulan.simplegank.ui.IView.IGankView;
import com.shulan.simplegank.ui.fragment.HomeFragment;
import com.shulan.simplegank.ui.fragment.ThemeFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

public class MainActivity extends BaseActivity implements IGankView {

    private FrameLayout mContainer;
    DrawerLayout drawerLayout;
    private RecyclerView leftDrawer;
    private DrawerAdapter drawerAdapter;
    private GankPresenter presenter;
    private HomeFragment homeFragment;
    private HashMap<String, ThemeFragment> fragments = new HashMap<>();
    private String currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(getActivity());

        mContainer = (FrameLayout) findViewById(R.id.fragment_container);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        toggle.syncState();
        drawerLayout.setDrawerListener(toggle);


        initLeftDrawer();
        loadFragment();

        presenter = new GankPresenter(this);
        presenter.getThemes();
    }

    private void loadFragment() {
        homeFragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, homeFragment, "home");
        transaction.commit();
        currentFragment = "home";
    }

    public void showFragment(String key){
        // todo 之后再优化fragment 的生命周期
        if(TextUtils.equals(key, currentFragment)){
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(fragments.get(key) == null){
            fragments.put(key, ThemeFragment.newInstance(key));
        }
        ThemeFragment fragment = fragments.get(key);
        if(!fragment.isAdded()){
            transaction.add(R.id.fragment_container, fragment, key);
        }
        Fragment curr = getSupportFragmentManager().findFragmentByTag(currentFragment);
        transaction.hide(curr);
        transaction.show(fragment);
        transaction.commit();
        currentFragment = key;
    }

    private void initLeftDrawer() {
        leftDrawer = (RecyclerView) findViewById(R.id.left_drawer);
        leftDrawer.setLayoutManager(new LinearLayoutManager(this));
        drawerAdapter = new DrawerAdapter();
        leftDrawer.setAdapter(drawerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btn_share:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void refreshThemes(ThemeObject obj) {
        drawerAdapter.setData(obj);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeThemeEvent(ChangeThemeEvent event){
        drawerLayout.closeDrawers();
        showFragment(event.getId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(getActivity());
    }
}
