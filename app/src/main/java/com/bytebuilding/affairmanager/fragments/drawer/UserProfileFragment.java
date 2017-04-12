package com.bytebuilding.affairmanager.fragments.drawer;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.activities.SignUpActivity;
import com.bytebuilding.affairmanager.utils.CryptoUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public class UserProfileFragment extends Fragment {

    @BindView(R.id.tv_user_profile_login)
    TextView userProfileLogin;
    @BindView(R.id.tv_user_profile_job)
    TextView userProfileJob;
    @BindView(R.id.tv_user_profile_coworkers)
    TextView userProfileCoworkers;

    private Unbinder unbinder;

    private SharedPreferences preferences;

    private DatabaseReference rootReference;
    private DatabaseReference userReference;

    public UserProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        preferences = getActivity()
                .getSharedPreferences("AffairManagerPreferences", MODE_PRIVATE);

        rootReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(SignUpActivity.FIREBASE_DATABASE_URL);
        userReference = rootReference.child("users");


        userReference.child(String.valueOf(preferences.getLong("id", 0)))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            Map<String, Object> userInfo = (Map<String, Object>) dataSnapshot.getValue();

                            userProfileLogin.setText(CryptoUtils.decrypt(CryptoUtils.KEY,
                                    CryptoUtils.VECTOR, userInfo.get("userLogin").toString()));
                            userProfileJob.setText(CryptoUtils.decrypt(CryptoUtils.KEY,
                                    CryptoUtils.VECTOR, userInfo.get("userOrganization").toString()));
                            userProfileCoworkers.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();

        super.onDestroy();
    }
}