package com.bytebuilding.affairmanager.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.utils.DateUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

/**
 * Created by Alexey on 01.11.2016.
 */

public class AddingAffairDialogFragment extends DialogFragment {

    private AddingTaskListener addingTaskListener;

    public interface AddingTaskListener {
        void onTaskAdded();
        void onTaskAddingCancel();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        try {
            addingTaskListener = (AddingTaskListener) activity;
        } catch (ClassCastException cca) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement AddingTaskListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.title_adding_affair_dialog_fragment);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View container = inflater.inflate(R.layout.dialog_add_affair, null);

        final MaterialEditText metTitle = (MaterialEditText) container
                .findViewById(R.id.met_affair_title);
        final MaterialEditText metDate = (MaterialEditText) container.findViewById(R.id
                .met_affair_date);
        MaterialEditText metDescription = (MaterialEditText) container
                .findViewById(R.id.met_affair_description);
        final MaterialEditText metTime = (MaterialEditText) container.findViewById(R.id
                .met_affair_time);
        MaterialEditText metObject = (MaterialEditText) container
                .findViewById(R.id.met_affair_object);
        MaterialEditText metType = (MaterialEditText) container.findViewById(R.id
                .met_affair_type);
        MaterialEditText metPlace = (MaterialEditText) container
                .findViewById(R.id.met_affair_place);

        alertDialogBuilder.setView(container);

        metDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (metDate.length() == 0) {
                    metDate.setText("   ");
                }

                DialogFragment datePickerFragment = new DatePickerFragment() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar dateCalendar = Calendar.getInstance();
                        dateCalendar.set(year, month, dayOfMonth);

                        metDate.setText(DateUtils.getDate(dateCalendar.getTimeInMillis()));
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        metDate.setText("");
                    }
                };
                datePickerFragment.show(getFragmentManager(), "DatePickerFragment");
            }
        });

        metTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (metTime.length() == 0) {
                    metTime.setText("   ");
                }

                DialogFragment timePickerFragment = new TimePickerFragment() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar timeCalendar = Calendar.getInstance();
                        timeCalendar.set(0, 0, 0, hourOfDay, minute);

                        metTime.setText(DateUtils.getTime(timeCalendar.getTimeInMillis()));
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        metTime.setText("");
                    }
                };
                timePickerFragment.show(getFragmentManager(), "TimePickerFragment");
            }
        });

        alertDialogBuilder.setPositiveButton(R.string.button_accept, new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addingTaskListener.onTaskAdded();
                dialog.dismiss();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.button_decline, new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addingTaskListener.onTaskAddingCancel();
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button acceptButton = ((AlertDialog) dialog).getButton(DialogInterface
                        .BUTTON_POSITIVE);

                if (metTitle.length() == 0) {
                    acceptButton.setEnabled(false);
                    metTitle.setError(getResources().getString(R.string.dialog_error_edit_text));
                }

                metTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            acceptButton.setEnabled(false);
                            metTitle.setError(getResources().getString(R.string
                                    .dialog_error_edit_text));
                        } else {
                            acceptButton.setEnabled(true);
                            metTitle.setError(null);
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