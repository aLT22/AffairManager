package com.bytebuilding.affairmanager.fragments.drawer;


import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.activities.SignUpActivity;
import com.bytebuilding.affairmanager.dialogs.AddingAffairDialogFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UserAffairsFragment extends Fragment {

    @BindView(R.id.fragment_user_affairs_fab_add_group)
    FloatingActionButton fabAddGroup;

    @BindView(R.id.fragment_user_affairs_fab_add_affair)
    FloatingActionButton fabAddAffair;

    @BindView(R.id.fragment_user_affairs_fab_menu)
    FloatingActionsMenu fabMenu;

    private Unbinder unbinder;

    private DatabaseReference rootReference = FirebaseDatabase.getInstance()
            .getReferenceFromUrl(SignUpActivity.FIREBASE_DATABASE_URL);

    private DatabaseReference userReference = rootReference.child("users");

    private DatabaseReference affairReference = rootReference.child("affairs");

    public UserAffairsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_affairs, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnClick(R.id.fragment_user_affairs_fab_add_affair)
    public void onAddAffairFabClick() {
        fabMenu.collapse();
        DialogFragment addingAffairDialogFragment = new AddingAffairDialogFragment();

        addingAffairDialogFragment.show(getFragmentManager(), "AddingAffairDialogFragment");
    }

    @OnClick(R.id.fragment_user_affairs_fab_add_group)
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
