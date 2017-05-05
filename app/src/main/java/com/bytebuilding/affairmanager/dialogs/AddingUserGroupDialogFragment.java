package com.bytebuilding.affairmanager.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import java.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.model.realm.UserGroup;

/**
 * Created by Turkin A. on 06.05.17.
 */

public class AddingUserGroupDialogFragment extends DialogFragment {

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
                    + " must implement AddingAffairListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View containerDialogFragment = inflater.inflate(R.layout.dialog_add_group, null);

        final TextInputLayout tilGroupTitle = (TextInputLayout) containerDialogFragment.findViewById(R.id.til_group_title);
        final EditText etTitle = tilGroupTitle.getEditText();

        final TextInputLayout tilGroupDescription = (TextInputLayout) containerDialogFragment.findViewById(R.id.til_group_description);
        final EditText etDescription = tilGroupDescription.getEditText();

        tilGroupTitle.setHint(getString(R.string.dialog_add_group_hint_title));
        tilGroupDescription.setHint(getString(R.string.dialog_add_group_hint_description));

        final UserGroup userGroup = new UserGroup();

        builder.setPositiveButton(getResources().getString(R.string.button_accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userGroup.setUserGroupId(Calendar.getInstance().getTimeInMillis());
            }
        });

        return super.onCreateDialog(savedInstanceState);
    }
}
