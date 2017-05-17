package com.bytebuilding.affairmanager.fragments.drawer;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.activities.SplashScreen;
import com.bytebuilding.affairmanager.database.realm.UserGroupsRealmHelper;
import com.bytebuilding.affairmanager.model.realm.UserGroup;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.Sort;

public class UserGroupsFragment extends Fragment {

    @BindView(R.id.fragment_user_groups_fab_add_group)
    FloatingActionButton fabAddGroup;

    @BindView(R.id.fragment_user_groups_fab_add_affair)
    FloatingActionButton fabAddAffair;

    @BindView(R.id.fragment_user_groups_fab_menu)
    FloatingActionsMenu fabMenu;

    @BindView(R.id.groups_recyclerview)
    RecyclerView recyclerView;

    private Unbinder unbinder;

    private Realm realm;

    public UserGroupsFragment() {
    }

    public static UserGroupsFragment newInstance() {
        UserGroupsFragment userGroupsFragment = new UserGroupsFragment();
        Bundle bundle = new Bundle();

        userGroupsFragment.setArguments(bundle);

        return userGroupsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_groups, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        realm = Realm.getDefaultInstance();

        return rootView;
    }

    public void addGroup(UserGroup userGroup) {
        realm = Realm.getDefaultInstance();

        UserGroupsRealmHelper.addUserGroupAsync(realm, userGroup);
    }

    @OnClick(R.id.fragment_user_groups_fab_add_group)
    public void onAddAffairFabClick() {
        Toast.makeText(getContext().getApplicationContext(), "Add Affair Clicked", Toast.LENGTH_LONG)
                .show();
        fabMenu.collapse();
    }

    @OnClick(R.id.fragment_user_groups_fab_add_affair)
    public void onAddGroupFabClick() {
        Toast.makeText(getContext().getApplicationContext(), "Add Group Clicked", Toast.LENGTH_LONG)
                .show();
        fabMenu.collapse();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();

        super.onDestroyView();
    }
}
