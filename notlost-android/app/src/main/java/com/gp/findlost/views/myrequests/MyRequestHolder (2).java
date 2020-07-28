package com.gp.findlost.views.myrequests;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.findlost.data.model.MyRequest;
import com.gp.findlost.data.model.Request;
import com.gp.findlost.databinding.ItemMyRequestBinding;
import com.gp.findlost.databinding.ItemRequestBinding;

public class MyRequestHolder extends RecyclerView.ViewHolder {

    private ItemMyRequestBinding binding;

    public MyRequestHolder(@NonNull ItemMyRequestBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(MyRequest request) {
        binding.setRequest(request);
    }
}
