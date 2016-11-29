package com.bytebuilding.affairmanager.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.adapters.MainOfflineActivityPagerAdapter;
import com.bytebuilding.affairmanager.animations.DepthPageAnimation;
import com.bytebuilding.affairmanager.dialogs.AddingAffairDialogFragment;
import com.bytebuilding.affairmanager.fragments.CurrentOfflineAffairsFragment;
import com.bytebuilding.affairmanager.fragments.DoneOfflineAffairsFragment;
import com.bytebuilding.affairmanager.fragments.OfflineAffairFragment;
import com.bytebuilding.affairmanager.model.Affair;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainOfflineActivity extends AppCompatActivity implements AddingAffairDialogFragment.AddingAffairListener, CurrentOfflineAffairsFragment.OnAffairDoneListener, DoneOfflineAffairsFragment
        .OnAffairRestoreListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.vp_main_offline_activity)
    ViewPager vpMainOfflineActivity;
    @BindView(R.id.fab_add_affair_offline)
    FloatingActionButton fabAddAffairOffline;

    private MainOfflineActivityPagerAdapter mainOfflineActivityPagerAdapter;

    private OfflineAffairFragment currentOfflineAffairsFragment;
    private OfflineAffairFragment doneOfflineAffairsFragment;

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
        mainOfflineActivityPagerAdapter =
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

        currentOfflineAffairsFragment = (CurrentOfflineAffairsFragment) mainOfflineActivityPagerAdapter
                .getItem(MainOfflineActivityPagerAdapter.CURRENT_AFFAIR_FRAGMENT_POSITION);
        doneOfflineAffairsFragment = (DoneOfflineAffairsFragment) mainOfflineActivityPagerAdapter
                .getItem(MainOfflineActivityPagerAdapter.DONE_AFFAIR_FRAGMENT_POSITION);

        vpMainOfflineActivity.setPageTransformer(true, new DepthPageAnimation());
    }

    @Override
    public void onAffairAdded(Affair affair) {
        currentOfflineAffairsFragment.addAffair(affair);
        //Toast.makeText(getApplicationContext(), "Задача была добавлена", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAffairAddingCancel() {
        Toast.makeText(getApplicationContext(), "Задача не была добавлена", Toast.LENGTH_SHORT)
                .show();
    }

    @OnClick(R.id.fab_add_affair_offline)
    public void onFabAddAffairOfflineClick() {
        DialogFragment addingAffairDialogFragment = new AddingAffairDialogFragment();

        addingAffairDialogFragment.show(getFragmentManager(), "AddingAffairDialogFragment");
    }

    @Override
    public void onAffairDone(Affair affair) {
        doneOfflineAffairsFragment.addAffair(affair);
    }

    @Override
    public void onAffairRestore(Affair affair) {
        currentOfflineAffairsFragment.addAffair(affair);
    }
}
