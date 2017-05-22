package com.bytebuilding.affairmanager.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.activities.SplashScreen;
import com.bytebuilding.affairmanager.model.realm.User;
import com.bytebuilding.affairmanager.utils.CryptoUtils;
import com.bytebuilding.affairmanager.utils.PasswordGenerator;

import io.realm.Realm;

/**
 * Created by Turkin A. on 11.05.17.
 */

public class ChangePasswordDialogFragment extends DialogFragment {

    private SharedPreferences preferences;

    private PasswordChangedListener passwordChangedListener;

    private boolean checkClick;

    public interface PasswordChangedListener {

        void onPasswordChanged(String password, boolean flag);

        void onPasswordChangedCancel();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            passwordChangedListener = (PasswordChangedListener) activity;
        } catch (ClassCastException cca) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement AddingUserGroupAffairListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        checkClick = false;

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        preferences = getActivity().getSharedPreferences(SplashScreen.PREFERENCES_NAME, Context.MODE_PRIVATE);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View containerDialogFragment = inflater.inflate(R.layout.dialog_change_password, null);

        final EditText etPassword = (EditText) containerDialogFragment.findViewById(R.id.et_change_password);
        etPassword.setText(PasswordGenerator.newGeneratedPassword());

        builder.setView(containerDialogFragment);

        builder.setTitle(getString(R.string.dialog_change_password_title));

        builder.setPositiveButton(getString(R.string.button_accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preferences.edit().putString("password", CryptoUtils.encrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, etPassword.getText().toString())).apply();
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        User user = realm.where(User.class).equalTo("userId", preferences.getLong("id", 0)).findFirst();
                        if (user != null) {
                            user.setUserPassword(CryptoUtils.encrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, etPassword.getText().toString()));
                            realm.insertOrUpdate(user);
                        }
                    }
                });
                passwordChangedListener.onPasswordChanged(preferences.getString("password", "password"), true);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.button_decline), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                passwordChangedListener.onPasswordChangedCancel();

                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();

        return alertDialog;
    }
}
