package com.bytebuilding.affairmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.bytebuilding.affairmanager.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSignUp;
    private Button btnSignIn;
    private Button btnSignInVk;
    private Button btnSignInGoogle;
    private Button btnSignInFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    private void initViews() {
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(this);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(this);
        btnSignInVk = (Button) findViewById(R.id.btn_sign_in_vk);
        btnSignInVk.setOnClickListener(this);
        btnSignInGoogle = (Button) findViewById(R.id.btn_sign_in_google);
        btnSignInGoogle.setOnClickListener(this);
        btnSignInFacebook = (Button) findViewById(R.id.btn_sign_in_facebook);
        btnSignInFacebook.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                Intent enterActivityIntent = new Intent(getApplicationContext(),
                        EnterActivity.class);
                startActivity(enterActivityIntent);
                break;
        }
    }
}