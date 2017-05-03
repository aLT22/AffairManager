package com.bytebuilding.affairmanager.fragments.drawer;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

    private ProgressDialog progressDialog;

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

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        progressDialog = new ProgressDialog(getActivity());

        Log.e("LOGIN", "onDataChange: " + CryptoUtils.decrypt(CryptoUtils.KEY,
                CryptoUtils.VECTOR, preferences.getString("login", "Default user")) + " " + preferences
                .getString("login", "Default user"));

        Log.e("JOB", "onDataChange: " + CryptoUtils.decrypt(CryptoUtils.KEY,
                CryptoUtils.VECTOR, preferences.getString("job", "")) + " " + preferences.getString("job", ""));

        userProfileLogin.setText(CryptoUtils.decrypt(CryptoUtils.KEY,
                CryptoUtils.VECTOR, preferences.getString("login", "Default user")));
        userProfileJob.setText(CryptoUtils.decrypt(CryptoUtils.KEY,
                CryptoUtils.VECTOR, preferences.getString("job", "")));
        userProfileCoworkers.setText("0");

        return rootView;
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();

        super.onDestroy();
    }
}
