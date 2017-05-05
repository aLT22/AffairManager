package com.bytebuilding.affairmanager.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.adapters.MainOfflineActivityPagerAdapter;
import com.bytebuilding.affairmanager.database.DBHelper;
import com.bytebuilding.affairmanager.dialogs.AddingAffairDialogFragment;
import com.bytebuilding.affairmanager.fragments.CurrentOfflineAffairsFragment;
import com.bytebuilding.affairmanager.fragments.drawer.AboutProgramFragment;
import com.bytebuilding.affairmanager.fragments.drawer.UserAffairsFragment;
import com.bytebuilding.affairmanager.fragments.drawer.UserCoworkersFragment;
import com.bytebuilding.affairmanager.fragments.drawer.UserGroupsFragment;
import com.bytebuilding.affairmanager.fragments.drawer.UserProfileFragment;
import com.bytebuilding.affairmanager.model.Affair;
import com.bytebuilding.affairmanager.model.realm.User;
import com.bytebuilding.affairmanager.model.realm.UserAffair;
import com.bytebuilding.affairmanager.notifications.OfflineNotificationHelper;
import com.bytebuilding.affairmanager.utils.CryptoUtils;
import com.bytebuilding.affairmanager.utils.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;

public class MainOnlineActivity extends AppCompatActivity implements FirebaseHelper, AddingAffairDialogFragment.AddingUserAffairListener,
        AddingAffairDialogFragment.AddingAffairListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    private Drawer drawerBuilder;

    private Unbinder unbinder;

    private SharedPreferences preferences;

    private FragmentManager fragmentManager;

    private DatabaseReference rootReference = FirebaseDatabase.getInstance().getReferenceFromUrl(SignUpActivity.FIREBASE_DATABASE_URL);

    private DatabaseReference userReference = rootReference.child("users");

    private DatabaseReference affairReference = rootReference.child("affairs");

    private boolean quitOptions = true;

    private UserAffairsFragment userAffairsFragment;

    private Realm realm;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_online);

        unbinder = ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();

        dbHelper = new DBHelper(MainOnlineActivity.this);

        userAffairsFragment = new UserAffairsFragment();

        OfflineNotificationHelper.getInstance().initializeAlarmManager(getApplicationContext());

        preferences = getSharedPreferences(SplashScreen.PREFERENCES_NAME, MODE_PRIVATE);

        setSupportActionBar(toolbar);
        createNavigationDrawer();

        fragmentManager = getFragmentManager();

        if (fragmentContainer != null) {
            UserProfileFragment userProfileFragment = new UserProfileFragment();

            fragmentManager.beginTransaction().add(R.id.fragment_container, userProfileFragment)
                    .commit();
        }
    }


    @Override
    protected void onDestroy() {
        unbinder.unbind();
        realm.close();

        super.onDestroy();
    }



    private void createNavigationDrawer() {
        AccountHeader accountHeaderBuilder = setAccountHeader();

        drawerBuilder = new DrawerBuilder()
                .withActivity(this)
                .withSelectedItem(3)
                .withShowDrawerOnFirstLaunch(false)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(accountHeaderBuilder)
                .addDrawerItems(setDrawerItems())
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Fragment currentFragment = null;

                        currentFragment = getFragmentForDrawer(position);

                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, currentFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                        return false;
                    }
                })
                .build();
    }

    @NonNull
    private Fragment getFragmentForDrawer(int position) {
        Fragment currentFragment = null;
        switch (position) {
            case 1:
                currentFragment = new UserAffairsFragment();
                break;

            case 2:
                currentFragment = new UserGroupsFragment();
                break;

            case 3:
                currentFragment = new UserCoworkersFragment();
                break;

            case 5:
                currentFragment = new UserProfileFragment();
                break;

            case 7:
                currentFragment = new AboutProgramFragment();
                break;

            case 8:
                currentFragment = new AboutProgramFragment();
                showQuitDialog();
                break;
        }
        return currentFragment;
    }

    private void showQuitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainOnlineActivity.this,
                R.style.Theme_AppCompat_Dialog);
        builder.setTitle(R.string.quit_dialog_title);
        builder.setIcon(R.mipmap.ic_launcher);

        String[] dialogItems = getResources().getStringArray(R.array.quite_dialog_items);

        builder.setSingleChoiceItems(dialogItems, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    quitOptions = true;
                } else {
                    quitOptions = false;
                }
            }
        });

        String positiveButtonText = getString(android.R.string.ok);

        builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (quitOptions) {
                    goToMainOfflineActivity();
                } else {
                    quitOptions = true;
                    goToEnterActivity();
                }

            }
        });

        String negativeButtonText = getString(android.R.string.cancel);

        builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void goToEnterActivity() {
        Intent enterActivityIntent = new Intent(getApplicationContext(), EnterActivity.class);
        startActivity(enterActivityIntent);
        finish();
    }

    private void goToMainOfflineActivity() {
        Intent intent = new Intent(getApplicationContext(), MainOfflineActivity.class);
        startActivity(intent);
        finish();
    }

    private AccountHeader setAccountHeader() {
        IProfile profile = new ProfileDrawerItem()
                .withEmail(CryptoUtils.decrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, preferences
                        .getString("login", "Non-identifier User")))
                .withIcon(R.drawable.ic_account_circle_white_48dp);

        return new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.material_background)
                .addProfiles(profile)
                .build();
    }

    @NonNull
    private IDrawerItem[] setDrawerItems() {
        return new IDrawerItem[]{new PrimaryDrawerItem()
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
                        .withSelectedColorRes(R.color.primary)};
    }

    @Override
    public void onBackPressed() {
        if (drawerBuilder != null && drawerBuilder.isDrawerOpen()) {
            drawerBuilder.closeDrawer();
        }
        else super.onBackPressed();
    }

    @Override
    public void saveUserToFirebase(User user) {
    }

    @Override
    public void saveAffairToFireBase(UserAffair userAffair) {
        affairReference.child(String.valueOf(userAffair.getTimestamp())).setValue(userAffair);
    }

    @Override
    public void onUserAffairAdded(UserAffair userAffair) {
        userAffairsFragment.addUserAffair(userAffair);
        saveAffairToFireBase(userAffair);
    }

    @Override
    public void onUserAffairAddingCancel() {
        Toast.makeText(getApplicationContext(), getString(R.string.affair_adding_cancel), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAffairAdded(Affair affair) {
        dbHelper.saveAffair(affair);
    }

    @Override
    public void onAffairAddingCancel() {
        Toast.makeText(getApplicationContext(), getString(R.string.affair_adding_cancel), Toast.LENGTH_SHORT).show();
    }
}

// TODO: 06.05.17 need to add group-id in UserAffair and in User and when users are standing at the same groups, both of users get affairs
// TODO: 06.05.17 when user has the same job-name with another user, there is shows in user's colleague's list