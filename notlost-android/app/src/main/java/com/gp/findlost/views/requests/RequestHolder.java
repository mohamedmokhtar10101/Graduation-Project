package com.gp.findlost.views.requests;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.findlost.data.model.Request;
import com.gp.findlost.databinding.ItemRequestBinding;

public class RequestHolder extends RecyclerView.ViewHolder {

    private ItemRequestBinding binding;

    public RequestHolder(@NonNull ItemRequestBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Request request, int position) {
        binding.setRequest(request);
        binding.setPosition(position);
    }
}
