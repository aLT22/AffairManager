package com.bytebuilding.affairmanager.adapters.realm;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.model.realm.UserAffair;
import com.bytebuilding.affairmanager.utils.DateUtils;

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

        holder.userAffairTitle.setText(userAffair.getTitle());
        holder.userAffairDescription.setText(userAffair.getDescription());
        holder.userAffairDate.setText(DateUtils.getDate(userAffair.getDate()));
        holder.userAffairTime.setText(DateUtils.getTime(userAffair.getTime()));
    }

    public class UserAffairsViewHolder extends RecyclerView.ViewHolder {

        public UserAffair data = null;

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
