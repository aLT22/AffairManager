package com.bytebuilding.affairmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bytebuilding.affairmanager.R;

public class EnterActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String BUNDLE_EMAIL_KEY = "email from et";
    public static final String BUNDLE_PASSWORD_KEY = "password from et";

    private EditText etEnterEmail;
    private EditText etEnterPassword;

    private Button btnSignIn;
    private Button btnSignInOffline;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        initViews();
    }

    private void initViews() {
        etEnterEmail = (EditText) findViewById(R.id.etEmail);
        etEnterPassword = (EditText) findViewById(R.id.etPassword);

        btnSignIn = (Button) findViewById(R.id.btn_sign_in_enterActivity);
        btnSignIn.setOnClickListener(this);
        btnSignInOffline = (Button) findViewById(R.id.btn_sign_in_offline);
        btnSignInOffline.setOnClickListener(this);

        bundle = new Bundle();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in_enterActivity:
                Intent startAppInOnline = new Intent(getApplicationContext(), MainOnlineActivity.class);
                startActivity(startAppInOnline);
                finish();
                break;
            case R.id.btn_sign_in_offline:
                Intent startAppInOffline = new Intent(getApplicationContext(), MainOfflineActivity.class);
                startActivity(startAppInOffline);
                finish();
                break;
        }
    }
}
