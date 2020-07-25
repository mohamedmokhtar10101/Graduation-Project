package com.gp.findlost.views.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.iid.FirebaseInstanceId;
import com.gp.findlost.R;
import com.gp.findlost.callback.OnNavigationUpdateListner;
import com.gp.findlost.data.model.CheckRequests;
import com.gp.findlost.data.network.api.ApiClient;
import com.gp.findlost.data.network.api.ApiService;
import com.gp.findlost.databinding.ActivityMainBinding;
import com.gp.findlost.util.PrefManager;
import com.gp.findlost.views.basefragment.BaseFragment;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnNavigationUpdateListner {
    private ActivityMainBinding binding;
    private String type;
    private ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        api = ApiClient.getInstance();

        BaseFragment.setListner(this);
        setupNavigation();
        registerToken();

    }

    private void registerToken() {
        if (PrefManager.getToken() != null) {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.w("FIREBASE_INSTANCE", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d("token", "onCreate: " + token);
                        registeredToken(token);
                    });
        }
    }

    private void registeredToken(String token) {
        HashMap<String, String> body = new HashMap<>();
        body.put("deviceToken", token);

        api.registerDevice(ApiClient.getHeaders(), body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void setupNavigation() {
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(binding.navigation, navController);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> type = destination.getLabel().toString());
    }

    @Override
    public void onUpdateNavigation(boolean showNavigation) {
        binding.setShowNavigation(showNavigation);
    }

    @Override
    public void onBackPressed() {
        if (type.equals(getString(R.string.children_fragment))) {
            finish();
        } else if (type.equals(getString(R.string.items_fragment)) || type.equals(getString(R.string.children_fragment))
                || type.equals(getString(R.string.my_posts_fragment)) || type.equals(getString(R.string.more))) {
            Navigation.findNavController(this, R.id.navHostFragment).navigate(R.id.actionChildren);
        } else if (type.equals(getString(R.string.add_fragment)) || type.equals("fragment_requests")) {
            Navigation.findNavController(this, R.id.navHostFragment).navigate(R.id.actionMyPosts);
        } else {
            super.onBackPressed();
        }
    }

}