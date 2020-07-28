package com.gp.findlost.util;

import android.app.Activity;
import android.util.Patterns;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.gp.findlost.R;


public class InputValidator {

    @SuppressWarnings("ConstantConditions")

    public static boolean loginValidation(Activity context, EditText usernameEdt, EditText passwordEdt) {
        String username = usernameEdt.getText().toString().trim();
        String password = passwordEdt.getText().toString().trim();
        if (username.isEmpty() || username.length() != 11 || password.isEmpty()) {
            ErrorDialog errorDialog = new ErrorDialog(context);
            if (username.isEmpty()) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.email_validator));
            } else if (username.length() != 11) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.email_validation));
            } else if (password.isEmpty()) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.password_validator));
            }
            return false;
        }
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean registerValidation(Activity context, EditText firstNameEdt, EditText lastNameEdt,
                                             TextInputEditText passwordEdt, EditText phoneEdt) {

        String firstName = firstNameEdt.getText().toString().trim();
        String lastName = lastNameEdt.getText().toString().trim();
        String password = passwordEdt.getText().toString().trim();
        String phone = phoneEdt.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || password.isEmpty()
                || password.length() < 6 || phone.length() != 11) {

            ErrorDialog errorDialog = new ErrorDialog(context);

            if (firstName.isEmpty()) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.firstName_validator));
            } else if (lastName.isEmpty()) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.lastName_validator));
            } else if (password.isEmpty()) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.password_validator));
            } else if (password.length() < 6) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.password_length));
            } else if (phone.isEmpty()) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.phone_validator));
            } else if (phone.length() != 11) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.valid_mobile));
            }
            return false;
        }
        return true;
    }

    public static boolean passwordValidation(Activity context, EditText passwordEdt, EditText confirmPasswordEdt) {
        String password = passwordEdt.getText().toString().trim();
        String confirmPassword = confirmPasswordEdt.getText().toString().trim();
        if (password.isEmpty() || password.length() < 6 || confirmPassword.isEmpty() || !password.equals(confirmPassword)) {
            ErrorDialog errorDialog = new ErrorDialog(context);
            if (password.isEmpty()) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.password_validator));
            } else if (password.length() < 6) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.password_length));
            } else if (confirmPassword.isEmpty()) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.confirm_password_validator));
            } else if (!password.equals(confirmPassword)) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.password_doesnot_match));
            }
            return false;
        }
        return true;
    }

    public static boolean phoneValidation(Activity context, EditText phonedEdt) {
        String phone = phonedEdt.getText().toString().trim();
        if (phone.isEmpty() || phone.length() != 11) {
            ErrorDialog errorDialog = new ErrorDialog(context);
            if (phone.isEmpty()) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.phone_validator));
            } else if (phone.length() != 11) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.phone_length));
            }
        }
        return true;
    }

    public static boolean editProfileValidator(Activity context, EditText firstNameEditText, EditText lastNameEditText,
                                               EditText phoneEditText) {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || phone.length() != 11) {

            ErrorDialog errorDialog = new ErrorDialog(context);

            if (firstName.isEmpty()) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.firstName_validator));
            } else if (lastName.isEmpty()) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.lastName_validator));
            } else if (phone.isEmpty()) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.phone_validator));
            } else if (phone.length() != 11) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.valid_mobile));
            }
            return false;
        }

        return true;
    }

    public static boolean userResetPasswordValidation(Activity context, EditText passwordEdt, EditText confirmPasswordEdt) {

        String password = passwordEdt.getText().toString().trim();
        String confirmPassword = confirmPasswordEdt.getText().toString().trim();

        if (password.isEmpty() || confirmPassword.isEmpty() || password.length() < 6
                || !password.equals(confirmPassword)) {
            ErrorDialog errorDialog = new ErrorDialog(context);
            if (password.isEmpty()) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.password_validator));
            } else if (password.length() < 6) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.password_length));
            } else if (confirmPassword.isEmpty()) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.confirm_password_validator));
            } else if (!password.equals(confirmPassword)) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.password_doesnot_match));
            }
            return false;
        }
        return true;
    }

    public static boolean addChildrenValidation(Activity context, EditText nameEdt, EditText descriptionEdt, EditText addressEdt, EditText ageEdt) {
        String name = nameEdt.getText().toString().trim();
        String description = descriptionEdt.getText().toString().trim();
        String address = addressEdt.getText().toString().trim();
        String age = ageEdt.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty() || address.isEmpty() || age.isEmpty()) {
            ErrorDialog errorDialog = new ErrorDialog(context);
            if (name.isEmpty()) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.valid_name));
            } else if (description.isEmpty()) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.valid_description));
            } else if (address.isEmpty()) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.valid_address));
            } else {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.valid_age));
            }
            return false;
        }
        return true;
    }

    public static boolean addItemrenValidation(Activity context, EditText nameEdt, EditText descriptionEdt, EditText addressEdt) {
        String name = nameEdt.getText().toString().trim();
        String description = descriptionEdt.getText().toString().trim();
        String address = addressEdt.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty() || address.isEmpty()) {
            ErrorDialog errorDialog = new ErrorDialog(context);
            if (name.isEmpty()) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.valid_name));
            } else if (description.isEmpty()) {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.valid_description));
            } else {
                errorDialog.show();
                errorDialog.setErrorMessage(context.getString(R.string.valid_address));
            }
            return false;
        }
        return true;
    }

}