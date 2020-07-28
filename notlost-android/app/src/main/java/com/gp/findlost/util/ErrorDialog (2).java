package com.gp.findlost.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.gp.findlost.R;
import com.gp.findlost.callback.OnBlockClickListner;
import com.gp.findlost.callback.OnRemoveClickListner;
import com.gp.findlost.callback.OnRetryClickListner;
import com.gp.findlost.databinding.DialogErrorBinding;

import java.util.Objects;

public class ErrorDialog {
    private DialogErrorBinding binding;
    private Dialog dialog;
    private OnRetryClickListner listner;
    private OnBlockClickListner blockListner;
    private Context context;
    private String errorMessage;
    public Button btn;
    public ImageView errorImage;
    public TextView errorTV;

    public ErrorDialog(Context context, String errorMessage, OnRetryClickListner listner, OnBlockClickListner blockListner) {
        this.context = context;
        this.errorMessage = errorMessage;
        this.listner = listner;
        this.blockListner = blockListner;
        dialog = new Dialog(context);
    }

    public ErrorDialog(Context context) {
        this.context = context;
        dialog = new Dialog(context);
    }

    public void setListner(OnRetryClickListner listner) {
        this.listner = listner;
    }

    public void show() {
        try {
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            View view = View.inflate(context, R.layout.dialog_error, null);
            binding = DataBindingUtil.bind(view);
            if (listner == null) {
                Objects.requireNonNull(binding).setShowRetry(false);
            } else {
                Objects.requireNonNull(binding).setShowRetry(true);
                binding.errorRetry.setOnClickListener(v -> {
                    blockListner.onBlockClicked();
                });
                binding.remove.setOnClickListener(v -> {
                    listner.onRetryClick();
                });
            }

            btn = binding.errorRetry;
            errorImage = binding.errorImage;
            errorTV = binding.ErrorTV;

            binding.setErrorMessage(errorMessage);
            dialog.setContentView(binding.getRoot());
            dialog.setCancelable(true);
            dialog.show();
        } catch (NullPointerException ex) {
            Log.e("Error", Objects.requireNonNull(ex.getLocalizedMessage()));
        }
    }

    public void setErrorMessage(String errorMessage) {
        binding.setErrorMessage(errorMessage);
    }

    public void hide() {
        dialog.dismiss();
    }
}
