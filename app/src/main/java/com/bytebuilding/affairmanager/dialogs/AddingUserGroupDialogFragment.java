package com.bytebuilding.affairmanager.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import java.util.Calendar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.activities.SplashScreen;
import com.bytebuilding.affairmanager.model.realm.User;
import com.bytebuilding.affairmanager.model.realm.UserGroup;

/**
 * Created by Turkin A. on 06.05.17.
 */

public class AddingUserGroupDialogFragment extends DialogFragment {

    private SharedPreferences preferences;

    private AddingUserGroupAffairListener addingUserGroupAffairListener;

    public interface AddingUserGroupAffairListener {

        void onGroupAdded(UserGroup userGroup);

        void onGroupAddingCancel();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            addingUserGroupAffairListener = (AddingUserGroupAffairListener) activity;
        } catch (ClassCastException cca) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement AddingUserGroupAffairListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final Calendar calendar = Calendar.getInstance();

        preferences = getActivity().getSharedPreferences(SplashScreen.PREFERENCES_NAME, Context.MODE_PRIVATE);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View containerDialogFragment = inflater.inflate(R.layout.dialog_add_group, null);

        final TextInputLayout tilGroupTitle = (TextInputLayout) containerDialogFragment.findViewById(R.id.til_group_title);
        final EditText etTitle = tilGroupTitle.getEditText();

        final TextInputLayout tilGroupDescription = (TextInputLayout) containerDialogFragment.findViewById(R.id.til_group_description);
        final EditText etDescription = tilGroupDescription.getEditText();

        tilGroupTitle.setHint(getString(R.string.dialog_add_group_hint_title));
        tilGroupDescription.setHint(getString(R.string.dialog_add_group_hint_description));

        final UserGroup userGroup = new UserGroup();

        builder.setView(containerDialogFragment);

        builder.setPositiveButton(getResources().getString(R.string.button_accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User currentUser = new User();
                currentUser.setUserId(preferences.getLong("id", 0));
                currentUser.setUserLogin(preferences.getString("login", "Non-identified"));
                currentUser.setUserPassword(preferences.getString("password", "null"));
                currentUser.setUserOrganization(preferences.getString("job", "job"));

                userGroup.setUserGroupId(calendar.getTimeInMillis());
                userGroup.addUser(currentUser);
                userGroup.setUserGroupName(etTitle.getText().toString());
                userGroup.setUserGroupDescription(etDescription.getText().toString());
                userGroup.setIdCreator(currentUser.getUserId());

                currentUser.addUserGroup(userGroup);

                addingUserGroupAffairListener.onGroupAdded(userGroup);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.button_decline), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addingUserGroupAffairListener.onGroupAddingCancel();

                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button acceptButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);

                if (etTitle.length() == 0) {
                    acceptButton.setEnabled(false);
                    tilGroupTitle.setErrorEnabled(true);
                    tilGroupTitle.setError(getResources().getString(R.string.dialog_error_edit_text));
                }

                etTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            acceptButton.setEnabled(false);
                            tilGroupTitle.setError(getResources().getString(R.string
                                    .dialog_error_edit_text));
                        } else {
                            acceptButton.setEnabled(true);
                            tilGroupTitle.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        });

        return alertDialog;
    }
}
