package com.bytebuilding.affairmanager.activities;

import android.content.SharedPreferences;
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
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

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

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_online);

        unbinder = ButterKnife.bind(this);

        preferences = getSharedPreferences("AffairManagerPreferences", MODE_PRIVATE);

        setSupportActionBar(toolbar);
        createNavigationDrawer();
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
        IProfile profile = new ProfileDrawerItem()
                .withEmail(preferences.getString("login", "User"))
                .withIcon(R.drawable.ic_account_circle_white_48dp);

        AccountHeader accountHeaderBuilder = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.material_background)
                .addProfiles(profile)
                .build();

        Drawer drawerBuilder = new DrawerBuilder()
                .withActivity(this)
                .withShowDrawerOnFirstLaunch(false)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(accountHeaderBuilder)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName(R.string.navigation_item_all_affairs)
                                .withIdentifier(0)
                                .withIcon(R.drawable.ic_all_affairs_drawer_18dp)
                                .withTextColorRes(R.color.color_icons)
                                .withSelectedColorRes(R.color.primary),
                        new PrimaryDrawerItem()
                                .withName(R.string.navigation_item_all_groups)
                                .withIdentifier(1)
                                .withIcon(R.drawable.ic_all_groups_drawer_18dp)
                                .withTextColorRes(R.color.color_icons)
                                .withSelectedColorRes(R.color.primary),
                        new PrimaryDrawerItem()
                                .withName(R.string.navigation_item_all_coworkers)
                                .withIdentifier(2)
                                .withIcon(R.drawable.ic_all_coworkers_drawer_18dp)
                                .withTextColorRes(R.color.color_icons)
                                .withSelectedColorRes(R.color.primary),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName(R.string.navigation_item_account_info)
                                .withIdentifier(3)
                                .withIcon(R.drawable.ic_profile_drawer_18dp)
                                .withTextColorRes(R.color.color_icons)
                                .withSelectedColorRes(R.color.primary),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName(R.string.navigation_item_about)
                                .withIdentifier(4)
                                .withIcon(R.drawable.ic_preferences_drawer_18dp)
                                .withTextColorRes(R.color.color_icons)
                                .withSelectedColorRes(R.color.primary),
                        new SecondaryDrawerItem()
                                .withName(R.string.navigation_item_exit)
                                .withIdentifier(5)
                                .withIcon(R.drawable.ic_exit_drawer_18dp)
                                .withTextColorRes(R.color.color_icons)
                                .withSelectedColorRes(R.color.primary)
                )
                .build();

        drawerBuilder.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                return false;
            }
        });
    }
}