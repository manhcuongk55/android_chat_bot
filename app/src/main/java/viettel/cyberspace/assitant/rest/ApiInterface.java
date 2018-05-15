package viettel.cyberspace.assitant.rest;


import java.util.HashMap;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import viettel.cyberspace.assitant.model.BaseResponse;
import viettel.cyberspace.assitant.model.RateMessageResponse;
import viettel.cyberspace.assitant.model.Response;
import viettel.cyberspace.assitant.model.ResponseMessage;
import viettel.cyberspace.assitant.model.StaticReponse;
import viettel.cyberspace.assitant.model.User;


public interface ApiInterface {

    //new 2018
    @Headers("Content-Type: application/json")
    @POST("va/login")
    Call<User> login(@Body HashMap<String, String> body);

    @Headers("Content-Type: application/json")
    @POST("va/send-message")
    Call<ResponseMessage> sendMessage(@Body HashMap<String, String> body);

//    @GET("va/get-answer/")
//    Call<Response> getAnswer(@Query("username") String username,
//                             @Query("mid") String mid);

    @Headers("Content-Type: application/json")
    @POST("va/get-answer")
    Call<BaseResponse> getAnswer(@Body HashMap<String, String> body);

    @Headers("Content-Type: application/json")
    @POST("va/rate-message")
    Call<RateMessageResponse> rateMessage(@Body HashMap<String, String> body);


}
