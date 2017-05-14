package com.bytebuilding.affairmanager.fragments.drawer;


import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.activities.DetailAffairActivity;
import com.bytebuilding.affairmanager.activities.MainOnlineActivity;
import com.bytebuilding.affairmanager.activities.SignUpActivity;
import com.bytebuilding.affairmanager.activities.SplashScreen;
import com.bytebuilding.affairmanager.adapters.online.UserAffairsRecyclerAdapter;
import com.bytebuilding.affairmanager.dialogs.AddingAffairDialogFragment;
import com.bytebuilding.affairmanager.dialogs.AddingUserGroupDialogFragment;
import com.bytebuilding.affairmanager.model.realm.UserAffair;
import com.bytebuilding.affairmanager.model.realm.UserGroup;
import com.bytebuilding.affairmanager.notifications.OfflineNotificationHelper;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jpardogo.android.googleprogressbar.library.ChromeFloatingCirclesDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;

public class UserAffairsFragment extends Fragment {

    public static final String TAG = "UserAffairsFragment";

    @BindView(R.id.fragment_user_affairs_fab_add_group)
    FloatingActionButton fabAddGroup;

    @BindView(R.id.fragment_user_affairs_fab_add_affair)
    FloatingActionButton fabAddAffair;

    @BindView(R.id.fragment_user_affairs_fab_menu)
    FloatingActionsMenu fabMenu;

    @BindView(R.id.rv_online_user_affairs)
    RecyclerView recyclerView;

    @BindView(R.id.pb_userAffair)
    ProgressBar progressBar;

    private UserAffairsRecyclerAdapter adapter;

    private Unbinder unbinder;

    private Realm realm;

    private DatabaseReference rootReference = FirebaseDatabase.getInstance()
            .getReferenceFromUrl(SignUpActivity.FIREBASE_DATABASE_URL);

    private DatabaseReference userReference = rootReference.child("users");

    private DatabaseReference affairReference = rootReference.child("affairs");

    private OfflineNotificationHelper offlineNotificationHelper;

    private Context context;

    private MainOnlineActivity mainOnlineActivity;

    private List<UserAffair> userAffairs = new ArrayList<>();

    public UserAffairsFragment() {
    }

    public static UserAffairsFragment newInstance() {
        Bundle bundle = new Bundle();
        UserAffairsFragment userAffairsFragment = new UserAffairsFragment();
        userAffairsFragment.setArguments(bundle);

        return userAffairsFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            mainOnlineActivity = (MainOnlineActivity) getActivity();
            context = getActivity();
        }

        offlineNotificationHelper = OfflineNotificationHelper.getInstance();
    }

    public void addAffairFromFirebase() {
        adapter.removeAllItems();

        List<UserAffair> userAffairList = new ArrayList<>();
        userAffairList.addAll(userAffairs);
        for (int i = 0; i < userAffairList.size(); i++) {
            addUserAffair(userAffairList.get(i));
        }

        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    public void addUserAffair(UserAffair userAffair) {
        int position = -1;

        for (int i = 0; i < adapter.getItemCount(); i++) {
            UserAffair task = adapter.getUserAffair(i);
            if (userAffair.getDate() < task.getDate()) {
                position = i;
                break;
            }
        }

        if (position != -1) {
            adapter.addUserAffair(position, userAffair);
        } else {
            adapter.addUserAffair(userAffair);
        }
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

        mainOnlineActivity = (MainOnlineActivity) getActivity();

        progressBar.setIndeterminateDrawable(new ChromeFloatingCirclesDrawable.Builder(rootView.getContext()).build());

        setUpRecyclerView();
        affairReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    userAffairs = mainOnlineActivity.getAffairsFromFirebase((Map<String, Object>) dataSnapshot.getValue(),
                            mainOnlineActivity.getSharedPreferences(SplashScreen.PREFERENCES_NAME, Context.MODE_PRIVATE).getLong("id", 0));
                    setUpRecyclerView();
                    addAffairFromFirebase();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    private void setUpRecyclerView() {
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        adapter = new UserAffairsRecyclerAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);
    }

    public void updateStatusAffair(long id, long status) {
        affairReference.child(String.valueOf(id)).child("status").setValue(status);
    }

    public void deleteAffairFromFirebase(long id) {
        affairReference.child(String.valueOf(id)).removeValue();
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

    public void addUserAffairCancel() {
        Toast.makeText(getActivity(), getString(R.string.affair_adding_cancel), Toast.LENGTH_SHORT).show();
    }

    public void addUserGroup(UserGroup userGroup) {

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
        innerBundle.putLong("status", affair.getStatus());

        Intent startDetailActivity = new Intent(context, DetailAffairActivity.class);
        startDetailActivity.putExtras(innerBundle);

        context.startActivity(startDetailActivity, innerBundle);
    }

    @Override
    public String toString() {
        return TAG;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        realm.close();

        super.onDestroyView();
    }
}