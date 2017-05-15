package com.bytebuilding.affairmanager.activities;

import android.content.Context;
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
import com.bytebuilding.affairmanager.database.DBHelper;
import com.bytebuilding.affairmanager.database.realm.UserGroupsRealmHelper;
import com.bytebuilding.affairmanager.dialogs.AddingAffairDialogFragment;
import com.bytebuilding.affairmanager.dialogs.AddingUserGroupDialogFragment;
import com.bytebuilding.affairmanager.dialogs.ChangePasswordDialogFragment;
import com.bytebuilding.affairmanager.fragments.drawer.AboutProgramFragment;
import com.bytebuilding.affairmanager.fragments.drawer.UserAffairsFragment;
import com.bytebuilding.affairmanager.fragments.drawer.UserCoworkersFragment;
import com.bytebuilding.affairmanager.fragments.drawer.UserGroupsFragment;
import com.bytebuilding.affairmanager.fragments.drawer.UserProfileFragment;
import com.bytebuilding.affairmanager.model.Affair;
import com.bytebuilding.affairmanager.model.realm.User;
import com.bytebuilding.affairmanager.model.realm.UserAffair;
import com.bytebuilding.affairmanager.model.realm.UserGroup;
import com.bytebuilding.affairmanager.notifications.OfflineNotificationHelper;
import com.bytebuilding.affairmanager.utils.CryptoUtils;
import com.bytebuilding.affairmanager.utils.FirebaseHelper;
import com.bytebuilding.affairmanager.utils.NetworkUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;

public class MainOnlineActivity extends AppCompatActivity implements FirebaseHelper, AddingAffairDialogFragment.AddingUserAffairListener,
        AddingAffairDialogFragment.AddingAffairListener, AddingUserGroupDialogFragment.AddingUserGroupAffairListener,
        ChangePasswordDialogFragment.PasswordChangedListener {

    // TODO: 11.05.17 add suggestion to user to change password
    // TODO: 11.05.17 add double entering (add in application firebase auth options)

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    private Drawer drawerBuilder;

    private Unbinder unbinder;

    private SharedPreferences preferences;

    private FragmentManager fragmentManager;

    private static DatabaseReference rootReference = FirebaseDatabase.getInstance().getReferenceFromUrl(SignUpActivity.FIREBASE_DATABASE_URL);

    private DatabaseReference userReference = rootReference.child("users");

    public static DatabaseReference affairReference = rootReference.child("affairs");

    private boolean quitOptions = true;

    UserAffairsFragment userAffairsFragment;

    private UserGroupsFragment userGroupsFragment;

    private Realm realm;

    private DBHelper dbHelper;

    private List<UserAffair> userAffairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_online);

        unbinder = ButterKnife.bind(this);

        userAffairs = new ArrayList<>();

        realm = Realm.getDefaultInstance();

        dbHelper = new DBHelper(MainOnlineActivity.this);

        userAffairsFragment = UserAffairsFragment.newInstance();
        userGroupsFragment = UserGroupsFragment.newInstance();

        OfflineNotificationHelper.getInstance().initializeAlarmManager(this);

        preferences = getSharedPreferences(SplashScreen.PREFERENCES_NAME, MODE_PRIVATE);

        setSupportActionBar(toolbar);
        createNavigationDrawer();

        fragmentManager = getFragmentManager();

        startDefaultFragment();

        toolbar.setTitle(getResources().getStringArray(R.array.toolbar_titles)[0]);

        if (LoginActivity.customRegistration) {
            ChangePasswordDialogFragment changePasswordDialogFragment = new ChangePasswordDialogFragment();
            changePasswordDialogFragment.show(getFragmentManager(), "ChangePasswordDialog");
        }
    }

    private void startDefaultFragment() {
        if (fragmentContainer != null) {
            UserProfileFragment userProfileFragment = new UserProfileFragment();

            fragmentManager.beginTransaction().add(R.id.fragment_container, userProfileFragment).commit();
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
                        transaction.replace(R.id.fragment_container, currentFragment, currentFragment.toString());
                        //transaction.addToBackStack(null);
                        transaction.commit();

                        return false;
                    }
                })
                .build();
    }

    @NonNull
    private Fragment getFragmentForDrawer(int position) {
        String[] toolbarTitles = getResources().getStringArray(R.array.toolbar_titles);

        Fragment currentFragment = null;
        switch (position) {
            case 1:
                currentFragment = UserAffairsFragment.newInstance();
                toolbar.setTitle(toolbarTitles[0]);
                break;

            case 2:
                currentFragment = UserGroupsFragment.newInstance();
                toolbar.setTitle(toolbarTitles[1]);
                break;

            case 3:
                currentFragment = new UserCoworkersFragment();
                toolbar.setTitle(toolbarTitles[2]);
                break;

            case 5:
                currentFragment = new UserProfileFragment();
                toolbar.setTitle(toolbarTitles[3]);
                break;

            case 7:
                currentFragment = new AboutProgramFragment();
                toolbar.setTitle(toolbarTitles[4]);
                break;

            case 8:
                currentFragment = new AboutProgramFragment();
                toolbar.setTitle(toolbarTitles[4]);
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
                    goToLoginActivity();
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

    private void goToLoginActivity() {
        Intent loginActivityIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginActivityIntent);
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
                        .withSetSelected(true)
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

    public List<UserAffair> getAffairsFromFirebase(Map<String, Object> affairs, long userId) {
        List<UserAffair> userAffairs = new ArrayList<>();

        for (Map.Entry<String, Object> entry : affairs.entrySet()) {
            Map singleAffair = (Map) entry.getValue();

            if (singleAffair.get("userId").equals(userId)) {
                UserAffair userAffair = new UserAffair();

                userAffair.setUserId(Long.valueOf(String.valueOf(singleAffair.get("userId"))));
                userAffair.setUserGroupId(Long.valueOf(String.valueOf(singleAffair.get("userGroupId"))));
                userAffair.setType((String) singleAffair.get("type"));
                userAffair.setTitle((String) singleAffair.get("title"));
                userAffair.setDescription((String) singleAffair.get("description"));
                userAffair.setTimestamp(Long.valueOf(String.valueOf(singleAffair.get("timestamp"))));
                userAffair.setTime(Long.valueOf(String.valueOf(singleAffair.get("time"))));
                userAffair.setStatus(Long.valueOf(String.valueOf(singleAffair.get("status"))));
                userAffair.setRepeatTimestamp((Long) singleAffair.get("repeatTimestamp"));
                userAffair.setPriority(Integer.valueOf(String.valueOf(singleAffair.get("priority"))));
                userAffair.setPlace((String) singleAffair.get("place"));
                userAffair.setObject((String) singleAffair.get("object"));
                userAffair.setDate(Long.valueOf(String.valueOf(singleAffair.get("date"))));

                userAffairs.add(userAffair);
            }
        }

        return userAffairs;
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
        saveAffairToFireBase(userAffair);
        if (NetworkUtils.isNetworkAvailable(this)) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            UserAffairsFragment fragment = (UserAffairsFragment) fragmentManager.findFragmentByTag(UserAffairsFragment.TAG);
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack("UserAffairsFragment");
            fragmentTransaction.commit();
            fragment.addAffairFromFirebase();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.error_getting_data_from_firebase), Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void onGroupAdded(UserGroup userGroup) {
        userGroupsFragment.addGroup(userGroup);
    }

    @Override
    public void onGroupAddingCancel() {
        Toast.makeText(getApplicationContext(), getString(R.string.dialog_adding_group_cancel), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPasswordChanged(String password, boolean flag) {

    }

    @Override
    public void onPasswordChangedCancel() {

    }
}