package com.gp.findlost.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.gp.findlost.R;

import java.util.Objects;

public class LoadingDialog {
    private Dialog dialog;

    public LoadingDialog(Context context) {
        dialog = new Dialog(context);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);
    }

    public void show() {
        dialog.show();
    }

    public void hide() {
        dialog.cancel();
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }
}
