package viettel.cyberspace.assitant.rest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiVoiceClient {

    // public static final String BASE_URL = "http://203.113.130.136:9988/";
    // public static final String BASE_URL = "http://10.30.153.132:9696/";


    //public static final String BASE_URL = "http://10.30.154.10/";
    public static final String BASE_URL = "http://203.113.152.90/";

    //http:// 203.113.152.90/hmm-stream/syn
    private static Retrofit retrofitVoice = null;


    public static Retrofit getClient() {
        if (retrofitVoice == null) {
            retrofitVoice = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okClientVOice())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitVoice;
    }

    public static OkHttpClient okClientVOice() {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
    }

}
