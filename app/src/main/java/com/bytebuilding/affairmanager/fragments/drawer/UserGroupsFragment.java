package com.bytebuilding.affairmanager.fragments.drawer;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UserGroupsFragment extends Fragment {

    @BindView(R.id.fragment_user_groups_fab_add_group)
    FloatingActionButton fabAddGroup;

    @BindView(R.id.fragment_user_groups_fab_add_affair)
    FloatingActionButton fabAddAffair;

    @BindView(R.id.fragment_user_groups_fab_menu)
    FloatingActionsMenu fabMenu;

    private Unbinder unbinder;

    public UserGroupsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_groups, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
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
