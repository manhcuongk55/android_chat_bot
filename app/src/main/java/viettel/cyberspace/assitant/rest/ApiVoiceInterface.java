package viettel.cyberspace.assitant.rest;


import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ApiVoiceInterface {
    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    //@POST("/tts-hmm/syn")
    @POST("/hmm-stream/syn")
    Call<ResponseBody> getVoice(@Field("data") String data, @Field("voices") String voices, @Field("key") String key);
    //@POST("/hmm-stream/syn")
}
