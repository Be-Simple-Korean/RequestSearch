package com.example.requestsearch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.requestsearch.R;

/**
 *  Recyclerview 아이템 클릭시 나오는 WebView 액티비티
 */
public class WebViewActivty extends AppCompatActivity {

    private static final String URL="url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_activty);
        Intent intent= getIntent();
        String url=intent.getStringExtra(URL);
        WebView web = findViewById(R.id.webview_activity_web);
        web.setWebViewClient(new MyWebViewClient());
        WebSettings set=web.getSettings();
        set.setJavaScriptEnabled(true);
        web.loadUrl(url);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            if(view.canGoBack()){
                finish();
            }
            super.onPageFinished(view, url);
        }
    }

}