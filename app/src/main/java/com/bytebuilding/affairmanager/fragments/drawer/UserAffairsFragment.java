package com.bytebuilding.affairmanager.fragments.drawer;


import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.activities.DetailAffairActivity;
import com.bytebuilding.affairmanager.activities.MainOnlineActivity;
import com.bytebuilding.affairmanager.activities.SignUpActivity;
import com.bytebuilding.affairmanager.adapters.realm.RealmUserAffairsAdapter;
import com.bytebuilding.affairmanager.database.realm.UserAffairsRealmHelper;
import com.bytebuilding.affairmanager.database.realm.UserGroupsRealmHelper;
import com.bytebuilding.affairmanager.dialogs.AddingAffairDialogFragment;
import com.bytebuilding.affairmanager.dialogs.AddingUserGroupDialogFragment;
import com.bytebuilding.affairmanager.model.Affair;
import com.bytebuilding.affairmanager.model.Item;
import com.bytebuilding.affairmanager.model.realm.UserAffair;
import com.bytebuilding.affairmanager.model.realm.UserGroup;
import com.bytebuilding.affairmanager.notifications.OfflineNotificationHelper;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmResults;

public class UserAffairsFragment extends Fragment {

    @BindView(R.id.fragment_user_affairs_fab_add_group)
    FloatingActionButton fabAddGroup;

    @BindView(R.id.fragment_user_affairs_fab_add_affair)
    FloatingActionButton fabAddAffair;

    @BindView(R.id.fragment_user_affairs_fab_menu)
    FloatingActionsMenu fabMenu;

    @BindView(R.id.rv_online_user_affairs)
    RecyclerView recyclerView;

    private RealmUserAffairsAdapter adapter;

    private Unbinder unbinder;

    private Realm realm;

    private DatabaseReference rootReference = FirebaseDatabase.getInstance()
            .getReferenceFromUrl(SignUpActivity.FIREBASE_DATABASE_URL);

    private DatabaseReference userReference = rootReference.child("users");

    private DatabaseReference affairReference = rootReference.child("affairs");

    private OfflineNotificationHelper offlineNotificationHelper;

    private Context context;

    public UserAffairsFragment() {
    }

    public static UserAffairsFragment newInstance() {
        Bundle bundle = new Bundle();
        UserAffairsFragment userAffairsFragment = new UserAffairsFragment();
        userAffairsFragment.setArguments(bundle);


        return userAffairsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_affairs, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        context = rootView.getContext();

        offlineNotificationHelper = OfflineNotificationHelper.getInstance();

        setUpRecyclerView();

        return rootView;
    }

    private void setUpRecyclerView() {
        adapter = new RealmUserAffairsAdapter(realm.where(UserAffair.class).findAllSorted("status"), true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.fragment_user_affairs_fab_add_affair)
    public void onAddAffairFabClick() {
        fabMenu.collapse();
        DialogFragment addingAffairDialogFragment = new AddingAffairDialogFragment();

        addingAffairDialogFragment.show(getFragmentManager(), "AddingAffairDialogFragment");
    }

    @OnClick(R.id.fragment_user_affairs_fab_add_group)
    public void onAddGroupFabClick() {
        fabMenu.collapse();
        DialogFragment addingGroupDialogFragment = new AddingUserGroupDialogFragment();

        addingGroupDialogFragment.show(getFragmentManager(), "AddingGroupDialogFragment");
    }

    public void addUserAffair(UserAffair userAffair) {
        realm = Realm.getDefaultInstance();

        UserAffairsRealmHelper.addUserAffairAsync(realm, userAffair);
    }

    public void addUserAffairCancel() {
        Toast.makeText(getActivity(), getString(R.string.affair_adding_cancel), Toast.LENGTH_SHORT).show();
    }

    public void addUserGroup(UserGroup userGroup) {
        realm = Realm.getDefaultInstance();

        UserGroupsRealmHelper.addUserGroupAsync(realm, userGroup);
    }

    public void seeDetails(UserAffair affair, Context context) {
        Bundle innerBundle = new Bundle();

        innerBundle.putString("title", affair.getTitle());
        innerBundle.putString("description", affair.getDescription());
        innerBundle.putLong("date", affair.getDate());
        innerBundle.putLong("time", affair.getTime());
        innerBundle.putString("object", affair.getObject());
        innerBundle.putString("type", affair.getType());
        innerBundle.putString("place", affair.getPlace());
        innerBundle.putInt("status", affair.getStatus());

        Intent startDetailActivity = new Intent(context, DetailAffairActivity.class);
        startDetailActivity.putExtras(innerBundle);

        context.startActivity(startDetailActivity, innerBundle);
    }

    public void doneUserAffair(UserAffair userAffair) {
        offlineNotificationHelper.doneAlarm(userAffair.getTimestamp());
    }

    public void restoreUserAffair(UserAffair userAffair) {
        offlineNotificationHelper.setReceiver(userAffair);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        realm.close();

        super.onDestroyView();
    }
}