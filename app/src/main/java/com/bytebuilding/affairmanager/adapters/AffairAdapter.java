package com.bytebuilding.affairmanager.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bytebuilding.affairmanager.fragments.OfflineAffairFragment;
import com.bytebuilding.affairmanager.model.Item;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class AffairAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Item> items;
    private OfflineAffairFragment offlineAffairFragment;

    public AffairAdapter(OfflineAffairFragment offlineAffairFragment) {
        this.offlineAffairFragment = offlineAffairFragment;
        items = new ArrayList<>();
    }

    public Item getItem(int position) {
        return items.get(position);
    }

    public void addItem(Item item) {
        items.add(item);
        /*сообщаем о добавлении нового элемента списка*/
        notifyItemInserted(getItemCount() - 1);
    }

    public void addItem(int location, Item item) {
        items.add(location, item);
        notifyItemInserted(location);
    }

    public void removeItem(int position) {
        if (position >= 0 && position <= getItemCount() - 1) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public OfflineAffairFragment getOfflineAffairFragment() {
        return offlineAffairFragment;
    }

    protected class AffairViewHolder extends RecyclerView.ViewHolder {

        protected View affairModelContainer;

        protected CircleImageView affairModelCircleImageView;

        protected TextView affairModelTitle;
        protected TextView affairModelDescription;
        protected TextView affairModelDate;
        protected TextView affairModelTime;

        public AffairViewHolder(View itemView, View affairModelContainer,
                                CircleImageView affairModelCircleImageView,
                                TextView affairModelTitle, TextView affairModelDescription,
                                TextView affairModelDate, TextView affairModelTime) {
            super(itemView);
            this.affairModelContainer = affairModelContainer;
            this.affairModelCircleImageView = affairModelCircleImageView;
            this.affairModelTitle = affairModelTitle;
            this.affairModelDescription = affairModelDescription;
            this.affairModelDate = affairModelDate;
            this.affairModelTime = affairModelTime;
        }
    }

}
