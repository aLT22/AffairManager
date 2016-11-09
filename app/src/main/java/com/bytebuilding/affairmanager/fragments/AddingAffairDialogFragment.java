package com.bytebuilding.affairmanager.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.bytebuilding.affairmanager.R;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by Alexey on 01.11.2016.
 */

public class AddingAffairDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_add_affair, null);

        MaterialEditText metTitle = (MaterialEditText) container
                .findViewById(R.id.met_affair_title);
        MaterialEditText metDate = (MaterialEditText) container.findViewById(R.id.met_affair_date);
        MaterialEditText metDescription = (MaterialEditText) container
                .findViewById(R.id.met_affair_description);
        MaterialEditText metTime = (MaterialEditText) container.findViewById(R.id.met_affair_time);
        MaterialEditText metObject = (MaterialEditText) container
                .findViewById(R.id.met_affair_object);
        MaterialEditText metType = (MaterialEditText) container.findViewById(R.id.met_affair_type);
        MaterialEditText metPlace = (MaterialEditText) container
                .findViewById(R.id.met_affair_place);

        alertDialogBuilder.setView(container);

        return super.onCreateDialog(savedInstanceState);
    }
}
