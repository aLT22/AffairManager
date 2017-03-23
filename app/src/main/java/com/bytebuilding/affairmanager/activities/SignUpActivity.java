package com.bytebuilding.affairmanager.activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.bytebuilding.affairmanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.til_user_login) TextInputLayout tilUserLogin;
    @BindView(R.id.et_user_login) EditText etUserLogin;
    @BindView(R.id.til_user_password) TextInputLayout tilUserPassword;
    @BindView(R.id.et_user_password) EditText etUserPassword;
    @BindView(R.id.rb_user_password) AppCompatRadioButton rbUserPassword;
    @BindView(R.id.til_user_job) TextInputLayout tilUserJob;
    @BindView(R.id.et_user_job) EditText etUserJob;
    @BindView(R.id.btn_user_accept) Button btnUserRegistrate;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        unbinder = ButterKnife.bind(this);

        setEditTexts();

        // TODO: 24.03.17 optimize text input layout hints and errors
        etUserLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (etUserLogin.length() == 0 || etUserPassword.length() == 0) {
                    btnUserRegistrate.setEnabled(false);
                    tilUserLogin.setErrorEnabled(true);
                    tilUserLogin.setError(getResources().getString(R.string.dialog_error_edit_text));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tilUserLogin.setError(null);
                }
                if (etUserLogin.length() == 0 || etUserPassword.length() == 0) {
                    btnUserRegistrate.setEnabled(false);
                    tilUserLogin.setErrorEnabled(true);
                    tilUserLogin.setError(getResources().getString(R.string.dialog_error_edit_text));
                } else {
                    btnUserRegistrate.setEnabled(true);
                    tilUserLogin.setError(null);
                    tilUserPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etUserLogin.length() == 0 || etUserPassword.length() == 0) {
                    btnUserRegistrate.setEnabled(false);
                    tilUserLogin.setErrorEnabled(true);
                    tilUserLogin.setError(getResources().getString(R.string.dialog_error_edit_text));
                }
            }
        });

        etUserPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (etUserPassword.length() == 0 || etUserLogin.length() == 0) {
                    btnUserRegistrate.setEnabled(false);
                    tilUserPassword.setErrorEnabled(true);
                    tilUserPassword.setError(getResources().getString(R.string.dialog_error_edit_text));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    tilUserPassword.setError(null);
                }
                if (etUserPassword.length() == 0 || etUserLogin.length() == 0) {
                    btnUserRegistrate.setEnabled(false);
                    tilUserPassword.setErrorEnabled(true);
                    tilUserPassword.setError(getResources().getString(R.string.dialog_error_edit_text));
                } else {
                    btnUserRegistrate.setEnabled(true);
                    tilUserPassword.setError(null);
                    tilUserLogin.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etUserPassword.length() == 0 || etUserLogin.length() == 0) {
                    btnUserRegistrate.setEnabled(false);
                    tilUserPassword.setErrorEnabled(true);
                    tilUserPassword.setError(getResources().getString(R.string.dialog_error_edit_text));
                }
            }
        });
    }

    private void setEditTexts() {
        etUserJob = tilUserJob.getEditText();

        tilUserJob.setHint(getResources().getString(R.string.signup_hint_job));

        tilUserLogin.setError(getResources().getString(R.string.dialog_error_edit_text));
        tilUserLogin.setHint(getResources().getString(R.string.signup_hint_login));
        etUserLogin.setHint(getResources().getString(R.string.signup_hint_login));

        tilUserPassword.setError(getResources().getString(R.string.dialog_error_edit_text));
        tilUserPassword.setHint(getResources().getString(R.string.signup_hint_password));
        etUserPassword.setHint(getResources().getString(R.string.signup_hint_password));
    }

    @OnClick(R.id.btn_user_accept)
    public void onRegistrateClick() {
        Intent intent = new Intent(this, MainOnlineActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
