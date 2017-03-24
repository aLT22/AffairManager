package com.bytebuilding.affairmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.utils.AffairManagerApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmObject;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btn_sign_up) Button btnSignUp;
    @BindView(R.id.btn_sign_in) Button btnSignIn;
    @BindView(R.id.btn_sign_in_vk) Button btnSignInVk;
    @BindView(R.id.btn_sign_in_google) Button btnSignInGoogle;
    @BindView(R.id.btn_sign_in_facebook) Button btnSignInFacebook;

    private Unbinder unbinder;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();
    }

    @OnClick(R.id.btn_sign_in)
    public void onSignInClick() {
        Bundle bundle = getIntent().getExtras();
        Intent enterActivityIntent = new Intent(getApplicationContext(), EnterActivity.class);
        enterActivityIntent.putExtras(bundle);
        startActivity(enterActivityIntent);
        finish();
    }

    @OnClick(R.id.btn_sign_up)
    public void onSignUpClick() {
        Intent signUpActivityIntent = new Intent(this, SignUpActivity.class);
        startActivity(signUpActivityIntent);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        realm.close();

        super.onDestroy();
    }
}