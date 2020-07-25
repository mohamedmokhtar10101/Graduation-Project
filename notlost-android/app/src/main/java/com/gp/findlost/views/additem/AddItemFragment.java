package com.gp.findlost.views.additem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gp.findlost.R;
import com.gp.findlost.data.model.AddChildren;
import com.gp.findlost.data.model.AddItem;
import com.gp.findlost.data.model.City;
import com.gp.findlost.data.network.api.ApiClient;
import com.gp.findlost.data.network.api.ApiService;
import com.gp.findlost.data.network.error.NoConnectivityException;
import com.gp.findlost.databinding.FragmentAddItemBinding;
import com.gp.findlost.util.Constants;
import com.gp.findlost.util.ErrorDialog;
import com.gp.findlost.util.InputValidator;
import com.gp.findlost.util.LoadingDialog;
import com.gp.findlost.util.ResponseError;
import com.gp.findlost.views.basefragment.BaseFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddItemFragment extends BaseFragment {

    private FragmentAddItemBinding binding;

    private String type;
    private String gender;
    private String type1;
    private ProgressDialog progressDialog;

    private FirebaseStorage storage;
    private StorageReference reference;
    private ImageAdapter adapter;

    private AddChildren addChildren;
    private AddItem addItem;

    private String cityId;
    private ApiService api;
    private List<City> cities;
    private FirebaseVisionFaceDetectorOptions highAccuracyOpts;
    private FirebaseVisionImage image;

    public AddItemFragment() {
        super(R.layout.fragment_add_item, true, true, false);
    }

    @Override
    protected void doOnCreate(View view) {
        binding = DataBindingUtil.bind(view);
        toolbarName.setValue(getString(R.string.add) + " " + getString(R.string.children));
        binding.setIsChildren(true);
        type = "Children";
        type1 = "Lost";
        binding.setType(type);
        gender = "Male";

        api = ApiClient.getInstance();

        storage = FirebaseStorage.getInstance();
        reference = storage.getReference().child("Images");
        adapter = new ImageAdapter(new ArrayList<>());

        binding.imagesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.imagesRecyclerView.setAdapter(adapter);

        // High-accuracy landmark detection and face classification
        highAccuracyOpts =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.NO_LANDMARKS)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.NO_CLASSIFICATIONS)
                        .build();


        getCities();

        initViews();
    }

    private void getCities() {
        api.getCities(ApiClient.getHeaders()).enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                if (response.isSuccessful()) {
                    cities = new ArrayList<>();
                    cities.addAll(response.body());
                    initSpinner();
                } else {
                    Toast.makeText(getContext(), ResponseError.getErrorMessage(Objects.requireNonNull(response.errorBody())), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    Toast.makeText(getContext(), Constants.NO_INTERNET_MESSAGE, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initSpinner() {
        List<String> spinnerData = City.getCities(cities);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, spinnerData);
        binding.citySpinner.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityId = cities.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

    }

    private void initViews() {
        binding.typeRG.setOnCheckedChangeListener((group, checkedId) -> {
            if (binding.addChildren.getId() == checkedId) {
                binding.setIsChildren(true);
                toolbarName.setValue(getString(R.string.add) + " " + getString(R.string.children));
                type = "Children";
            } else {
                binding.setIsChildren(false);
                toolbarName.setValue(getString(R.string.add) + " " + getString(R.string.item));
                type = "Item";
            }
            binding.setType(type);
        });

        binding.type1RG.setOnCheckedChangeListener((group, checkedId) -> {
            if (binding.lost.getId() == checkedId) {
                type1 = "Lost";
            } else {
                toolbarName.setValue(getString(R.string.add) + " " + getString(R.string.item));
                type1 = "Found";
            }
        });

        binding.genderRG.setOnCheckedChangeListener((group, checkedId) -> {
            if (binding.male.getId() == checkedId) {
                gender = "Male";
            } else {
                gender = "Female";
            }
        });

        binding.addImage.setOnClickListener(v -> {
            if (type.equals("Children")) {
                if (adapter.getItemCount() < 1) {
                    pickImage();
                } else {
                    Toast.makeText(getContext(), "You can't add another image.", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (adapter.getItemCount() < 3) {
                    pickImage();
                } else {
                    Toast.makeText(getContext(), "You can't add another image.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.add.setOnClickListener(v -> {
            if (getInputData()) {
                if (adapter.getImages().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Add Images", Toast.LENGTH_SHORT).show();
                } else {
                    add();
                }
            }
        });
    }

    private void add() {
        if (type.equals("Children")) {
            addNewChildren();
        } else {
            addNewItem();
        }
    }

    private void addNewItem() {
        LoadingDialog dialog = new LoadingDialog(getContext());
        dialog.show();

        api.addNewItem(ApiClient.getHeaders(), addItem).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.hide();
                if (response.isSuccessful()) {
                    Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(R.id.actionItems);
                } else {
                    Toast.makeText(getContext(), ResponseError.getErrorMessage(Objects.requireNonNull(response.errorBody())), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.hide();
                if (t instanceof NoConnectivityException) {
                    Toast.makeText(getContext(), Constants.NO_INTERNET_MESSAGE, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addNewChildren() {
        LoadingDialog dialog = new LoadingDialog(getContext());
        dialog.show();

        api.addNewChildren(ApiClient.getHeaders(), addChildren).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.hide();
                if (response.isSuccessful()) {
                    Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(R.id.actionChildren);
                } else {
                    Toast.makeText(getContext(), ResponseError.getErrorMessage(Objects.requireNonNull(response.errorBody())), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.hide();
                if (t instanceof NoConnectivityException) {
                    Toast.makeText(getContext(), Constants.NO_INTERNET_MESSAGE, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean getInputData() {
        if (type.equals("Children")) {
            if (!InputValidator.addChildrenValidation(getActivity(), binding.addItemName, binding.addItemDescription, binding.addItemAddress, binding.addItemAge)) {
                return false;
            }
            addChildren = new AddChildren(binding.addItemName.getText().toString(), binding.addItemDescription.getText().toString(),
                    binding.addItemAddress.getText().toString(), type1, gender, Integer.parseInt(binding.addItemAge.getText().toString()), adapter.getImages(), cityId);
        } else {
            if (!InputValidator.addItemrenValidation(getActivity(), binding.addItemName, binding.addItemDescription, binding.addItemAddress)) {
                return false;
            }
            addItem = new AddItem(binding.addItemName.getText().toString(), binding.addItemDescription.getText().toString(),
                    binding.addItemAddress.getText().toString(), type1, adapter.getImages(), cityId);
        }
        return true;
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
            if (type.equals("Children")) {
                try {
                    image = FirebaseVisionImage.fromFilePath(getContext(), filerUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                detect(filerUri);
            } else {
                uploadImage(filerUri);
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
                            if (task.isSuccessful()) {
                                adapter.addImage(task.getResult().toString());
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