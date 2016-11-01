package com.bytebuilding.affairmanager.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.bytebuilding.affairmanager.R;

/**
 * Created by Alexey on 01.11.2016.
 */

public class AddingAffairDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_affair, null);

        alertDialogBuilder.setView(container);

        return super.onCreateDialog(savedInstanceState);
    }
}
