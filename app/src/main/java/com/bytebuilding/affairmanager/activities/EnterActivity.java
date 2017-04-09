package com.bytebuilding.affairmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
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

public class EnterActivity extends AppCompatActivity {

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btn_sign_in_enterActivity)
    Button btnSignInEnterActivity;
    @BindView(R.id.btn_sign_in_offline)
    Button btnSignInOffline;

    private DatabaseReference rootReference = FirebaseDatabase.getInstance()
            .getReferenceFromUrl(SignUpActivity.FIREBASE_DATABASE_URL);
    private DatabaseReference userReference = rootReference.child("users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_sign_in_enterActivity)
    public void onSignInEnterActivityButtonClick() {
        /*Intent startAppInOnline = new Intent(getApplicationContext(), MainOnlineActivity.class);
        startActivity(startAppInOnline);
        finish();*/
        if (etEmail.getText().length() == 0 || etPassword.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string
                    .dialog_error_edit_texts), Toast.LENGTH_SHORT).show();
        } else {
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (checkUser((Map<String,Object>) dataSnapshot.getValue(), etEmail.getText()
                            .toString(), etPassword.getText().toString())) {

                        getSharedPreferences("AffairManagerPreferences", MODE_PRIVATE).edit()
                                .putString("login", etEmail.getText().toString()).apply();
                        getSharedPreferences("AffairManagerPreferences", MODE_PRIVATE).edit()
                                .putString("password", etPassword.getText().toString()).apply();
                        getSharedPreferences("AffairManagerPreferences", MODE_PRIVATE).edit()
                                .putString("type", getResources().getStringArray(R.array
                                        .registration_type_in_preferences)[3]).apply();

                        Intent intent = new Intent(getApplicationContext(), MainOnlineActivity
                                .class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string
                                .error_entering_into_application), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string
                    .error_getting_data_from_firebase), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick(R.id.btn_sign_in_offline)
    public void onSignInOffline() {
        Intent startAppInOffline = new Intent(getApplicationContext(), MainOfflineActivity.class);
        startActivity(startAppInOffline);
        finish();
    }

    private boolean checkUser(Map<String, Object> users, String login, String password) {
        List<String> logins = new ArrayList<>();
        List<String> passwords = new ArrayList<>();

        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
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
}
