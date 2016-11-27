package com.bytebuilding.affairmanager.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.fragments.OfflineAffairFragment;
import com.bytebuilding.affairmanager.model.Affair;
import com.bytebuilding.affairmanager.model.Item;
import com.bytebuilding.affairmanager.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CurrentOfflineAffairsAdapter extends AffairAdapter {

    private List<Item> items = new ArrayList<>();

    public static final int AFFAIR_TYPE = 0;
    public static final int SEPARATOR_TYPE = 1;

    public CurrentOfflineAffairsAdapter(OfflineAffairFragment currentOfflineAffairFragment) {
        super(currentOfflineAffairFragment);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case AFFAIR_TYPE:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.affair_model,
                        parent, false);
                View affairModelContainer = view.findViewById(R.id.affair_model_container);

                CircleImageView affairModelCircleImageView = (CircleImageView) view
                        .findViewById(R.id.civ_affair_model);

                TextView affairModelTitle = (TextView) view.findViewById(R.id.affair_model_title);
                TextView affairModelDescription = (TextView) view.findViewById(R.id
                        .affair_model_description);
                TextView affairModelDate = (TextView) view.findViewById(R.id
                        .affair_model_date);
                TextView affairModelTime = (TextView) view.findViewById(R.id.affair_model_time);

                return new AffairViewHolder(view, affairModelContainer, affairModelCircleImageView,
                        affairModelTitle, affairModelDescription, affairModelDate, affairModelTime);
            case SEPARATOR_TYPE:

                break;
            default:
                return null;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Item item = items.get(position);

        if (item.isAffair()) {
            holder.itemView.setEnabled(true);
            Affair  affair = (Affair) item;
            AffairViewHolder affairViewHolder = (AffairViewHolder) holder;

            affairViewHolder.affairModelTitle.setText(affair.getTitle());
            affairViewHolder.affairModelDescription.setText(affair.getDescription());

            if (affair.getPriority() == 0) {
                affairViewHolder.affairModelCircleImageView.setBackgroundColor(Color.GREEN);
            } else if (affair.getPriority() == 1) {
                affairViewHolder.affairModelCircleImageView.setBackgroundColor(Color.YELLOW);
            } else {
                affairViewHolder.affairModelCircleImageView.setBackgroundColor(Color.RED);
            }

            if (affair.getDate() != 0) {
                affairViewHolder.affairModelDate.setText(DateUtils.getDate(affair.getTimestamp()));
            } else {
                affairViewHolder.affairModelDate.setText("Дата не указана");
            }

            if (affair.getTime() != 0) {
                affairViewHolder.affairModelTime.setText(DateUtils.getTime(affair.getTimestamp()));
            } else {
                affairViewHolder.affairModelTime.setText("Время не указано");
            }
        } else {
            holder.itemView.setEnabled(false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isAffair()) {
            return AFFAIR_TYPE;
        } else {
            return SEPARATOR_TYPE;
        }
    }

}
