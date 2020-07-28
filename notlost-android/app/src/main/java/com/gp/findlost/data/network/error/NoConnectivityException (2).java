package com.gp.findlost.data.network.error;


import com.gp.findlost.util.Constants;

import java.io.IOException;

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return Constants.NO_INTERNET_MESSAGE;
    }
}
