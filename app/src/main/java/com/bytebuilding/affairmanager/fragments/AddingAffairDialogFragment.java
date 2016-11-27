package com.bytebuilding.affairmanager.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.DialogFragment;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.utils.DateUtils;

import java.util.Calendar;

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

        View container = inflater.inflate(R.layout.dialog_add_affair, null);

        TextInputLayout tilTitle = (TextInputLayout) container
                .findViewById(R.id.til_affair_title);
        final EditText etTitle = tilTitle.getEditText();

        final TextInputLayout tilDate = (TextInputLayout) container.findViewById(R.id
                .til_affair_date);
        final EditText etDate = tilDate.getEditText();

        TextInputLayout tilTime = (TextInputLayout) container
                .findViewById(R.id.til_affair_time);
        final EditText etTime = tilTime.getEditText();

        TextInputLayout tilDescription = (TextInputLayout) container.findViewById(R.id
                .til_affair_description);
        EditText etDescription = tilDescription.getEditText();

        TextInputLayout tilObject = (TextInputLayout) container
                .findViewById(R.id.til_affair_object);
        EditText etObject = tilObject.getEditText();

        TextInputLayout tilType = (TextInputLayout) container.findViewById(R.id
                .til_affair_type);
        EditText etType = tilType.getEditText();

        TextInputLayout tilPlace = (TextInputLayout) container
                .findViewById(R.id.til_affair_place);
        EditText etPlace = tilPlace.getEditText();

        Spinner spinnerAffairPriority = (Spinner) container.findViewById(R.id
                .spinner_affair_priority);

        Spinner spinnerAffairRepeatsType = (Spinner) container.findViewById(R.id
                .spinner_affair_repeats_type);

        Spinner spinnerAffairRepeatsTime = (Spinner) container.findViewById(R.id
                .spinner_affair_repeats_time);

        tilTitle.setHint(getString(R.string.dialog_title_hint));
        tilDate.setHint(getString(R.string.dialog_date_hint));
        tilTime.setHint(getString(R.string.dialog_time_hint));
        tilDescription.setHint(getString(R.string.dialog_description_hint));
        tilObject.setHint(getString(R.string.dialog_object_hint));
        tilPlace.setHint(getString(R.string.dialog_place_hint));
        tilType.setHint(getString(R.string.dialog_type_hint));


        alertDialogBuilder.setView(container);

        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, getResources()
                .getStringArray(R.array.affair_priorities));
        spinnerAffairPriority.setAdapter(priorityAdapter);

        ArrayAdapter<String> repeatsTypeAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, getResources()
                .getStringArray(R.array.affair_repeats_type));
        spinnerAffairRepeatsType.setAdapter(repeatsTypeAdapter);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etDate.length() == 0) {
                    etDate.setText("   ");
                }

                DialogFragment datePickerFragment = new DatePickerFragment() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar dateCalendar = Calendar.getInstance();
                        dateCalendar.set(year, month, dayOfMonth);

                        etDate.setText(DateUtils.getDate(dateCalendar.getTimeInMillis()));
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        etDate.setText("");
                    }
                };
                datePickerFragment.show(getFragmentManager(), "DatePickerFragment");
            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etTime.length() == 0) {
                    etTime.setText("   ");
                }

                DialogFragment timePickerFragment = new TimePickerFragment() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar timeCalendar = Calendar.getInstance();
                        timeCalendar.set(0, 0, 0, hourOfDay, minute);

                        etTime.setText(DateUtils.getTime(timeCalendar.getTimeInMillis()));
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        etTime.setText("");
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

                if (etTitle.length() == 0) {
                    acceptButton.setEnabled(false);
                    etTitle.setError(getResources().getString(R.string.dialog_error_edit_text));
                }

                etTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            acceptButton.setEnabled(false);
                            etTitle.setError(getResources().getString(R.string
                                    .dialog_error_edit_text));
                        } else {
                            acceptButton.setEnabled(true);
                            etTitle.setError(null);
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