package com.gp.findlost.views.editprofile;

import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import com.gp.findlost.R;
import com.gp.findlost.data.model.EditProfile;
import com.gp.findlost.data.model.User;
import com.gp.findlost.data.network.api.ApiClient;
import com.gp.findlost.data.network.api.ApiService;
import com.gp.findlost.databinding.FragmentEditProfileBinding;
import com.gp.findlost.util.InputValidator;
import com.gp.findlost.util.LoadingDialog;
import com.gp.findlost.util.PrefManager;
import com.gp.findlost.views.basefragment.BaseFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends BaseFragment {

    private FragmentEditProfileBinding binding;
    private EditProfile editProfile;


    public EditProfileFragment() {
        super(R.layout.fragment_edit_profile, true, true, true);
    }

    @Override
    protected void doOnCreate(View view) {
        binding = DataBindingUtil.bind(view);
        toolbarName.setValue(getString(R.string.edit_profile));

        binding.save.setOnClickListener(v -> {
            if (getInputData()) {
                editProfile();
            }
        });
    }

    private boolean getInputData() {
        if (!InputValidator.editProfileValidator(getActivity(), binding.firstNameET, binding.lastNameET, binding.phoneET))
            return false;

        editProfile = new EditProfile(binding.firstNameET.getText().toString(), binding.lastNameET.getText().toString(), binding.phoneET.getText().toString());
        return true;
    }

    private void editProfile() {
        ApiService api = ApiClient.getInstance();

        LoadingDialog dialog = new LoadingDialog(getContext());
        dialog.show();
        api.editProfile(ApiClient.getHeaders(), editProfile).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                dialog.hide();
                if (response.isSuccessful()) {
                    PrefManager.saveUser(response.body());
                    Navigation.findNavController(getActivity(), R.id.navHostFragment).popBackStack();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                dialog.hide();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}