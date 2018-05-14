package viettel.cyberspace.assitant.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

   // public static final String BASE_URL = "http://203.113.130.136:9988/";
    public static final String BASE_URL = "http://10.30.153.132:9988/";
    public static final String SUFFIX_URL_AUTHEN = "auth";
    public static final String SUFFIX_URL_GETCONTENT = "getContent";
    public static final String SUFFIX_URL_SAVE_AUDIO = "saveAudio";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
