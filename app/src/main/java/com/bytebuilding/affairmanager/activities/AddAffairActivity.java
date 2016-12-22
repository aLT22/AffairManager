package com.bytebuilding.affairmanager.activities;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.database.DBHelper;
import com.bytebuilding.affairmanager.dialogs.DatePickerFragment;
import com.bytebuilding.affairmanager.dialogs.TimePickerFragment;
import com.bytebuilding.affairmanager.model.Affair;
import com.bytebuilding.affairmanager.utils.DateUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAffairActivity extends AppCompatActivity {

    public static final String TTILE = "title";
    public static final String DESCRIPTION = "desc";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String OBJECT = "object";
    public static final String TYPE = "type";
    public static final String PLACE = "place";
    public static final String PRIORITY = "priority";
    public static final String REPEATS_TYPE = "rep_type";
    public static final String REPEATS_TIME = "rep_time";

    @BindView(R.id.act_affair_title)
    TextInputLayout actAffairTitle;
    @BindView(R.id.act_affair_date)
    TextInputLayout actAffairDate;
    @BindView(R.id.act_affair_time)
    TextInputLayout actAffairTime;
    @BindView(R.id.act_affair_description)
    TextInputLayout actAffairDescription;
    @BindView(R.id.act_affair_object)
    TextInputLayout actAffairObject;
    @BindView(R.id.act_affair_type)
    TextInputLayout actAffairType;
    @BindView(R.id.act_affair_place)
    TextInputLayout actAffairPlace;
    @BindView(R.id.act_spinner_affair_priority)
    Spinner actSpinnerAffairPriority;
    @BindView(R.id.act_spinner_affair_repeats_type)
    Spinner actSpinnerAffairRepeatsType;
    @BindView(R.id.act_spinner_affair_repeats_time)
    Spinner actSpinnerAffairRepeatsTime;
    @BindView(R.id.button_decline)
    Button buttonDecline;
    @BindView(R.id.button_accept)
    Button buttonAccept;

    private Calendar calendar = Calendar.getInstance();

    private EditText etTitle, etDate, etTime, etDescription, etObject, etType, etPlace;

    private DBHelper dbHelper;

    private Affair affair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_affair);
        ButterKnife.bind(this);

        getEditTexts();
        setSpinnersContent();

        dbHelper = new DBHelper(this);

        affair = new Affair();

        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);

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
    }

    private void getEditTexts() {
        etTitle = actAffairTitle.getEditText();
        etDate = actAffairDate.getEditText();
        etTime = actAffairTime.getEditText();
        etDescription = actAffairDescription.getEditText();
        etObject = actAffairObject.getEditText();
        etType = actAffairType.getEditText();
        etPlace = actAffairPlace.getEditText();

        etTitle.setHint(getString(R.string.dialog_title_hint));
        etDate.setHint(getString(R.string.dialog_date_hint));
        etTime.setHint(getString(R.string.dialog_time_hint));
        etDescription.setHint(getString(R.string.dialog_description_hint));
        etObject.setHint(getString(R.string.dialog_object_hint));
        etPlace.setHint(getString(R.string.dialog_place_hint));
        etType.setHint(getString(R.string.dialog_type_hint));
    }

    private void setSpinnersContent() {
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources()
                .getStringArray(R.array.affair_priorities));
        actSpinnerAffairPriority.setAdapter(priorityAdapter);

        actSpinnerAffairPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        ArrayAdapter<String> repeatsTypeAdapter = new ArrayAdapter<String>(this, android.R.layout
                .simple_spinner_dropdown_item, getResources().getStringArray(R.array.affair_repeats_type));
        actSpinnerAffairRepeatsType.setAdapter(repeatsTypeAdapter);

        actSpinnerAffairRepeatsType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        ArrayAdapter<String> repeatsTimeAdapterHours = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_spinner_dropdown_item, getResources()
                                .getStringArray(R.array.affair_repeats_by_hours));
                        actSpinnerAffairRepeatsTime.setAdapter(repeatsTimeAdapterHours);
                        break;

                    case 1:
                        ArrayAdapter<String> repeatsTimeAdapterDays = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_spinner_dropdown_item, getResources()
                                .getStringArray(R.array.affair_repeats_by_days));
                        actSpinnerAffairRepeatsTime.setAdapter(repeatsTimeAdapterDays);
                        break;

                    case 2:
                        ArrayAdapter<String> repeatsTimeAdapterWeeks = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_spinner_dropdown_item, getResources()
                                .getStringArray(R.array.affair_repeats_by_weeks));
                        actSpinnerAffairRepeatsTime.setAdapter(repeatsTimeAdapterWeeks);
                        break;

                    case 3:
                        ArrayAdapter<String> repeatsTimeAdapterMonths = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_spinner_dropdown_item, getResources()
                                .getStringArray(R.array.affair_repeats_by_months));
                        actSpinnerAffairRepeatsTime.setAdapter(repeatsTimeAdapterMonths);
                        break;

                    case 4:
                        ArrayAdapter<String> repeatsTimeAdapterYears = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_spinner_dropdown_item, getResources()
                                .getStringArray(R.array.affair_repeats_by_years));
                        actSpinnerAffairRepeatsTime.setAdapter(repeatsTimeAdapterYears);
                        break;

                    default:break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.button_accept)
    public void onButtonAcceptClicked() {
        affair.setTitle(etTitle.getText().toString());
        affair.setDescription(etDescription.getText().toString());
        affair.setTimestamp(Calendar.getInstance().getTimeInMillis());
        affair.setType(etType.getText().toString());
        affair.setObject(etObject.getText().toString());
        affair.setPlace(etPlace.getText().toString());
        affair.setStatus(Affair.STATUS_CURRENT);
        affair.setPriority(actSpinnerAffairPriority.getSelectedItemPosition());

        if (etDate.length() != 0 || etTime.length() != 0) {
            affair.setDate(calendar.getTimeInMillis());
            affair.setTime(calendar.getTimeInMillis());
        }

        dbHelper.saveAffair(affair);
        this.finish();
    }

    @OnClick(R.id.button_decline)
    public void onButtonDeclineClick() {
        Toast.makeText(getApplicationContext(), "Задача не добавлена", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (etTitle.length() == 0) {
            buttonAccept.setEnabled(false);
            actAffairTitle.setErrorEnabled(true);
            actAffairTitle.setError(getResources().getString(R.string.dialog_error_edit_text));
        }

        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    buttonAccept.setEnabled(false);
                    actAffairTitle.setError(getResources().getString(R.string
                            .dialog_error_edit_text));
                } else {
                    buttonAccept.setEnabled(true);
                    actAffairTitle.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}