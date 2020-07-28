package com.gp.findlost.data.network.api;

import com.gp.findlost.data.model.AddChildren;
import com.gp.findlost.data.model.AddItem;
import com.gp.findlost.data.model.ChangePassword;
import com.gp.findlost.data.model.CheckRequests;
import com.gp.findlost.data.model.City;
import com.gp.findlost.data.model.EditProfile;
import com.gp.findlost.data.model.Item;
import com.gp.findlost.data.model.MyRequest;
import com.gp.findlost.data.model.Request;
import com.gp.findlost.data.model.RequestChild;
import com.gp.findlost.data.model.RequestItem;
import com.gp.findlost.data.model.SignIn;
import com.gp.findlost.data.model.SignUp;
import com.gp.findlost.data.model.Token;
import com.gp.findlost.data.model.User;
import com.gp.findlost.util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST(Constants.LOGIN)
    Call<Token> signIn(@HeaderMap Map<String, String> header,
                       @Body SignIn logIn);

    @POST(Constants.SIGN_UP)
    Call<Token> signUp(@HeaderMap Map<String, String> header,
                       @Body SignUp signUp);

    @GET(Constants.GET_ME)
    Call<User> getUser(@HeaderMap Map<String, String> header);

    @POST(Constants.REGISTER_DEVICE)
    Call<ResponseBody> registerDevice(@HeaderMap Map<String, String> header,
                                      @Body HashMap<String, String> body);

    @POST(Constants.EDIT_PROFILE)
    Call<User> editProfile(@HeaderMap Map<String, String> header,
                           @Body EditProfile user);

    @PUT(Constants.CHANGE_PASSWORD)
    Call<ResponseBody> changePassword(@HeaderMap Map<String, String> header,
                                      @Body ChangePassword changePassword);

    @GET(Constants.GET_ITEMS)
    Call<List<Item>> getItems(@HeaderMap HashMap<String, String> headers,
                              @Query("type") String type,
                              @Query("completed") Boolean isCompleted);

    @GET(Constants.GET_CHILDREN)
    Call<List<Item>> getChildren(@HeaderMap HashMap<String, String> headers,
                                 @Query("type") String type,
                                 @Query("completed") Boolean isCompleted);

    @GET(Constants.GEt_MY_POSTS)
    Call<List<Item>> getMyPosts(@HeaderMap HashMap<String, String> headers,
                                @Query("type") String type,
                                @Query("searchType") String searchType);

    @POST(Constants.ADD_NEW_CHILDREN)
    Call<ResponseBody> addNewChildren(@HeaderMap HashMap<String, String> headers,
                                      @Body AddChildren addChildren);

    @POST(Constants.ADD_NEW_ITEM)
    Call<ResponseBody> addNewItem(@HeaderMap HashMap<String, String> headers,
                                  @Body AddItem addItem);

    @GET(Constants.CITIES)
    Call<List<City>> getCities(@HeaderMap HashMap<String, String> headers);

    @POST(Constants.REQUEST_ITEM)
    Call<ResponseBody> makeRequestItem(@HeaderMap HashMap<String, String> headers,
                                       @Body RequestItem requestItem);

    @POST(Constants.REQUEST_CHILD)
    Call<ResponseBody> makeRequestChild(@HeaderMap HashMap<String, String> headers,
                                        @Body RequestChild requestChild);

    @GET(Constants.GET_REQUEST)
    Call<List<Request>> getChildRequests(@HeaderMap HashMap<String, String> headers,
                                         @Path("type") String type,
                                         @Path("id") String id);

    @POST(Constants.CHANGE_REQUEST_STATE)
    Call<ResponseBody> changeRequestState(@HeaderMap HashMap<String, String> headers,
                                          @Path("type") String type,
                                          @Path("id") String id,
                                          @Path("action") String action);

    @GET(Constants.GET_MY_REQUESTS)
    Call<List<MyRequest>> getMyRequests(@HeaderMap HashMap<String, String> headers,
                                        @Path("type") String type);

    @PUT(Constants.UPDATE_IMAGE)
    Call<ResponseBody> updateImage(@HeaderMap HashMap<String, String> headers,
                                   @Body HashMap<String, String> body);

    @GET(Constants.CHECK_REQUESTS)
    Call<CheckRequests> checkRequests(@HeaderMap HashMap<String, String> headers);
}
