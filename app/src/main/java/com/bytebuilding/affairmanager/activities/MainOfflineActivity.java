package com.bytebuilding.affairmanager.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.adapters.MainOfflineActivityPagerAdapter;
import com.bytebuilding.affairmanager.animations.DepthPageAnimation;
import com.bytebuilding.affairmanager.database.DBHelper;
import com.bytebuilding.affairmanager.dialogs.AddingAffairDialogFragment;
import com.bytebuilding.affairmanager.dialogs.EditAffairDialogFragment;
import com.bytebuilding.affairmanager.fragments.CurrentOfflineAffairsFragment;
import com.bytebuilding.affairmanager.fragments.DoneOfflineAffairsFragment;
import com.bytebuilding.affairmanager.fragments.OfflineAffairFragment;
import com.bytebuilding.affairmanager.model.Affair;
import com.bytebuilding.affairmanager.notifications.OfflineNotificationHelper;
import com.bytebuilding.affairmanager.utils.AffairManagerApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainOfflineActivity extends AppCompatActivity implements AddingAffairDialogFragment.AddingAffairListener,
        CurrentOfflineAffairsFragment.OnAffairDoneListener, DoneOfflineAffairsFragment.OnAffairRestoreListener,
        EditAffairDialogFragment.EditingAffairListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.vp_main_offline_activity)
    ViewPager vpMainOfflineActivity;
    @BindView(R.id.fab_add_affair_offline)
    FloatingActionButton fabAddAffairOffline;
    @BindView(R.id.search_view)
    SearchView searchView;

    private MainOfflineActivityPagerAdapter mainOfflineActivityPagerAdapter;

    OfflineAffairFragment currentOfflineAffairsFragment;

    OfflineAffairFragment doneOfflineAffairsFragment;
    public DBHelper dbHelper;

    public OfflineAffairFragment getCurrentOfflineAffairsFragment() {
        return currentOfflineAffairsFragment;
    }

    public OfflineAffairFragment getDoneOfflineAffairsFragment() {
        return doneOfflineAffairsFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_offline);
        ButterKnife.bind(this);

        dbHelper = new DBHelper(getApplicationContext());

        fabAddAffairOffline.setColorFilter(getResources().getColor(R.color.color_white));

        OfflineNotificationHelper.getInstance().initializeAlarmManager(getApplicationContext());

        initTabs();
        setSearchViewListener();
    }

    private void setSearchViewListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentOfflineAffairsFragment.findAffairsByTitle(newText);
                doneOfflineAffairsFragment.findAffairsByTitle(newText);
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabAddAffairOffline.hide();
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                fabAddAffairOffline.show();
                return false;
            }
        });
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
        currentOfflineAffairsFragment.addAffair(affair, true);
    }

    @Override
    public void onAffairAddingCancel() {
        Toast.makeText(getApplicationContext(), "Задача не была добавлена", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fab_add_affair_offline)
    public void onFabAddAffairOfflineClick() {
        if (getResources().getDisplayMetrics().density > 1.5) {
            DialogFragment addingAffairDialogFragment = new AddingAffairDialogFragment();

            addingAffairDialogFragment.show(getFragmentManager(), "AddingAffairDialogFragment");
        } else {
            Intent intent = new Intent(getApplicationContext(), AddAffairActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onAffairDone(Affair affair) {
        doneOfflineAffairsFragment.addAffair(affair, false);
    }

    @Override
    public void onAffairRestore(Affair affair) {
        currentOfflineAffairsFragment.addAffair(affair, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AffairManagerApplication.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AffairManagerApplication.activityPaused();
    }

    @Override
    public void onAffairEdited(Affair updatedAffair) {
        currentOfflineAffairsFragment.updateAffair(updatedAffair);
        dbHelper.getDbUpdateManager().affair(updatedAffair);
    }
}