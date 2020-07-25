package com.gp.findlost.views.makerequest;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gp.findlost.R;
import com.gp.findlost.data.model.ItemType;
import com.gp.findlost.data.model.RequestChild;
import com.gp.findlost.data.model.RequestItem;
import com.gp.findlost.data.model.RequestType;
import com.gp.findlost.data.network.api.ApiClient;
import com.gp.findlost.data.network.api.ApiService;
import com.gp.findlost.databinding.FragmentMakeRequestBinding;
import com.gp.findlost.util.ErrorDialog;
import com.gp.findlost.util.LoadingDialog;
import com.gp.findlost.util.ResponseError;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MakeRequestFragment extends BottomSheetDialogFragment {

    private String imageUrl = "";
    private ItemType itemType;
    private BottomSheetDialog bottomSheet;

    public MakeRequestFragment() {

    }

    private FragmentMakeRequestBinding binding;
    private ProgressDialog progressDialog;


    private FirebaseStorage storage;
    private StorageReference reference;

    private BottomSheetBehavior bottomSheetBehavior;


    private String id;
    private String founderId;
    private RequestType requestType;

    private FirebaseVisionFaceDetectorOptions highAccuracyOpts;
    private FirebaseVisionImage image;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        //inflating layout
        View view = View.inflate(getContext(), R.layout.fragment_make_request, null);
        //binding views to data binding.
        binding = DataBindingUtil.bind(view);
        //setting layout with bottom sheet
        bottomSheet.setContentView(view);

        storage = FirebaseStorage.getInstance();
        reference = storage.getReference().child("Images");
        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));

        id = MakeRequestFragmentArgs.fromBundle(getArguments()).getId();
        founderId = MakeRequestFragmentArgs.fromBundle(getArguments()).getFounderId();
        requestType = MakeRequestFragmentArgs.fromBundle(getArguments()).getType();
        itemType = MakeRequestFragmentArgs.fromBundle(getArguments()).getItemType();

        // High-accuracy landmark detection and face classification
        highAccuracyOpts =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.NO_LANDMARKS)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.NO_CLASSIFICATIONS)
                        .build();

        if (requestType == RequestType.ITEM) {
            binding.imageCardView.setVisibility(View.GONE);
        } else {
            binding.imageCardView.setVisibility(View.VISIBLE);
        }

        binding.image.setOnClickListener(v -> pickImage());
        binding.request.setOnClickListener(v -> {
            if (validated()) {
                makeRequest();
            }
        });

        return bottomSheet;
    }

    private void makeRequest() {
        ApiService api = ApiClient.getInstance();
        if (requestType == RequestType.CHILD) {
            RequestChild requestChild;
            if (!imageUrl.isEmpty()) {
                requestChild = new RequestChild(imageUrl, binding.description.getText().toString(), id, founderId);
            } else {
                requestChild = new RequestChild(binding.description.getText().toString(), id, founderId);
            }
            LoadingDialog dialog = new LoadingDialog(getContext());
            dialog.show();
            api.makeRequestChild(ApiClient.getHeaders(), requestChild).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.hide();
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), ResponseError.getErrorMessage(Objects.requireNonNull(response.errorBody())), Toast.LENGTH_SHORT).show();
                    }
                    bottomSheet.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.hide();
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            RequestItem requestItem = new RequestItem(binding.description.getText().toString(), id, founderId);
            LoadingDialog dialog = new LoadingDialog(getContext());
            dialog.show();
            api.makeRequestItem(ApiClient.getHeaders(), requestItem).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.hide();
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), ResponseError.getErrorMessage(Objects.requireNonNull(response.errorBody())), Toast.LENGTH_SHORT).show();
                    }
                    bottomSheet.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.hide();
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validated() {
        if (binding.description.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Enter description", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (requestType == RequestType.CHILD && itemType == ItemType.LOST) {
            if (imageUrl.isEmpty()) {
                Toast.makeText(getContext(), "Select Image", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }


    private void pickImage() {
        ImagePicker.Companion.with(this)
                .crop()
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri filerUri = data.getData();
            if (requestType == RequestType.CHILD) {
                try {
                    image = FirebaseVisionImage.fromFilePath(getContext(), filerUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                detect(filerUri);
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getContext(), ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void detect(Uri filerUri) {
        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(highAccuracyOpts);

        detector.detectInImage(image)
                .addOnSuccessListener(
                        faces -> {
                            if (!faces.isEmpty()) {
                                uploadImage(filerUri);
                            } else {
                                Toast.makeText(getActivity(), "Please select person image", Toast.LENGTH_SHORT).show();
                            }
                        })
                .addOnFailureListener(
                        e -> {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
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
                            imageUrl = task.getResult().toString();
                            if (task.isSuccessful()) {
                                Picasso.get()
                                        .load(task.getResult().toString())
                                        .into(binding.image);
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

}