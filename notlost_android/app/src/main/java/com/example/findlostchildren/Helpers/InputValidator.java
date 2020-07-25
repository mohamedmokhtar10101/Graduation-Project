package com.example.findlostchildren.Helpers;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

public class InputValidator {

    public static boolean signUpValidation(Context context, EditText userName, EditText emailET, EditText passwordET, EditText confirmPasswordET) {

        String name = userName.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String confirmPassword = confirmPasswordET.getText().toString().trim();
        if (name.isEmpty() || email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.isEmpty() || confirmPassword.isEmpty() || password.length() < 6 || !password.equals(confirmPassword)) {

            if (name.isEmpty())
                //userName.setError("يرجي كتابه أسم المستخدم");
                Toast.makeText(context, "يرجي كتابة اسم المستخدم", Toast.LENGTH_LONG).show();

            if (email.isEmpty())
                Toast.makeText(context, "يرجي كتابة البريد الألكتروني", Toast.LENGTH_LONG).show();

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                Toast.makeText(context, "البريد الألكتروني غير صالح", Toast.LENGTH_LONG).show();

            if (password.isEmpty())
                //passwordET.setError("يرجي كتابة الباسورد");
                Toast.makeText(context, "يرجي كتابة الباسورد", Toast.LENGTH_LONG).show();

            if (confirmPassword.isEmpty())
                Toast.makeText(context, "يرجي تأكيد كلمة السر", Toast.LENGTH_LONG).show();

            if (password.length() < 6)
                Toast.makeText(context, "يجب ادخال كلمه سر اكبر من 6", Toast.LENGTH_LONG).show();

            if (!(password.equals(confirmPassword)))
                Toast.makeText(context, "كلمه السر غير متطابقه", Toast.LENGTH_LONG).show();

            return false;
        }
        return true;
    }

    public static boolean signInValidation(Context context, EditText emailET, EditText passwordET) {

        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.isEmpty()) {

            if (email.isEmpty())
                Toast.makeText(context, "يرجي كتابة البريد الألكتروني", Toast.LENGTH_LONG).show();

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                Toast.makeText(context, "البريد الألكتروني غير صالح", Toast.LENGTH_LONG).show();

            if (password.isEmpty())
                Toast.makeText(context, "يرجي كتابة الباسورد", Toast.LENGTH_LONG).show();

            return false;
        }
        return true;
    }

    public static boolean victimValidation(Context context, EditText victimNameET, EditText victimCityET, EditText victimAgeET, EditText victimBNumberET, EditText victimDescritionET) {
        String name = victimNameET.getText().toString();
        String city = victimCityET.getText().toString();
        String age = victimAgeET.getText().toString().trim();
        String number = victimBNumberET.getText().toString().trim();
        String description = victimDescritionET.getText().toString();

        if (name.isEmpty() || city.isEmpty() || age.isEmpty() || number.isEmpty() || number.length() != 11 || description.isEmpty()) {

            if (name.isEmpty())
                victimNameET.setError("Please Enter Victim Name");

            if (city.isEmpty())
                victimCityET.setError("Please Enter Victim City");

            if (age.isEmpty())
                victimAgeET.setError("Please Enter Victim Age");

            if (number.length() != 11)
                victimBNumberET.setError("Please Enter Valid Phone Number");

            if (number.isEmpty())
                victimBNumberET.setError("Please Enter Victim Number Number");
            
            if (description.isEmpty())
                victimDescritionET.setError("Please Enter Victim Description");

            return false;
        }
        return true;

    }
}
