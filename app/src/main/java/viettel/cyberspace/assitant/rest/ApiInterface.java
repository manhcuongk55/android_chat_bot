package viettel.cyberspace.assitant.rest;


import java.util.HashMap;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import viettel.cyberspace.assitant.model.Respose;
import viettel.cyberspace.assitant.model.StaticReponse;
import viettel.cyberspace.assitant.model.User;


public interface ApiInterface {
    @POST("api/auth")
    Call<User> getTokenAuthen(@Body HashMap<String, String> body);
//    @FormUrlEncoded
//    @POST("api/getContent")
//    Call<AudioResponse> getContent(@Field("status") String status, @Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @POST("api/statistic")
    Call<StaticReponse> getStatistic(@Body HashMap<String, String> body, @Header("Authorization") String token);

    @Multipart
    @POST("api/saveAudio")
    Call<Respose> saveAudio(@Part MultipartBody.Part file, @Query("id") String id, @Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @POST("api/register")
    Call<Respose> register(@Body HashMap<String, String> body);

}
