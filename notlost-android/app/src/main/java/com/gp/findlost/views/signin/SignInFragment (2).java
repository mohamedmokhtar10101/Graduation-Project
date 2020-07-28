package com.gp.findlost.views.signin;

import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import com.google.firebase.iid.FirebaseInstanceId;
import com.gp.findlost.R;
import com.gp.findlost.data.model.SignIn;
import com.gp.findlost.data.model.Token;
import com.gp.findlost.data.model.User;
import com.gp.findlost.data.network.api.ApiClient;
import com.gp.findlost.data.network.api.ApiService;
import com.gp.findlost.data.network.error.NoConnectivityException;
import com.gp.findlost.databinding.FragmentSignInBinding;
import com.gp.findlost.util.Constants;
import com.gp.findlost.util.InputValidator;
import com.gp.findlost.util.LoadingDialog;
import com.gp.findlost.util.PrefManager;
import com.gp.findlost.util.ResponseError;
import com.gp.findlost.views.basefragment.BaseFragment;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class SignInFragment extends BaseFragment {
    private FragmentSignInBinding binding;
    private String username;
    private String password;
    private ApiService api;
    private LoadingDialog loadingDialog;
    private SignIn user;

    public SignInFragment() {
        super(R.layout.fragment_sign_in, true, true, false);
    }


    @Override
    protected void doOnCreate(View view) {
        binding = DataBindingUtil.bind(view);
        toolbarName.setValue(getString(R.string.signin));
        binding.forgetPasswordTextView.setPaintFlags(binding.forgetPasswordTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        api = ApiClient.getInstance();

        binding.signInLogInButton.setOnClickListener(v -> {
            if (getInputData()) {
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                user = new SignIn(username, password, "token");
                                login();
                                return;
                            }
                            String token = task.getResult().getToken();
                            user = new SignIn(username, password, token);
                            login();

                        });

            }
        });
    }

    private boolean getInputData() {
        if (!InputValidator.loginValidation(getActivity(), binding.signInUsernameEditText, binding.signInPasswordEditText))
            return false;

        username = binding.signInUsernameEditText.getText().toString().trim();
        password = binding.signInPasswordEditText.getText().toString().trim();

        return true;
    }

    private void login() {
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.show();

        api.signIn(ApiClient.getHeaders(), user).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    PrefManager.saveToken(response.body().getAccessToken());
                    getUser();
                } else {
                    loadingDialog.hide();
                    Toast.makeText(getContext(), ResponseError.getErrorMessage(Objects.requireNonNull(response.errorBody())), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                loadingDialog.hide();
                if (t instanceof NoConnectivityException) {
                    Toast.makeText(getContext(), Constants.NO_INTERNET_MESSAGE, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void getUser() {
        api.getUser(ApiClient.getHeaders()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                loadingDialog.hide();
                if (response.isSuccessful()) {
                    PrefManager.saveUser(response.body());
                    Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(R.id.splashFragment);
                } else {
                    Toast.makeText(getContext(), ResponseError.getErrorMessage(Objects.requireNonNull(response.errorBody())), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                loadingDialog.hide();
                if (t instanceof NoConnectivityException) {
                    Toast.makeText(getContext(), Constants.NO_INTERNET_MESSAGE, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}