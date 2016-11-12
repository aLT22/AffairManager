package com.bytebuilding.affairmanager.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.title_adding_affair_dialog_fragment);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_add_affair, null);

        MaterialEditText metTitle = (MaterialEditText) container
                .findViewById(R.id.met_affair_title);
        final MaterialEditText metDate = (MaterialEditText) container.findViewById(R.id.met_affair_date);
        MaterialEditText metDescription = (MaterialEditText) container
                .findViewById(R.id.met_affair_description);
        final MaterialEditText metTime = (MaterialEditText) container.findViewById(R.id.met_affair_time);
        MaterialEditText metObject = (MaterialEditText) container
                .findViewById(R.id.met_affair_object);
        MaterialEditText metType = (MaterialEditText) container.findViewById(R.id.met_affair_type);
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
                        metDate.setText(null);
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
                        metTime.setText(null);
                    }
                };
            }
        });

        return super.onCreateDialog(savedInstanceState);
    }
}
