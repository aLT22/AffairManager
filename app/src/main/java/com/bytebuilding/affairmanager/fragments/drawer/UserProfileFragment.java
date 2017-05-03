package com.bytebuilding.affairmanager.fragments.drawer;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.activities.SignUpActivity;
import com.bytebuilding.affairmanager.activities.SplashScreen;
import com.bytebuilding.affairmanager.utils.CryptoUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public class UserProfileFragment extends Fragment {

    @BindView(R.id.tv_user_profile_login)
    EditText userProfileLogin;

    @BindView(R.id.tv_user_profile_job)
    EditText userProfileJob;

    @BindView(R.id.tv_user_profile_coworkers)
    TextView userProfileCoworkers;

    @BindView(R.id.ib_user_profile_edit)
    ImageButton userProfileEdit;

    @BindView(R.id.btn_accept_changes)
    Button userProfileAcceptChanges;

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
                .getSharedPreferences(SplashScreen.PREFERENCES_NAME, MODE_PRIVATE);

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

        //userProfileLogin.setSelected(false);
        userProfileLogin.setFocusable(false);
        //userProfileJob.setSelected(false);
        userProfileJob.setFocusable(false);

        userProfileAcceptChanges.setClickable(false);

        setUserProfileInformation();

        return rootView;
    }

    private void setUserProfileInformation() {
        userProfileLogin.setText(CryptoUtils.decrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, preferences.getString("login", "Default user")));
        userProfileJob.setText(CryptoUtils.decrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, preferences.getString("job", "")));
        userProfileCoworkers.setText("0");
    }

    @OnClick(R.id.ib_user_profile_edit)
    public void onIbEditUserProfileClick() {

        userProfileLogin.setFocusableInTouchMode(true);
        userProfileJob.setFocusableInTouchMode(true);
        userProfileLogin.setFocusable(true);
        userProfileLogin.setActivated(true);

        userProfileAcceptChanges.setClickable(true);
        userProfileAcceptChanges.setFocusableInTouchMode(true);

        userProfileAcceptChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newLogin = userProfileLogin.getText().toString();
                final String newJob = userProfileJob.getText().toString();

                userProfileLogin.setSelected(false);
                userProfileLogin.setFocusable(false);
                userProfileJob.setSelected(false);
                userProfileJob.setFocusable(false);

                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (checkRegisteredUser((Map<String, Object>) dataSnapshot.getValue(), newLogin)) {
                            Toast.makeText(getContext().getApplicationContext(), getResources().getString(R.string
                                    .user_exists), Toast.LENGTH_SHORT).show();
                        } else {
                            userReference.child(String.valueOf(preferences.getLong("id", 0))).child("userLogin")
                                    .setValue(CryptoUtils.encrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, newLogin));
                            userReference.child(String.valueOf(preferences.getLong("id", 0))).child("userOrganization")
                                    .setValue(CryptoUtils.encrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, newJob));

                            preferences.edit().putString("login", CryptoUtils.encrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, newLogin))
                                    .apply();
                            preferences.edit().putString("job", CryptoUtils.encrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, newJob))
                                    .apply();

                            showAttentionDialog();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void showAttentionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Dialog);
        builder.setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
        builder.setTitle(getResources().getString(R.string.attention_dialog_title));
        builder.setMessage(getResources().getString(R.string.attention_dialog_message));
        String buttonText = getResources().getString(R.string.alert_dialog_newbie_button_text);
        builder.setNeutralButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean checkRegisteredUser(Map<String, Object> users, String login) {
        List<String> logins = new ArrayList<>();

        for (Map.Entry<String, Object> entry : users.entrySet()){
            Map singleUser = (Map) entry.getValue();

            String log = CryptoUtils.decrypt(CryptoUtils.KEY, CryptoUtils.VECTOR,
                    (String) singleUser.get("userLogin"));

            logins.add(log);
        }

        if (logins.contains(login)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();

        super.onDestroyView();
    }
}
