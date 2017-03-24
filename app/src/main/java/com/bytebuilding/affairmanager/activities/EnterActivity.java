package com.bytebuilding.affairmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bytebuilding.affairmanager.R;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_sign_in_enterActivity)
    public void onSignInEnterActivityButtonClick() {
        Intent startAppInOnline = new Intent(getApplicationContext(), MainOnlineActivity.class);
        startActivity(startAppInOnline);
        finish();
    }

    @OnClick(R.id.btn_sign_in_offline)
    public void onSignInOffline() {
        Bundle bundle = getIntent().getExtras();
        Intent startAppInOffline = new Intent(getApplicationContext(), MainOfflineActivity.class);
        startAppInOffline.putExtras(bundle);
        startActivity(startAppInOffline);
        finish();
    }
}
