package com.bytebuilding.affairmanager.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.model.Affair;
import com.bytebuilding.affairmanager.notifications.OfflineNotificationHelper;
import com.bytebuilding.affairmanager.utils.DateUtils;

import java.util.Calendar;

public class EditAffairDialogFragment extends DialogFragment {

    private EditingAffairListener editingAffairListener;

    public static EditAffairDialogFragment newInstance(Affair affair) {
        EditAffairDialogFragment editAffairDialogFragment = new EditAffairDialogFragment();

        Bundle args = new Bundle();
        args.putString("title", affair.getTitle());
        args.putLong("date", affair.getDate());
        args.putLong("time", affair.getTime());
        args.putString("description", affair.getDescription());
        args.putString("object", affair.getObject());
        args.putString("type", affair.getType());
        args.putString("place", affair.getPlace());
        args.putInt("priority", affair.getPriority());
        args.putLong("timestamp", affair.getTimestamp());
        args.putLong("repeate_timestamp", affair.getRepeatTimestamp());

        editAffairDialogFragment.setArguments(args);

        return editAffairDialogFragment;
    }

    public interface EditingAffairListener {
        void onAffairEdited(Affair updatedAffair);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            editingAffairListener = (EditingAffairListener) activity;
        } catch (ClassCastException cce) {
            throw new ClassCastException(activity.toString() + "have to implement interface EditingAffairListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();

        String title = args.getString("title");
        long date = args.getLong("date");
        long time = args.getLong("time");
        String description = args.getString("description");
        String object = args.getString("object");
        String type = args.getString("type");
        String place = args.getString("place");
        int priority = args.getInt("priority");
        long timestamp = args.getLong("timestamp");
        long repeatetimestamp = args.getLong("repeate_timestamp");

        final Affair affair = new Affair(title, description, date, time, priority, object, type, place,
                repeatetimestamp, timestamp, 0);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_add_affair, null);

        final TextInputLayout tilTitle = (TextInputLayout) container.findViewById(R.id.til_affair_title);
        final EditText etTitle = tilTitle.getEditText();

        final TextInputLayout tilDate = (TextInputLayout) container.findViewById(R.id.til_affair_date);
        final EditText etDate = tilDate.getEditText();

        TextInputLayout tilTime = (TextInputLayout) container.findViewById(R.id.til_affair_time);
        final EditText etTime = tilTime.getEditText();

        TextInputLayout tilDescription = (TextInputLayout) container.findViewById(R.id.til_affair_description);
        final EditText etDescription = tilDescription.getEditText();

        TextInputLayout tilObject = (TextInputLayout) container.findViewById(R.id.til_affair_object);
        final EditText etObject = tilObject.getEditText();

        TextInputLayout tilType = (TextInputLayout) container.findViewById(R.id.til_affair_type);
        final EditText etType = tilType.getEditText();

        TextInputLayout tilPlace = (TextInputLayout) container.findViewById(R.id.til_affair_place);
        final EditText etPlace = tilPlace.getEditText();

        final Spinner spinnerAffairPriority = (Spinner) container.findViewById(R.id.spinner_affair_priority);

        Spinner spinnerAffairRepeatsType = (Spinner) container.findViewById(R.id.spinner_affair_repeats_type);

        final Spinner spinnerAffairRepeatsTime = (Spinner) container.findViewById(R.id.spinner_affair_repeats_time);

        tilTitle.setHint(getString(R.string.dialog_title_hint));
        tilDate.setHint(getString(R.string.dialog_date_hint));
        tilTime.setHint(getString(R.string.dialog_time_hint));
        tilDescription.setHint(getString(R.string.dialog_description_hint));
        tilObject.setHint(getString(R.string.dialog_object_hint));
        tilPlace.setHint(getString(R.string.dialog_place_hint));
        tilType.setHint(getString(R.string.dialog_type_hint));

        spinnerAffairPriority.setPrompt("Выберите приоритет");

        alertDialogBuilder.setView(container);

        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, getResources()
                .getStringArray(R.array.affair_priorities));
        spinnerAffairPriority.setAdapter(priorityAdapter);

        spinnerAffairPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                affair.setPriority(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);
        if (etDate.length() != 0 || etTime.length() != 0) {
            calendar.setTimeInMillis(affair.getDate());
        }

        ArrayAdapter<String> repeatsTypeAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, getResources()
                .getStringArray(R.array.affair_repeats_type));
        spinnerAffairRepeatsType.setAdapter(repeatsTypeAdapter);

        spinnerAffairRepeatsType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        ArrayAdapter<String> repeatsTimeAdapterHours = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_dropdown_item, getResources()
                                .getStringArray(R.array.affair_repeats_by_hours));
                        spinnerAffairRepeatsTime.setAdapter(repeatsTimeAdapterHours);
                        break;

                    case 1:
                        ArrayAdapter<String> repeatsTimeAdapterDays = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_dropdown_item, getResources()
                                .getStringArray(R.array.affair_repeats_by_days));
                        spinnerAffairRepeatsTime.setAdapter(repeatsTimeAdapterDays);
                        break;

                    case 2:
                        ArrayAdapter<String> repeatsTimeAdapterWeeks = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_dropdown_item, getResources()
                                .getStringArray(R.array.affair_repeats_by_weeks));
                        spinnerAffairRepeatsTime.setAdapter(repeatsTimeAdapterWeeks);
                        break;

                    case 3:
                        ArrayAdapter<String> repeatsTimeAdapterMonths = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_dropdown_item, getResources()
                                .getStringArray(R.array.affair_repeats_by_months));
                        spinnerAffairRepeatsTime.setAdapter(repeatsTimeAdapterMonths);
                        break;

                    case 4:
                        ArrayAdapter<String> repeatsTimeAdapterYears = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_dropdown_item, getResources()
                                .getStringArray(R.array.affair_repeats_by_years));
                        spinnerAffairRepeatsTime.setAdapter(repeatsTimeAdapterYears);
                        break;

                    default:break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etTitle.setText(affair.getTitle());
        if (affair.getDate() != 0) {
            etDate.setText(DateUtils.getDate(affair.getDate()));
        }
        if (affair.getTime() != 0) {
            etTime.setText(DateUtils.getTime(affair.getTime()));
        }
        etDescription.setText(affair.getDescription());
        etType.setText(affair.getType());
        etObject.setText(affair.getObject());
        etPlace.setText(affair.getPlace());

        spinnerAffairPriority.setSelection(affair.getPriority());

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etDate.length() == 0) {
                    etDate.setText("   ");
                }

                DialogFragment datePickerFragment = new DatePickerFragment() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        etDate.setText(DateUtils.getDate(calendar.getTimeInMillis()));
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
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);

                        etTime.setText(DateUtils.getTime(calendar.getTimeInMillis()));
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        etTime.setText("");
                    }
                };
                timePickerFragment.show(getFragmentManager(), "TimePickerFragment");
            }
        });

        alertDialogBuilder.setPositiveButton(R.string.button_accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                affair.setTitle(etTitle.getText().toString());
                affair.setDescription(etDescription.getText().toString());
                affair.setTimestamp(calendar.getTimeInMillis());
                affair.setType(etType.getText().toString());
                affair.setObject(etObject.getText().toString());
                affair.setPlace(etPlace.getText().toString());
                affair.setStatus(Affair.STATUS_CURRENT);

                if (etDate.length() != 0 || etTime.length() != 0) {
                    affair.setDate(calendar.getTimeInMillis());
                    affair.setTime(calendar.getTimeInMillis());

                    OfflineNotificationHelper offlineNotificationHelper = OfflineNotificationHelper.getInstance();
                    offlineNotificationHelper.setReceiver(affair);
                }

                editingAffairListener.onAffairEdited(affair);
                dialog.dismiss();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.button_decline, new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
                    tilTitle.setErrorEnabled(true);
                    tilTitle.setError(getResources().getString(R.string.dialog_error_edit_text));
                }

                etTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            acceptButton.setEnabled(false);
                            tilTitle.setError(getResources().getString(R.string
                                    .dialog_error_edit_text));
                        } else {
                            acceptButton.setEnabled(true);
                            tilTitle.setError(null);
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
