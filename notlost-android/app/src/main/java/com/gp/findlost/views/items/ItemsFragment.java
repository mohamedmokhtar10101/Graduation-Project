package com.gp.findlost.views.items;

import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gp.findlost.R;
import com.gp.findlost.callback.OnImageClickListner;
import com.gp.findlost.callback.OnRequestClickListner;
import com.gp.findlost.callback.OnShowRequestClickListner;
import com.gp.findlost.data.model.Item;
import com.gp.findlost.data.model.ItemType;
import com.gp.findlost.data.model.RequestType;
import com.gp.findlost.data.network.api.ApiClient;
import com.gp.findlost.data.network.api.ApiService;
import com.gp.findlost.data.network.error.NoConnectivityException;
import com.gp.findlost.databinding.FragmentItemsBinding;
import com.gp.findlost.util.Constants;
import com.gp.findlost.util.LoadingDialog;
import com.gp.findlost.util.PrefManager;
import com.gp.findlost.util.ResponseError;
import com.gp.findlost.views.basefragment.BaseFragment;
import com.gp.findlost.views.children.ChildrenFragment;
import com.gp.findlost.views.children.ChildrenFragmentDirections;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemsFragment extends BaseFragment implements OnRequestClickListner, OnImageClickListner {
    private FragmentItemsBinding binding;
    private List<String> spinnerData = new ArrayList<>();
    private List<String> types = new ArrayList<>();
    private int selectedPosition = -1;
    private ApiService api;
    private ItemAdapter adapter;
    private List<Item> items;
    private List<Item> filteredItems = new ArrayList<>();

    public ItemsFragment() {
        super(R.layout.fragment_items, true, false, true);
    }

    @Override
    protected void doOnCreate(View view) {
        binding = DataBindingUtil.bind(view);
        toolbarName.setValue(getString(R.string.items));
        api = ApiClient.getInstance();

        initView();
        getItems();
    }

    private void initView() {
        spinnerData.clear();
        types.clear();
        spinnerData.add("Founded");
        spinnerData.add("Lost");
        types.add("Found");
        types.add("Lost");
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
                if (position == selectedPosition) {
                    tv.setTextColor(ItemsFragment.this.getContext().getResources().getColor(R.color.colorWhite));
                    tv.setBackgroundColor(ItemsFragment.this.getContext().getResources().getColor(R.color.colorPrimary));
                } else {
                    tv.setTextColor(ItemsFragment.this.getContext().getResources().getColor(R.color.colorPrimary));
                    tv.setBackgroundColor(Color.WHITE);

                }
                return view;
            }
        };
        binding.spinner.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.spinner.setBackground(getResources().getDrawable(R.drawable.custom_spinner_primary));
                TextView tv = (TextView) parent.getChildAt(0);
                if (tv != null) {
                    tv.setTextColor(getResources().getColor(R.color.colorWhite));
                    tv.setPadding(8, 0, 8, 0);
                }
                selectedPosition = position;
                getItems();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                binding.spinner.setBackground(getResources().getDrawable(R.drawable.custom_spinner_white));
                TextView tv = (TextView) parent.getChildAt(0);
                if (tv != null) {
                    tv.setTextColor(getResources().getColor(R.color.colorWhite));
                    tv.setPadding(8, 0, 8, 0);
                }
            }

        });

        binding.removeSearch.setOnClickListener(v -> {
            adapter.setItems(items);
            binding.searchET.setText("");
            binding.setNoResult(false);
            //Hide Keyboard
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        });
        watchSearchET();
    }

    private void watchSearchET() {
        binding.searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.setNoResult(false);
                if (s.toString().isEmpty()) {
                    adapter.setItems(items);
                    binding.setIsWrite(false);
                    binding.setNoResult(false);
                } else {
                    binding.setIsWrite(true);
                    filteredItems = new ArrayList<>();
                    for (Item item : items) {
                        if (item.getName().toLowerCase().contains(s.toString().toLowerCase())
                                || item.getCity().getName().toLowerCase().contains(s.toString().toLowerCase())) {
                            filteredItems.add(item);
                        }
                    }
                    if (filteredItems.isEmpty()) {
                        binding.setNoResult(true);
                    } else {
                        binding.setNoResult(false);
                    }
                    adapter.setItems(filteredItems);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getItems() {
        if (selectedPosition != -1) {
            LoadingDialog dialog = new LoadingDialog(getContext());
            dialog.show();
            api.getItems(ApiClient.getHeaders(), types.get(selectedPosition), false).enqueue(new Callback<List<Item>>() {
                @Override
                public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                    dialog.hide();
                    if (response.isSuccessful()) {
                        items = new ArrayList<>();
                        items.addAll(response.body());
                        adapter = new ItemAdapter(response.body(), null, ItemsFragment.this, ItemsFragment.this);
                        binding.itemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.itemsRecyclerView.setAdapter(adapter);
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
    }

    @Override
    public void onRequestClicked(Item item) {
        if (PrefManager.isLogin()){
            ItemType itemType;
            if (selectedPosition == 0) {
                itemType = ItemType.FOUNDED;
            } else {
                itemType = ItemType.LOST;
            }
            ItemsFragmentDirections.ItemMakeRequest action = ItemsFragmentDirections.itemMakeRequest(item.getId(), item.getUserId(), RequestType.ITEM, itemType);
            Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(action);
        }
        else{
            Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(R.id.signInFragment);
        }
    }

    @Override
    public void onImageClicked(String imageUrl) {
        ItemsFragmentDirections.ItemToImage itemToImage = ItemsFragmentDirections.itemToImage(imageUrl);
        Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(itemToImage);
    }
}