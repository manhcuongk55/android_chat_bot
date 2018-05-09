package viettel.cyberspace.assitant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.cloud.android.speech.R;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viettel.cyberspace.assitant.model.Respose;
import viettel.cyberspace.assitant.model.User;
import viettel.cyberspace.assitant.rest.ApiClient;
import viettel.cyberspace.assitant.rest.ApiInterface;
import viettel.cyberspace.assitant.storage.StorageManager;
import viettel.cyberspace.assitant.utils.Const;


/**
 * Created by brwsr on 08/05/2018.
 */

public class LoginActivity extends AppCompatActivity {
    RelativeLayout layoutSkip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        layoutSkip = findViewById(R.id.layoutSkip);
        layoutSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ChatViewActivity.class);
                startActivity(intent);
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Intent intent = new Intent(LoginActivity.this, ChatViewActivity.class);
                startActivity(intent);
            }
        }, 1000);

    }

    private void Login() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> map = new HashMap<>();
        map.put("email", "phamduybk@gmail.com");
        map.put("password", "123456");
        StorageManager.setStringValue(getApplicationContext(), Const.NAME_CONSUMER, "phamduybk@gmail.com");
        Call<User> call = apiService.getTokenAuthen(map);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                int statusCode = response.code();
                if (statusCode == 200) {
                    if (response.body().getToken() != null) {
                        StorageManager.setStringValue(getApplicationContext(), Const.TOKEN, response.body().getToken());
                        onLoginSuccess();
                    } else {
                        onLoginFailed("token null");
                    }
                } else {
                    onLoginFailed(response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Log error here since request failed

                onLoginFailed(t.toString());
            }
        });

    }

    private void onLoginSuccess() {

        //dang nhap thanh cong
    }

    public void onLoginFailed(String statusCode) {
        Toast.makeText(getBaseContext(), statusCode, Toast.LENGTH_LONG).show();
    }
}
