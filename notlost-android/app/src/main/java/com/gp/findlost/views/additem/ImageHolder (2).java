package com.gp.findlost.views.additem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.findlost.databinding.ItemAddImageBinding;

public class ImageHolder extends RecyclerView.ViewHolder {

    private ItemAddImageBinding binding;

    public ImageHolder(@NonNull ItemAddImageBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(String image, int position) {
        binding.setImage(image);
        binding.setPosition(position);
    }
}
