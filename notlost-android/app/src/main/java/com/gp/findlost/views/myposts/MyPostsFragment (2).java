package com.gp.findlost.views.myposts;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gp.findlost.R;
import com.gp.findlost.callback.OnImageClickListner;
import com.gp.findlost.callback.OnShowRequestClickListner;
import com.gp.findlost.data.model.Item;
import com.gp.findlost.data.model.RequestType;
import com.gp.findlost.data.network.api.ApiClient;
import com.gp.findlost.data.network.api.ApiService;
import com.gp.findlost.data.network.error.NoConnectivityException;
import com.gp.findlost.databinding.FragmentMyPostsBinding;
import com.gp.findlost.util.Constants;
import com.gp.findlost.util.LoadingDialog;
import com.gp.findlost.util.PrefManager;
import com.gp.findlost.util.ResponseError;
import com.gp.findlost.views.basefragment.BaseFragment;
import com.gp.findlost.views.items.ItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyPostsFragment extends BaseFragment implements OnShowRequestClickListner, OnImageClickListner {
    private FragmentMyPostsBinding binding;
    private List<String> type = new ArrayList<>();
    private List<String> searchType = new ArrayList<>();
    private List<String> searchTypeSpinner = new ArrayList<>();
    private int typeSelected = -1;
    private int typeSearchSelected = -1;
    private boolean show = true;
    private ApiService api;

    public MyPostsFragment() {
        super(R.layout.fragment_my_posts, true, false, true);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void doOnCreate(View view) {
        binding = DataBindingUtil.bind(view);
        toolbarName.setValue(getString(R.string.my_posts));
        binding.add.setOnClickListener(v -> Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(R.id.myPostsToAdd));
        if (PrefManager.isLogin()) {
            initType();
            initSearchType();
            binding.add.setVisibility(View.VISIBLE);
        } else {
            binding.layout.setVisibility(View.GONE);
        }
    }

    private void initSearchType() {
        searchType.add("Lost");
        searchType.add("Found");
        searchTypeSpinner.add("Type");
        searchTypeSpinner.add("Lost");
        searchTypeSpinner.add("Founded");
        initSpinner(binding.spinnerSearchType, searchTypeSpinner);
    }

    private void initType() {
        type.add("Children");
        type.add("Items");
        initSpinner(binding.spinnerType, type);
    }

    private void initSpinner(Spinner spinner, List<String> spinnerData) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, spinnerData) {
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
                int selectedItem = -1;
                switch (parent.getId()) {
                    case R.id.spinnerType:
                        selectedItem = typeSelected;
                        break;
                    case R.id.spinnerSearchType:
                        selectedItem = typeSearchSelected;
                        break;
                }
                if (position == 0 && parent.getId() == R.id.spinnerSearchType) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    if (position == selectedItem) {
                        tv.setTextColor(MyPostsFragment.this.getContext().getResources().getColor(R.color.colorWhite));
                        tv.setBackgroundColor(MyPostsFragment.this.getContext().getResources().getColor(R.color.colorPrimary));
                    } else {
                        tv.setTextColor(MyPostsFragment.this.getContext().getResources().getColor(R.color.colorPrimary));
                        tv.setBackgroundColor(Color.WHITE);
                    }
                }
                return view;
            }
        };
        spinner.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv;
                switch (spinner.getId()) {
                    case R.id.spinnerType:
                        spinner.setBackground(getResources().getDrawable(R.drawable.custom_spinner_primary));
                        tv = (TextView) parent.getChildAt(0);
                        if (tv != null) {
                            tv.setTextColor(getResources().getColor(R.color.colorWhite));
                            tv.setPadding(8, 0, 8, 0);
                        }
                        typeSelected = position;
                        if (show) {
                            binding.spinnerSearchType.setSelection(1);
                            show = false;
                        } else {
                            binding.spinnerSearchType.setSelection(0);
                        }
                        break;
                    case R.id.spinnerSearchType:
                        typeSearchSelected = position - 1;
                        if (position != 0) {
                            spinner.setBackground(getResources().getDrawable(R.drawable.custom_spinner_primary));
                            tv = (TextView) parent.getChildAt(0);
                            if (tv != null) {
                                tv.setTextColor(getResources().getColor(R.color.colorWhite));
                                tv.setPadding(8, 0, 8, 0);
                            }
                            getPosts();
                        } else {
                            spinner.setBackground(getResources().getDrawable(R.drawable.custom_spinner_white));
                            tv = (TextView) parent.getChildAt(0);
                            if (tv != null) {
                                tv.setTextColor(getResources().getColor(R.color.colorTitle));
                                tv.setPadding(8, 0, 8, 0);
                            }
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setBackground(getResources().getDrawable(R.drawable.custom_spinner_white));
                TextView tv = (TextView) parent.getChildAt(0);
                tv.setTextColor(getResources().getColor(R.color.colorTitle));
                tv.setPadding(8, 0, 8, 0);
            }

        });

    }

    private void getPosts() {
        LoadingDialog dialog = new LoadingDialog(getContext());
        dialog.show();

        api = ApiClient.getInstance();
        api.getMyPosts(ApiClient.getHeaders(), type.get(typeSelected), searchType.get(typeSearchSelected)).enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                dialog.hide();
                if (response.isSuccessful()) {
                    binding.setNoResult(response.body().isEmpty());
                    ItemAdapter adapter = new ItemAdapter(response.body(), MyPostsFragment.this, null, MyPostsFragment.this);
                    binding.myPostsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.myPostsRecyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), ResponseError.getErrorMessage(Objects.requireNonNull(response.errorBody())), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                dialog.hide();
                if (t instanceof NoConnectivityException) {
                    Toast.makeText(getContext(), Constants.NO_INTERNET_MESSAGE, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onShowRequestClicked(String name, String id) {
        RequestType requestType;
        if (typeSelected == 0) {
            requestType = RequestType.CHILD;
        } else {
            requestType = RequestType.ITEM;
        }
        MyPostsFragmentDirections.ShowRequests action = MyPostsFragmentDirections.showRequests(name, requestType, id);
        Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(action);
    }

    @Override
    public void onImageClicked(String imageUrl) {
    }
}