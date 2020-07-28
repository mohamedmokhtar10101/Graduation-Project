package com.gp.findlost.views.items;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.findlost.callback.OnImageClickListner;
import com.gp.findlost.data.model.Item;
import com.gp.findlost.databinding.ItemItemsBinding;

public class ItemHolder extends RecyclerView.ViewHolder {
    private ItemItemsBinding binding;
    private OnImageClickListner listner;

    public ItemHolder(@NonNull ItemItemsBinding binding, OnImageClickListner listner) {
        super(binding.getRoot());
        this.binding = binding;
        this.listner = listner;

    }

    public void bind(Item item) {
        binding.setItem(item);
        SliderAdapter adapter = new SliderAdapter(item.getImages(), listner);
        binding.itemSlider.setAdapter(adapter);
        binding.itemIndicator.setupWithViewPager(binding.itemSlider, true);

    }
}
