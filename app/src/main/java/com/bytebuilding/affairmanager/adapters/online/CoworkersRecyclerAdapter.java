package com.bytebuilding.affairmanager.adapters.online;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.model.online.Coworker;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Turkin A. on 15.05.17.
 */

public class CoworkersRecyclerAdapter extends RecyclerView.Adapter<CoworkersRecyclerAdapter.ViewHolder> {

    private static final String TAG = CoworkersRecyclerAdapter.class.getSimpleName();

    private Context mContext;

    private List<Coworker> mList;

    public CoworkersRecyclerAdapter(Context context, List<Coworker> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.coworkers_model, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Coworker item = mList.get(position);
        holder.data = item;

        holder.username.setText(holder.data.getUsername());
        holder.job.setText(holder.data.getJob());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Coworker item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private Coworker data = null;

        @BindView(R.id.coworkers_container)
        View container;

        @BindView(R.id.coworkers_username)
        TextView username;

        @BindView(R.id.coworkers_job)
        TextView job;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}