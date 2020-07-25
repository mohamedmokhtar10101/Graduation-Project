package com.gp.findlost.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;


import com.gp.findlost.R;
import com.gp.findlost.callback.OnRetryClickListner;
import com.gp.findlost.databinding.DialogNoInternetBinding;

import java.util.Objects;

public class NoInternetDialog {
    private DialogNoInternetBinding binding;
    private Dialog dialog;
    private OnRetryClickListner listner;

    private Context context;

    public NoInternetDialog(Context context, OnRetryClickListner listner) {
        this.context = context;
        this.listner = listner;
        dialog = new Dialog(context);
    }

    public void show() {
        try {
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            View view = View.inflate(context, R.layout.dialog_no_internet, null);
            binding = DataBindingUtil.bind(view);
            if (listner == null) {
                Objects.requireNonNull(binding).setShowRetry(false);
            } else {
                Objects.requireNonNull(binding).setShowRetry(true);
                binding.noInternetRetry.setOnClickListener(v -> {
                    listner.onRetryClick();
                });
            }
            dialog.setContentView(binding.getRoot());
            dialog.setCancelable(true);
            dialog.show();
        } catch (NullPointerException ex) {
            Log.e("Error", Objects.requireNonNull(ex.getLocalizedMessage()));
        }
    }

    public void hide() {
        dialog.dismiss();
    }

}
