package com.bytebuilding.affairmanager.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.model.realm.User;
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
import io.realm.Realm;

public class SignUpActivity extends AppCompatActivity implements FirebaseHelper {

    public static final String FIREBASE_DATABASE_URL = "https://affair-manager-8a6f9.firebaseio.com/";

    @BindView(R.id.til_user_login) TextInputLayout tilUserLogin;
    @BindView(R.id.et_user_login) EditText etUserLogin;
    @BindView(R.id.til_user_password) TextInputLayout tilUserPassword;
    @BindView(R.id.et_user_password) TextInputEditText etUserPassword;
    @BindView(R.id.til_user_job) TextInputLayout tilUserJob;
    @BindView(R.id.et_user_job) EditText etUserJob;
    @BindView(R.id.btn_user_accept) Button btnUserRegistrate;

    private Unbinder unbinder;

    private String login = "login";
    private String password = "password";
    private String job = "job";

    private SharedPreferences preferences;

    private Realm realm;

    private DatabaseReference rootReference = FirebaseDatabase.getInstance()
            .getReferenceFromUrl(FIREBASE_DATABASE_URL);
    private DatabaseReference userReference = rootReference.child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        unbinder = ButterKnife.bind(this);

        btnUserRegistrate.setEnabled(false);

        preferences = getSharedPreferences("AffairManagerPreferences", MODE_PRIVATE);

        realm = Realm.getDefaultInstance();

        setEditTexts();
    }

    private void setEditTexts() {
        etUserJob = tilUserJob.getEditText();

        tilUserJob.setHint(getResources().getString(R.string.signup_hint_job));

        tilUserLogin.setError(getResources().getString(R.string.dialog_error_edit_text));
        tilUserLogin.setHint(getResources().getString(R.string.signup_hint_login));
        etUserLogin.setHint(getResources().getString(R.string.signup_hint_login));

        tilUserPassword.setError(getResources().getString(R.string.dialog_error_edit_text));
        tilUserPassword.setHint(getResources().getString(R.string.signup_hint_password));
    }

    @OnClick(R.id.btn_user_accept)
    public void onRegistrateClick() {
        login = CryptoUtils.encrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, etUserLogin.getText()
                    .toString());
        password = CryptoUtils.encrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, etUserPassword.getText()
                    .toString());
        if (etUserJob.getText().length() == 0) {
            job = "";
        } else {
            job = CryptoUtils.encrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, etUserJob.getText()
                    .toString());
        }

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (checkRegisteredUser((Map<String, Object>) dataSnapshot.getValue(),
                        CryptoUtils.decrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, login))) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string
                                    .user_exists), Toast.LENGTH_SHORT).show();
                } else {
                    preferences.edit().putString("login", login).apply();
                    preferences.edit().putString("password", password).apply();
                    preferences.edit().putString("type", getResources().getStringArray(R.array
                            .registration_type_in_preferences)[3]).apply();

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Number num = realm.where(User.class).max("userId");
                            long nextID;
                            if(num == null) {
                                nextID = 0;
                            } else {
                                nextID = num.intValue() + 1;
                            }
                            User user = realm.createObject(User.class, nextID);
                            user.setUserLogin(login);
                            user.setUserPassword(password);
                            user.setUserOrganization(job);
                        }
                    });

                    saveUserToFirebase(realm.where(User.class).findAll().last());

                    Intent intent = new Intent(getApplicationContext(), MainOnlineActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean checkRegisteredUser(Map<String, Object> users, String login) {
        List<String> logins = new ArrayList<>();

        for (Map.Entry<String, Object> entry : users.entrySet()){
            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
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
    protected void onStart() {
        super.onStart();

        etUserLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btnUserRegistrate.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tilUserLogin.setError(null);
                    if (etUserPassword.length() != 0) {
                        tilUserPassword.setError(null);
                        btnUserRegistrate.setEnabled(true);
                    } else {
                        tilUserPassword.setError(getResources().getString(R.string
                                .dialog_error_edit_text));
                        btnUserRegistrate.setEnabled(false);
                    }
                } else {
                    tilUserLogin.setError(getResources().getString(R.string.dialog_error_edit_text));
                    btnUserRegistrate.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etUserPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btnUserRegistrate.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tilUserPassword.setError(null);
                    if (etUserLogin.length() != 0) {
                        tilUserLogin.setError(null);
                        btnUserRegistrate.setEnabled(true);
                    } else {
                        tilUserLogin.setError(getResources().getString(R.string
                                .dialog_error_edit_text));
                        btnUserRegistrate.setEnabled(false);
                    }
                } else {
                    tilUserPassword.setError(getResources().getString(R.string.dialog_error_edit_text));
                    btnUserRegistrate.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        realm.close();
        super.onDestroy();
    }

    @Override
    public void saveUserToFirebase(User user) {
        userReference.child(String.valueOf(System.currentTimeMillis())).setValue(user);
    }
}