package com.gp.findlost.views.requests;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.common.api.Api;
import com.gp.findlost.R;
import com.gp.findlost.callback.OnAcceptClickListner;
import com.gp.findlost.callback.OnBlockClickListner;
import com.gp.findlost.callback.OnCallClickListner;
import com.gp.findlost.callback.OnRejectClickListner;
import com.gp.findlost.callback.OnRemoveClickListner;
import com.gp.findlost.callback.OnRetryClickListner;
import com.gp.findlost.data.model.Request;
import com.gp.findlost.data.model.RequestType;
import com.gp.findlost.data.network.api.ApiClient;
import com.gp.findlost.data.network.api.ApiService;
import com.gp.findlost.databinding.FragmentRequestsBinding;
import com.gp.findlost.util.ErrorDialog;
import com.gp.findlost.util.LoadingDialog;
import com.gp.findlost.views.basefragment.BaseFragment;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestsFragment extends BaseFragment implements OnAcceptClickListner, OnRejectClickListner, OnCallClickListner, OnRemoveClickListner, OnRetryClickListner, OnBlockClickListner {

    private ApiService api;
    private LoadingDialog dialog;
    private ErrorDialog errorDialog;
    private Request removeRequest;

    public RequestsFragment() {
        super(R.layout.fragment_requests, true, true, false);
    }

    private FragmentRequestsBinding binding;

    private String name;
    private String requestId;
    private RequestType type;

    @Override
    protected void doOnCreate(View view) {
        binding = DataBindingUtil.bind(view);

        name = RequestsFragmentArgs.fromBundle(getArguments()).getName();
        requestId = RequestsFragmentArgs.fromBundle(getArguments()).getId();
        type = RequestsFragmentArgs.fromBundle(getArguments()).getType();
        toolbarName.setValue(name + " Requests");

        getItemRequest();
    }

    private void getItemRequest() {
        api = ApiClient.getInstance();
        dialog = new LoadingDialog(getContext());
        dialog.show();
        api.getChildRequests(ApiClient.getHeaders(), type.toString().toLowerCase(), requestId).enqueue(new Callback<List<Request>>() {
            @Override
            public void onResponse(Call<List<Request>> call, Response<List<Request>> response) {
                binding.setNoResult(response.body().isEmpty());
                RequestAdapter adapter = new RequestAdapter(response.body(), RequestsFragment.this, RequestsFragment.this, RequestsFragment.this, RequestsFragment.this);
                binding.requestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.requestRecyclerView.setAdapter(adapter);
                dialog.hide();
            }

            @Override
            public void onFailure(Call<List<Request>> call, Throwable t) {
                dialog.hide();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAcceptClicked(Request request, int position) {
        changeRequestState(request, "accept");
    }

    @Override
    public void onRejectClicked(Request request, int position) {
        changeRequestState(request, "reject");
    }

    private void changeRequestState(Request request, String action) {
        dialog.show();
        api.changeRequestState(ApiClient.getHeaders(), type.toString().toLowerCase(), request.getId(), action).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.hide();
                if (response.isSuccessful()) {
                    getItemRequest();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.hide();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onPhoneClicked(String phone) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null)));
    }

    @Override
    public void onRemoveCLickListner(Request request) {
        removeRequest = request;
        errorDialog = new ErrorDialog(getContext(), "Did you want to remove this request or you want to block the request sender.", this, this);
        errorDialog.show();
        errorDialog.errorImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_close));
        errorDialog.errorTV.setVisibility(View.GONE);
    }

    @Override
    public void onRetryClick() {
        errorDialog.hide();
        changeRequestState(removeRequest, "delete");
    }

    @Override
    public void onBlockClicked() {
        errorDialog.hide();
        changeRequestState(removeRequest, "block");
    }
}