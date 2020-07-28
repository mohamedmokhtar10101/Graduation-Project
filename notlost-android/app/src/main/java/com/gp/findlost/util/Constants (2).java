package com.gp.findlost.util;

public class Constants {
    //////////////////////////////// Network ///////////////////////////////////////////
    public static final String BASE_URL = "https://notlost.herokuapp.com/api/";
    public static final String AUTH = "auth/";
    public static final String USERS = "users/";
    public static final String LOGIN = BASE_URL + USERS + "login/";
    public static final String SIGN_UP = BASE_URL + USERS + "register/";
    public static final String GET_ITEMS = BASE_URL + "items/";
    public static final String GET_CHILDREN = BASE_URL + "children/";
    public static final String CHANGE_PASSWORD = "";
    public static final String GET_ME = BASE_URL + USERS + "me/";
    public static final String REGISTER_DEVICE = BASE_URL + USERS + "registerDevice/";
    public static final String EDIT_PROFILE = BASE_URL + USERS + "editProfile/";
    public static final String GEt_MY_POSTS = BASE_URL + USERS + "myPosts/";
    public static final String ADD_NEW_CHILDREN = BASE_URL + "children/";
    public static final String ADD_NEW_ITEM = BASE_URL + "items/";
    public static final String CITIES = BASE_URL + "cities/";
    public static final String REQUEST_CHILD = BASE_URL + "childrequests/";
    public static final String REQUEST_ITEM = BASE_URL + "itemrequests/";
    public static final String GET_REQUEST = BASE_URL + "{type}" + "requests/{id}";
    public static final String CHANGE_REQUEST_STATE = BASE_URL + "{type}" + "requests/{action}/{id}";
    public static final String GET_MY_REQUESTS = BASE_URL + "{type}" + "requests/auth/myRequests";
    public static final String UPDATE_IMAGE = BASE_URL + USERS + "auth/updateImage";
    public static final String CHECK_REQUESTS = BASE_URL + USERS + "hasNonCompletedRequest/";

    //////////////////////////////// Paginator /////////////////////////////////////////
    public static final int PAGE_NUMBER = 1;
    public static final int PAGE_SIZE = 10;


    /////////////////////////////////////// Shared Preference /////////////////////////////
    public static final String KEY_TOKEN = "x-auth-token";
    public static final String USER = "user";
    public static final String SHOW_INTRO = "showIntro";
    public static final String KEY_LOCALE = "locale";

    /////////////////////////////////////// Splash /////////////////////////////
    public static final int SPLASH_DISPLAY_LENGTH = 2000;
    public static final int BOOKED_DISPLAY_LENGTH = 1000;


    /////////////////////////////////////// Custom Alert Dialog /////////////////////////////
    public static final String ERROR = "error";
    public static final String WARNING = "warning";
    public static final String SUCCESS = "success";

    /////////////////////////////////////// Intent /////////////////////////////
    public static final String TYPE = "type";
    public static final String MESSAGE = "message";


    /////////////////////////////////////// Message /////////////////////////////
    public static final String NO_INTERNET_MESSAGE = "Check Your Internet Connection";

    /////////////////////////////////////// Sign Up /////////////////////////////
    public static final String REGISTER = "register";
    public static final String RESET_PASSWORD = "resetPassword";
    public static final String VERIFICATION = "verification";
    public static final String SET_PASSWORD = "setPassword";
    public static final int RESEND_SMS_AFTER_FOR_SECOND = 30;


}
