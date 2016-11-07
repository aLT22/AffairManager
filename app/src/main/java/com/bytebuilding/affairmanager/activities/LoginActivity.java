package com.bytebuilding.affairmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.bytebuilding.affairmanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btn_sign_up)
    Button btnSignUp;
    @BindView(R.id.btn_sign_in)
    Button btnSignIn;
    @BindView(R.id.btn_sign_in_vk)
    Button btnSignInVk;
    @BindView(R.id.btn_sign_in_google)
    Button btnSignInGoogle;
    @BindView(R.id.btn_sign_in_facebook)
    Button btnSignInFacebook;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_sign_in)
    public void onSignInClick() {
        Intent enterActivityIntent = new Intent(getApplicationContext(),
                EnterActivity.class);
        startActivity(enterActivityIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbinder.unbind();
    }
}