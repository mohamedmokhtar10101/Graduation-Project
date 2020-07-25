package com.gp.findlost.views.additem;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.findlost.R;
import com.gp.findlost.callback.OnRemoveImageClickListner;
import com.gp.findlost.databinding.ItemAddImageBinding;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageHolder> implements OnRemoveImageClickListner {
    private List<String> images;

    public ImageAdapter(List<String> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_add_image, parent, false);
        binding.setListner(this);
        return new ImageHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        holder.bind(images.get(position), position);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void addImage(String image) {
        this.images.add(image);
        notifyItemChanged(getItemCount() - 1);
    }

    public List<String> getImages() {
        return images;
    }

    @Override
    public void onRemoveClicked(int position) {
        notifyDataSetChanged();
    }
}
