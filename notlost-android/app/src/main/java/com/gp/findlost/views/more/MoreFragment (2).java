package com.gp.findlost.views.more;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gp.findlost.R;
import com.gp.findlost.data.network.api.ApiClient;
import com.gp.findlost.data.network.api.ApiService;
import com.gp.findlost.databinding.FragmentMoreBinding;
import com.gp.findlost.util.ErrorDialog;
import com.gp.findlost.util.LoadingDialog;
import com.gp.findlost.util.PrefManager;
import com.gp.findlost.views.basefragment.BaseFragment;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoreFragment extends BaseFragment {
    private FragmentMoreBinding binding;
    private ProgressDialog progressDialog;

    private FirebaseStorage storage;
    private StorageReference reference;
    private ApiService api;

    public MoreFragment() {
        super(R.layout.fragment_more, true, false, true);
    }

    @Override
    protected void doOnCreate(View view) {
        binding = DataBindingUtil.bind(view);
        toolbarName.setValue(getString(R.string.more));
        binding.setIsLogin(PrefManager.isLogin());
        if (PrefManager.isLogin()) {
            binding.setUser(PrefManager.getUser());
            if (PrefManager.getUser().getEmail() == null){
                binding.userEmail.setVisibility(View.GONE);
            } else {
                binding.userEmail.setVisibility(View.VISIBLE);
            }
        } else {
            binding.userEmail.setVisibility(View.GONE);
        }


        storage = FirebaseStorage.getInstance();
        reference = storage.getReference().child("Images");

        initViews();
    }

    private void initViews() {
        binding.logOut.setOnClickListener(v -> {
            PrefManager.logOut();
            Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(R.id.splashFragment);
        });

        binding.moreSignInButton.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(R.id.signInFragment);
        });

        binding.moreSignUpButton.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(R.id.signUpFragment);
        });

        binding.editProfile.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(R.id.editProfileFragment);
        });


        binding.addImage.setOnClickListener(v -> {
            pickImage();
        });
    }


    private void pickImage() {
        ImagePicker.Companion.with(this)
                .galleryOnly()
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri filerUri = data.getData();
            uploadImage(filerUri);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getContext(), ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage(Uri filerUri) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        final String imageName = UUID.randomUUID().toString() + ".jpg";
        reference.child(imageName).putFile(filerUri).addOnSuccessListener(
                taskSnapshot -> reference.child(imageName)
                        .getDownloadUrl().addOnCompleteListener(task -> {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Picasso.get()
                                        .load(Objects.requireNonNull(task.getResult()).toString())
                                        .into(binding.userImage);
                                saveImage(task.getResult().toString());
                            } else {
                                ErrorDialog errorDialog = new ErrorDialog(getContext());
                                errorDialog.show();
                                errorDialog.setErrorMessage(task.getException().getMessage());
                            }
                        })).addOnFailureListener(e -> {
            progressDialog.dismiss();
            ErrorDialog errorDialog = new ErrorDialog(getContext());
            errorDialog.show();
            errorDialog.setErrorMessage(e.getMessage());
        });
    }

    private void saveImage(String imageUrl) {
        HashMap<String, String> body = new HashMap<>();
        body.put("image", imageUrl);
        api = ApiClient.getInstance();
        LoadingDialog dialog = new LoadingDialog(getContext());
        dialog.show();
        api.updateImage(ApiClient.getHeaders(), body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.hide();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.hide();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                binding.userImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.img_placeholder));
            }
        });
    }

}