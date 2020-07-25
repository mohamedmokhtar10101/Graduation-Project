package com.gp.findlost.views.myrequests;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.collection.ArraySet;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gp.findlost.R;
import com.gp.findlost.callback.OnCallClickListner;
import com.gp.findlost.data.model.MyRequest;
import com.gp.findlost.data.model.Request;
import com.gp.findlost.data.network.api.ApiClient;
import com.gp.findlost.data.network.api.ApiService;
import com.gp.findlost.databinding.FragmentMyRequestsBinding;
import com.gp.findlost.util.LoadingDialog;
import com.gp.findlost.views.basefragment.BaseFragment;
import com.gp.findlost.views.children.ChildrenFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyRequestsFragment extends BaseFragment implements OnCallClickListner {

    private FragmentMyRequestsBinding binding;
    private ApiService api;
    private LoadingDialog dialog;
    private List<String> type = new ArrayList<>();
    private int selectedPosition = 0;

    public MyRequestsFragment() {
        super(R.layout.fragment_my_requests, true, false, true);
    }

    @Override
    protected void doOnCreate(View view) {
        binding = DataBindingUtil.bind(view);
        toolbarName.setValue(getString(R.string.my_requests));
        api = ApiClient.getInstance();
        dialog = new LoadingDialog(getContext());

        initType();
        getMyRequests();
    }


    private void initType() {
        type.add("Children");
        type.add("Items");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, type) {
            @Override
            public boolean isEnabled(int position) {
                return true;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
                if (position == selectedPosition) {
                    tv.setTextColor(MyRequestsFragment.this.getContext().getResources().getColor(R.color.colorWhite));
                    tv.setBackgroundColor(MyRequestsFragment.this.getContext().getResources().getColor(R.color.colorPrimary));
                } else {
                    tv.setTextColor(MyRequestsFragment.this.getContext().getResources().getColor(R.color.colorPrimary));
                    tv.setBackgroundColor(Color.WHITE);

                }
                return view;
            }
        };
        binding.spinnerType.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.spinnerType.setBackground(getResources().getDrawable(R.drawable.custom_spinner_primary));
                TextView tv = (TextView) parent.getChildAt(0);
                tv.setTextColor(getResources().getColor(R.color.colorWhite));
                tv.setPadding(8, 0, 8, 0);
                selectedPosition = position;
                getMyRequests();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                binding.spinnerType.setBackground(getResources().getDrawable(R.drawable.custom_spinner_white));
                TextView tv = (TextView) parent.getChildAt(0);
                tv.setTextColor(getResources().getColor(R.color.colorTitle));
                tv.setPadding(8, 0, 8, 0);
            }

        });
    }

    private void getMyRequests() {
        dialog.show();
        String requestType;
        if (type.get(selectedPosition).equals("Children")) {
            requestType = "child";
        } else {
            requestType = "item";
        }
        api.getMyRequests(ApiClient.getHeaders(), requestType).enqueue(new Callback<List<MyRequest>>() {
            @Override
            public void onResponse(Call<List<MyRequest>> call, Response<List<MyRequest>> response) {
                if (response.isSuccessful()) {
                    binding.setNoResult(response.body().isEmpty());
                    MyRequestAdapter adapter = new MyRequestAdapter(response.body(), MyRequestsFragment.this);
                    binding.myRequestssRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.myRequestssRecyclerView.setAdapter(adapter);
                }
                dialog.hide();
            }

            @Override
            public void onFailure(Call<List<MyRequest>> call, Throwable t) {
                dialog.hide();
            }
        });
    }

    @Override
    public void onPhoneClicked(String phone) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null)));
    }
}