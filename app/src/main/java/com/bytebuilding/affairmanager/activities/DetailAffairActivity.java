package com.bytebuilding.affairmanager.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.utils.DateUtils;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.internal.Utils;

public class DetailAffairActivity extends AppCompatActivity {

    @BindView(R.id.detailActivity_title)
    TextView title;
    @BindView(R.id.detailActivity_title_dynamic)
    TextView titleDynamic;
    @BindView(R.id.detailActivity_description)
    TextView description;
    @BindView(R.id.detailActivity_description_dynamic)
    TextView descriptionDynamic;
    @BindView(R.id.detailActivity_date)
    TextView date;
    @BindView(R.id.detailActivity_date_dynamic)
    TextView dateDynamic;
    @BindView(R.id.detailActivity_time)
    TextView time;
    @BindView(R.id.detailActivity_time_dynamic)
    TextView timeDynamic;
    @BindView(R.id.detailActivity_object)
    TextView object;
    @BindView(R.id.detailActivity_object_dynamic)
    TextView objectDynamic;
    @BindView(R.id.detailActivity_type)
    TextView type;
    @BindView(R.id.detailActivity_type_dynamic)
    TextView typeDynamic;
    @BindView(R.id.detailActivity_place)
    TextView place;
    @BindView(R.id.detailActivity_place_dynamic)
    TextView placeDynamic;
    @BindView(R.id.detailActivity_status)
    TextView status;
    @BindView(R.id.detailActivity_status_dynamic)
    TextView statusDynamic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_affair);

        ButterKnife.bind(this);
        getExtras();
    }

    private void getExtras() {
        Intent outerIntent = getIntent();
        Bundle outerBundle = outerIntent.getExtras();

        titleDynamic.setText(outerBundle.getString("title"));
        descriptionDynamic.setText(outerBundle.getString("description"));
        dateDynamic.setText(DateUtils.getDate(outerBundle.getLong("date")));
        timeDynamic.setText(DateUtils.getTime(outerBundle.getLong("time")));
        objectDynamic.setText(outerBundle.getString("object"));
        typeDynamic.setText(outerBundle.getString("type"));
        placeDynamic.setText(outerBundle.getString("place"));
        if (outerBundle.getInt("status") == 2) {
            statusDynamic.setText(R.string.detailActivity_status_done);
        } else {
            statusDynamic.setText(R.string.detailActivity_status_current);
        }
    }
}