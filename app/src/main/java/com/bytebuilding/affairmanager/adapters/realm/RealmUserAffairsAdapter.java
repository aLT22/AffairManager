package com.bytebuilding.affairmanager.adapters.realm;

import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.model.realm.UserAffair;
import com.bytebuilding.affairmanager.utils.DateUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Turkin A. on 03.05.17.
 */

public class RealmUserAffairsAdapter extends RealmRecyclerViewAdapter<UserAffair, RealmUserAffairsAdapter.UserAffairsViewHolder> {


    public RealmUserAffairsAdapter(@Nullable OrderedRealmCollection<UserAffair> data, boolean autoUpdate) {
        super(data, autoUpdate);
    }

    @Override
    public RealmUserAffairsAdapter.UserAffairsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.affair_model, parent, false);

        return new UserAffairsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RealmUserAffairsAdapter.UserAffairsViewHolder holder, int position) {
        final UserAffair userAffair = getItem(position);

        holder.data = userAffair;

        final Resources resources = holder.itemView.getResources();

        holder.userAffairTitle.setText(holder.data.getTitle());
        holder.userAffairDescription.setText(holder.data.getDescription());
        if (holder.data.getDate() != 0) {
            holder.userAffairDate.setText(DateUtils.getDate(holder.data.getTimestamp()));
        } else {
            holder.userAffairDate.setText("-");
        }

        if (holder.data.getTime() != 0) {
            holder.userAffairTime.setText(DateUtils.getTime(holder.data.getTimestamp()));
        } else {
            holder.userAffairTime.setText("-");
        }

        holder.userAffairPriority.setEnabled(true);

        if (holder.data.getDate() != 0 && holder.data.getDate() < Calendar.getInstance().getTimeInMillis()) {
            holder.userAffairContainer.setBackgroundColor(resources.getColor(R.color.color_primary_light));
        }

        holder.userAffairPriority.setImageResource(R.drawable.ic_checkbox_blank_circle_white_48dp);
        holder.userAffairPriority.setColorFilter(resources.getColor(holder.data.getColor()));

        holder.userAffairTitle.setTextColor(resources.getColor(R.color.color_primary_text));

        holder.userAffairDescription.setTextColor(resources.getColor(R.color.color_secondary_text));

        holder.userAffairDate.setTextColor(resources.getColor(R.color.color_primary_text));

        holder.userAffairTime.setTextColor(resources.getColor(R.color.color_primary_text));
    }

    public class UserAffairsViewHolder extends RecyclerView.ViewHolder {

        public UserAffair data;

        @BindView(R.id.affair_model_container)
        View userAffairContainer;

        @BindView(R.id.civ_affair_model)
        CircleImageView userAffairPriority;

        @BindView(R.id.affair_model_title)
        TextView userAffairTitle;

        @BindView(R.id.affair_model_description)
        TextView userAffairDescription;

        @BindView(R.id.affair_model_date)
        TextView userAffairDate;

        @BindView(R.id.affair_model_time)
        TextView userAffairTime;


        public UserAffairsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
