package viettel.cyberspace.assitant.Webview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.cloud.android.speech.R;


/**
 * Created by brwsr on 09/05/2018.
 */

public class WebviewActivity extends AppCompatActivity {
    WebView webView;
    TextView tvContent;
    TextView tvUrl;
    String weblink;
    ProgressBar progressBarLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_webview);
        Intent intent = getIntent();
        weblink = intent.getStringExtra("weblink");
        Log.v("trungbd", weblink);
        webView = (WebView) findViewById(R.id.webView);
        tvContent = findViewById(R.id.tvContent);
        tvUrl = findViewById(R.id.tvUrl);
        progressBarLoading = findViewById(R.id.progressBarLoading);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                tvUrl.setText(view.getUrl());
                tvContent.setText(view.getTitle());
//                progressBarLoading.setVisibility(View.GONE);
            }

        });
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBarLoading.setVisibility(View.VISIBLE);
                progressBarLoading.setProgress(progress);
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(weblink);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
