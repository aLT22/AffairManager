package com.bytebuilding.affairmanager.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainOnlineActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_add_group)
    FloatingActionButton fabAddGroup;
    @BindView(R.id.fab_add_affair)
    FloatingActionButton fabAddAffair;
    @BindView(R.id.fab_menu)
    FloatingActionsMenu fabMenu;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_online);

        unbinder = ButterKnife.bind(this);

        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.fab_add_affair)
    public void onAddAffairFabClick() {
        Toast.makeText(getApplicationContext(), "Add Affair Clicked", Toast.LENGTH_LONG)
                .show();
        fabMenu.collapse();
    }

    @OnClick(R.id.fab_add_group)
    public void onAddGroupFabClick() {
        Toast.makeText(getApplicationContext(), "Add Group Clicked", Toast.LENGTH_LONG)
                .show();
        fabMenu.collapse();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();

        super.onDestroy();
    }

    private void createNavigationDrawer() {
        Drawer drawerBuilder = new DrawerBuilder()
                .withActivity(this)
                .withShowDrawerOnFirstLaunch(false)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem(),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem(),
                        new SectionDrawerItem(),
                        new SectionDrawerItem()
                )
                .build();
    }
}