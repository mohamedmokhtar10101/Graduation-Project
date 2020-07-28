package com.gp.findlost.views.signup;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import com.google.firebase.iid.FirebaseInstanceId;
import com.gp.findlost.R;
import com.gp.findlost.data.model.SignUp;
import com.gp.findlost.data.model.Token;
import com.gp.findlost.data.model.User;
import com.gp.findlost.data.network.api.ApiClient;
import com.gp.findlost.data.network.api.ApiService;
import com.gp.findlost.data.network.error.NoConnectivityException;
import com.gp.findlost.databinding.FragmentSignUpBinding;
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


public class SignUpFragment extends BaseFragment {
    private FragmentSignUpBinding binding;
    private ApiService api;
    private SignUp signUp;
    private LoadingDialog loadingDialog;


    public SignUpFragment() {
        super(R.layout.fragment_sign_up, true, true, true);
    }

    @Override
    protected void doOnCreate(View view) {
        binding = DataBindingUtil.bind(view);
        toolbarName.setValue(getString(R.string.sign_up));

        api = ApiClient.getInstance();

        binding.signUpButton.setOnClickListener(v -> {
            if (getInputData()) {
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                signUp = new SignUp(binding.signUpFirstNameEditText.getText().toString(), binding.signUpLastNameEditText.getText().toString(),
                                        binding.signUpEmailEditText.getText().toString(), binding.signUpPasswordEditText.getText().toString(), binding.signUpPhoneEditText.getText().toString(), "token");
                                signUp();
                                return;
                            }
                            String token = task.getResult().getToken();
                            String email = binding.signUpEmailEditText.getText().toString();
                            if (email.isEmpty()) {
                                signUp = new SignUp(binding.signUpFirstNameEditText.getText().toString(), binding.signUpLastNameEditText.getText().toString(),
                                        binding.signUpPasswordEditText.getText().toString(), binding.signUpPhoneEditText.getText().toString(), token);
                            } else {
                                signUp = new SignUp(binding.signUpFirstNameEditText.getText().toString(), binding.signUpLastNameEditText.getText().toString(),
                                        binding.signUpEmailEditText.getText().toString(), binding.signUpPasswordEditText.getText().toString(), binding.signUpPhoneEditText.getText().toString(), token);
                            }
                            signUp();
                        });
            }
        });

    }

    private void signUp() {
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.show();

        api.signUp(ApiClient.getHeaders(), signUp).enqueue(new Callback<Token>() {
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

    private boolean getInputData() {
        if (!InputValidator.registerValidation(getActivity(), binding.signUpFirstNameEditText, binding.signUpLastNameEditText,
                binding.signUpPasswordEditText, binding.signUpPhoneEditText)) {
            return false;
        }

        String email = binding.signUpEmailEditText.getText().toString().trim();
        if (!email.isEmpty()) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.signUpEmailEditText.setError("Invalid Email");
                return false;
            }
        }
        return true;
    }
}