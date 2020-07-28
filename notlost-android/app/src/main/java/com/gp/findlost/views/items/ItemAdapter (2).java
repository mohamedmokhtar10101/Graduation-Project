package com.gp.findlost.views.items;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.findlost.R;
import com.gp.findlost.callback.OnImageClickListner;
import com.gp.findlost.callback.OnRequestClickListner;
import com.gp.findlost.callback.OnShowRequestClickListner;
import com.gp.findlost.data.model.Item;
import com.gp.findlost.databinding.ItemItemsBinding;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
    private List<Item> items;
    private OnShowRequestClickListner listner;
    private OnRequestClickListner requestListner;
    private OnImageClickListner imageClickListner;

    public ItemAdapter(List<Item> items, OnShowRequestClickListner listner, OnRequestClickListner requestListner, OnImageClickListner imageClickListner) {
        this.items = items;
        this.listner = listner;
        this.requestListner = requestListner;
        this.imageClickListner = imageClickListner;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemItemsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_items, parent, false);
        binding.setListner(listner);
        binding.setRequestListner(requestListner);
        return new ItemHolder(binding, imageClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
