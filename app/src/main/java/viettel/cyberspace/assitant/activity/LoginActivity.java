package viettel.cyberspace.assitant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.cloud.android.speech.R;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import viettel.cyberspace.assitant.model.Response;
import viettel.cyberspace.assitant.model.User;
import viettel.cyberspace.assitant.rest.ApiClient;
import viettel.cyberspace.assitant.rest.ApiInterface;
import viettel.cyberspace.assitant.storage.StorageManager;
import viettel.cyberspace.assitant.utils.Const;


/**
 * Created by brwsr on 08/05/2018.
 */

public class LoginActivity extends AppCompatActivity {
    //    RelativeLayout layoutSkip;
    Button signin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signin = findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ChatBotActivity.class);
                startActivity(intent);
            }
        });

/*        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Intent intent = new Intent(LoginActivity.this, ChatViewActivity.class);
                startActivity(intent);
            }
        }, 1000);*/

    }

    private void Login() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> map = new HashMap<>();
        map.put("username", "namnh475");
        map.put("password", "123456a@");

        Call<User> call = apiService.login(map);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                int statusCode = response.code();
                if (statusCode == 200) {

                } else {
                    onLoginFailed(response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

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
