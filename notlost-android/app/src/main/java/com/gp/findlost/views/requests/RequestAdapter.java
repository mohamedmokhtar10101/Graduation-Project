package com.gp.findlost.views.requests;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.findlost.R;
import com.gp.findlost.callback.OnAcceptClickListner;
import com.gp.findlost.callback.OnCallClickListner;
import com.gp.findlost.callback.OnRejectClickListner;
import com.gp.findlost.callback.OnRemoveClickListner;
import com.gp.findlost.data.model.Request;
import com.gp.findlost.databinding.ItemRequestBinding;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestHolder> {

    private List<Request> requests;
    private OnAcceptClickListner acceptClickListner;
    private OnRejectClickListner rejectClickListner;
    private OnCallClickListner callClickListner;
    private OnRemoveClickListner removeClickListner;

    public RequestAdapter(List<Request> requests, OnAcceptClickListner acceptClickListner, OnRejectClickListner rejectClickListner, OnCallClickListner callClickListner, OnRemoveClickListner removeClickListner) {
        this.requests = requests;
        this.acceptClickListner = acceptClickListner;
        this.rejectClickListner = rejectClickListner;
        this.callClickListner = callClickListner;
        this.removeClickListner = removeClickListner;
    }

    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRequestBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_request, parent, false);
        binding.setAcceptListner(acceptClickListner);
        binding.setRejectListner(rejectClickListner);
        binding.setPhoneListner(callClickListner);
        binding.setRemoveListner(removeClickListner);
        return new RequestHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestHolder holder, int position) {
        holder.bind(requests.get(position), position);
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }
}
