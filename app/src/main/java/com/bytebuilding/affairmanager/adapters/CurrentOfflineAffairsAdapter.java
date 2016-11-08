package com.bytebuilding.affairmanager.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.bytebuilding.affairmanager.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexey on 08.11.2016.
 */

public class CurrentOfflineAffairsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> items = new ArrayList<>();

    public Item getItem(int position) {
        return items.get(position);
    }

    public void addItem(Item item) {
        items.add(item);
        /*сообщаем о добавлении нового элемента списка*/
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
