package com.bytebuilding.affairmanager.dialogs;

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
import com.bytebuilding.affairmanager.utils.PasswordGenerator;

/**
 * Created by Turkin A. on 11.05.17.
 */

public class ChangePasswordDialogFragment extends DialogFragment {

    private SharedPreferences preferences;

    private PasswordChangedListener passwordChangedListener;

    public interface PasswordChangedListener {

        void onPasswordChanged();

        void onPasswordChangedCancel();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            passwordChangedListener = (PasswordChangedListener) getActivity();
        } catch (ClassCastException cca) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement AddingUserGroupAffairListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        preferences = getActivity().getSharedPreferences(SplashScreen.PREFERENCES_NAME, Context.MODE_PRIVATE);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View containerDialogFragment = inflater.inflate(R.layout.dialog_change_password, null);

        final EditText etPassword = (EditText) containerDialogFragment.findViewById(R.id.et_change_password);
        etPassword.setText(PasswordGenerator.newGeneratedPassword());

        final String newPassword = etPassword.getText().toString();

        builder.setTitle(getString(R.string.dialog_change_password_title));

        builder.setPositiveButton(getString(R.string.button_accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preferences.edit().putString("password", newPassword).apply();
                passwordChangedListener.onPasswordChanged();

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
