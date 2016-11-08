package com.bytebuilding.affairmanager.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.adapters.MainOfflineActivityPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainOfflineActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.vp_main_offline_activity)
    ViewPager vpMainOfflineActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_offline);
        ButterKnife.bind(this);
        initTabs();
    }

    private void initTabs() {
        tabLayout.addTab(tabLayout.newTab().setText(R.string.current_affairs));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.done_affairs));

        initTabsBehavior();
    }

    private void initTabsBehavior() {
        MainOfflineActivityPagerAdapter mainOfflineActivityPagerAdapter =
                new MainOfflineActivityPagerAdapter(getSupportFragmentManager(), (byte) 2);

        vpMainOfflineActivity.setAdapter(mainOfflineActivityPagerAdapter);

        vpMainOfflineActivity.setOnPageChangeListener(new TabLayout
                .TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpMainOfflineActivity.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
