package com.gp.findlost.views.myrequests;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.findlost.R;
import com.gp.findlost.callback.OnCallClickListner;
import com.gp.findlost.data.model.MyRequest;
import com.gp.findlost.data.model.Request;
import com.gp.findlost.databinding.ItemMyRequestBinding;

import java.util.List;

public class MyRequestAdapter extends RecyclerView.Adapter<MyRequestHolder> {

    private List<MyRequest> requests;
    private OnCallClickListner callClickListner;

    public MyRequestAdapter(List<MyRequest> requests, OnCallClickListner callClickListner) {
        this.requests = requests;
        this.callClickListner = callClickListner;
    }

    @NonNull
    @Override
    public MyRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyRequestBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_my_request, parent, false);
        binding.setPhoneListner(callClickListner);
        return new MyRequestHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRequestHolder holder, int position) {
        holder.bind(requests.get(position));
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }
}
