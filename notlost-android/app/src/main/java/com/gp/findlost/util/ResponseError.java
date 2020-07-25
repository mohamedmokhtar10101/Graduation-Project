package com.gp.findlost.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gp.findlost.data.model.ErrorMessage;

import java.lang.reflect.Type;

import okhttp3.ResponseBody;

public class ResponseError {
    public static String getErrorMessage(ResponseBody responseBody) {
        Gson gson = new Gson();
        Type type = new TypeToken<ErrorMessage>() {
        }.getType();
        ErrorMessage errorResponse = gson.fromJson(responseBody.charStream(), type);
        return errorResponse.getErrorMessage();
    }
}
