package viettel.cyberspace.assitant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.cloud.android.speech.R;


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

    }
}
