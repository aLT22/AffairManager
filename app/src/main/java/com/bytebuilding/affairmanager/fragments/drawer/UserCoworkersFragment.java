package com.bytebuilding.affairmanager.fragments.drawer;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.activities.MainOnlineActivity;
import com.bytebuilding.affairmanager.activities.SignUpActivity;
import com.bytebuilding.affairmanager.activities.SplashScreen;
import com.bytebuilding.affairmanager.adapters.online.CoworkersRecyclerAdapter;
import com.bytebuilding.affairmanager.adapters.online.UserAffairsRecyclerAdapter;
import com.bytebuilding.affairmanager.model.online.Coworker;
import com.bytebuilding.affairmanager.utils.CryptoUtils;
import com.bytebuilding.affairmanager.utils.NetworkUtils;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jpardogo.android.googleprogressbar.library.ChromeFloatingCirclesDrawable;
import com.jpardogo.android.googleprogressbar.library.GoogleMusicDicesDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UserCoworkersFragment extends Fragment {

    @BindView(R.id.fragment_user_coworkers_fab_add_group)
    FloatingActionButton fabAddGroup;

    @BindView(R.id.fragment_user_coworkers_fab_add_affair)
    FloatingActionButton fabAddAffair;

    @BindView(R.id.fragment_user_coworkers_fab_menu)
    FloatingActionsMenu fabMenu;

    @BindView(R.id.rv_coworkers)
    RecyclerView recyclerView;

    @BindView(R.id.pb_userCoworkers)
    ProgressBar progressBar;

    private Unbinder unbinder;

    private DatabaseReference rootReference = FirebaseDatabase.getInstance().getReferenceFromUrl(SignUpActivity.FIREBASE_DATABASE_URL);

    private DatabaseReference userReference = rootReference.child("users");

    private CoworkersRecyclerAdapter adapter;

    private SharedPreferences preferences;

    public UserCoworkersFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_coworkers, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        preferences = getActivity().getSharedPreferences(SplashScreen.PREFERENCES_NAME, Context.MODE_PRIVATE);

        progressBar.setIndeterminateDrawable(new ChromeFloatingCirclesDrawable.Builder(rootView.getContext()).build());

        progressBar.setVisibility(View.VISIBLE);

        if (NetworkUtils.isNetworkAvailable(rootView.getContext())) {
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        setUpRecyclerView(getCoworkers((Map<String, Object>) dataSnapshot.getValue(), CryptoUtils.decrypt(CryptoUtils.KEY,
                                CryptoUtils.VECTOR, preferences.getString("job", ""))));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(rootView.getContext().getApplicationContext(), getString(R.string.error_getting_data_from_firebase), Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    private List<Coworker> getCoworkers(Map<String, Object> users, String job) {
        List<Coworker> coworkers = new ArrayList<>();

        if (job != null) {
            if (!job.equals("")) {
                for (Map.Entry<String, Object> entry :
                        users.entrySet()){
                    Map singleUser = (Map) entry.getValue();

                    String userJob = CryptoUtils.decrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, (String) singleUser.get("userOrganization"));

                    if (userJob.equals(job)) {
                        Coworker coworker = new Coworker();

                        coworker.setUsername(CryptoUtils.decrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, (String) singleUser.get("userLogin")));
                        coworker.setJob(CryptoUtils.decrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, (String) singleUser.get("userOrganization")));

                        coworkers.add(coworker);
                    }
                }
            }
        }

        return coworkers;
    }

    private void setUpRecyclerView(List<Coworker> coworkers) {
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        adapter = new CoworkersRecyclerAdapter(getActivity(), coworkers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.fragment_user_coworkers_fab_add_affair)
    public void onAddAffairFabClick() {
        Toast.makeText(getContext().getApplicationContext(), "Add Affair Clicked", Toast.LENGTH_LONG)
                .show();
        fabMenu.collapse();
    }

    @OnClick(R.id.fragment_user_coworkers_fab_add_group)
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
