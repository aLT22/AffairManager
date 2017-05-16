package com.bytebuilding.affairmanager.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.utils.Advertisement;
import com.bytebuilding.affairmanager.utils.CryptoUtils;
import com.bytebuilding.affairmanager.utils.NetworkUtils;
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

public class EnterActivity extends AppCompatActivity {

    @BindView(R.id.etEmail) EditText etEmail;
    @BindView(R.id.etPassword) EditText etPassword;
    @BindView(R.id.btn_sign_in_enterActivity) Button btnSignInEnterActivity;
    @BindView(R.id.btn_sign_in_offline) Button btnSignInOffline;
    @BindView(R.id.btn_sign_up_enterActivity) Button btnSignUpEnterActivity;

    private ProgressDialog progressDialog;

    private Unbinder unbinder;

    private DatabaseReference rootReference = FirebaseDatabase.getInstance()
            .getReferenceFromUrl(SignUpActivity.FIREBASE_DATABASE_URL);
    private DatabaseReference userReference = rootReference.child("users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        unbinder = ButterKnife.bind(this);

        Advertisement.showBannerEnterActivity(this);

        progressDialog = new ProgressDialog(EnterActivity.this);
    }

    @OnClick(R.id.btn_sign_in_enterActivity)
    public void onSignInEnterActivityButtonClick() {
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (isNetworkAvailable()) {
                    if (etEmail.getText().length() == 0 || etPassword.getText().length() == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string
                                        .dialog_error_edit_texts), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        if (rootReference.child("users").toString() != "users") {
                            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() == null && NetworkUtils.isNetworkAvailable(getApplicationContext())) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), getResources().getString(R
                                                        .string.error_entering_into_application), Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        });
                                    } else {
                                        if (checkUser((Map<String, Object>) dataSnapshot.getValue(), etEmail.getText()
                                                .toString(), etPassword.getText().toString())) {

                                            List<String> userInfo = getUserInfo((Map<String, Object>) dataSnapshot.getValue(), etEmail.getText()
                                                    .toString());

                                            getSharedPreferences(SplashScreen.PREFERENCES_NAME, MODE_PRIVATE).edit()
                                                    .putString("login", CryptoUtils.encrypt(CryptoUtils.KEY,
                                                            CryptoUtils.VECTOR, etEmail.getText().toString()))
                                                    .apply();
                                            getSharedPreferences(SplashScreen.PREFERENCES_NAME, MODE_PRIVATE).edit()
                                                    .putString("password", CryptoUtils.encrypt(CryptoUtils.KEY,
                                                            CryptoUtils.VECTOR, etPassword.getText().toString()))
                                                    .apply();
                                            getSharedPreferences(SplashScreen.PREFERENCES_NAME, MODE_PRIVATE).edit()
                                                    .putString("type", CryptoUtils.encrypt(CryptoUtils.KEY,
                                                            CryptoUtils.VECTOR, getResources()
                                                                    .getStringArray(R.array
                                                                            .registration_type_in_preferences)[3]))
                                                    .apply();
                                            getSharedPreferences(SplashScreen.PREFERENCES_NAME, MODE_PRIVATE).edit()
                                                    .putString("job", userInfo.get(1))
                                                    .apply();
                                            getSharedPreferences(SplashScreen.PREFERENCES_NAME, MODE_PRIVATE).edit()
                                                    .putLong("id", Long.valueOf(userInfo.get(2)))
                                                    .apply();

                                            goToMainOnlineActivity();
                                        } else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string
                                                            .error_entering_into_application), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string
                                                    .error_getting_data_from_firebase), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string
                                            .error_getting_data_from_firebase), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string
                                    .error_getting_data_from_firebase), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
            }
        }).start();
    }

    private void goToMainOnlineActivity() {
        Intent intent = new Intent(getApplicationContext(), MainOnlineActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_sign_up_enterActivity)
    public void onSignUpEnterActivityButtonClick() {
        if (isNetworkAvailable()) {
            goToSignUpActivity();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string
                    .error_getting_data_from_firebase), Toast.LENGTH_SHORT).show();
        }
    }

    private void goToSignUpActivity() {
        Intent signUpActivityIntent = new Intent(this, SignUpActivity.class);
        startActivity(signUpActivityIntent);
    }

    @OnClick(R.id.btn_sign_in_offline)
    public void onSignInOffline() {
        goToMainOfflineActivity();
    }

    private void goToMainOfflineActivity() {
        Intent startAppInOffline = new Intent(getApplicationContext(), MainOfflineActivity.class);
        startActivity(startAppInOffline);
        finish();
    }

    private boolean checkUser(Map<String, Object> users, String login, String password) {
        List<String> logins = new ArrayList<>();
        List<String> passwords = new ArrayList<>();

        for (Map.Entry<String, Object> entry : users.entrySet()) {

            Map singleUser = (Map) entry.getValue();

            String log = CryptoUtils.decrypt(CryptoUtils.KEY, CryptoUtils.VECTOR,
                    (String) singleUser.get("userLogin"));
            String pass = CryptoUtils.decrypt(CryptoUtils.KEY, CryptoUtils.VECTOR,
                    (String) singleUser.get("userPassword"));
            logins.add(log);
            passwords.add(pass);
        }

        if (logins.contains(login) && passwords.contains(password)) {
            return true;
        } else {
            return false;
        }
    }

    private List<String> getUserInfo(Map<String, Object> users, String login) {
        List<String> userInfo = new ArrayList<>();

        for (Map.Entry<String, Object> entry : users.entrySet()) {

            Map singleUser = (Map) entry.getValue();

            String temporaryLogin = CryptoUtils.decrypt(CryptoUtils.KEY, CryptoUtils.VECTOR,
                    (String) singleUser.get("userLogin"));

            if (temporaryLogin.equals(login)) {
                userInfo.add((String) singleUser.get("userLogin"));
                userInfo.add((String) singleUser.get("userOrganization"));
                userInfo.add(String.valueOf(singleUser.get("userId")));
            } else {
            }
        }

        return userInfo;
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        progressDialog.dismiss();

        super.onDestroy();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}