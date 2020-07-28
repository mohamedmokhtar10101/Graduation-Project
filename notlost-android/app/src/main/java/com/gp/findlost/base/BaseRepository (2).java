package com.gp.findlost.base;

import androidx.lifecycle.MutableLiveData;

import com.gp.findlost.data.network.api.ApiClient;
import com.gp.findlost.data.network.api.ApiService;

import java.util.HashMap;

public class BaseRepository {
    protected ApiService apiService = ApiClient.getInstance();
    protected MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    protected MutableLiveData<String> errorMessage = new MutableLiveData<>();
    protected HashMap<String, String> headers = ApiClient.getHeaders();

}