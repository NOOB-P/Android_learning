package com.example.android_leraning;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_leraning.databinding.ActivityWebviewBinding;

public class webview extends AppCompatActivity {
    WebSettings settings;
    ActivityWebviewBinding binding;

    WebViewClient webViewClient = new WebViewClient(){
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            if (url.startsWith("http://") || url.startsWith("https://")) {
                return false;
            } else {
                return true;
            }
        }
    };

    WebChromeClient webChromeClient = new WebChromeClient(){
        FrameLayout mframeLayout;
        View mView;
        CustomViewCallback mCallback;
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);

            mView = view;
            mCallback = callback;

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

            mframeLayout = new FrameLayout(webview.this);
            mframeLayout.setBackgroundColor(Color.BLACK);
            mframeLayout.addView(view);
            setContentView(mframeLayout);
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            mCallback.onCustomViewHidden();
            setContentView(binding.getRoot());
        }
    };

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        settings = binding.wv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);

        binding.wv.loadUrl("https://www.bing.com/");
        binding.wv.setWebViewClient(webViewClient);
        binding.wv.setWebChromeClient(webChromeClient);
    }

    public void onBackPressed() {
        if (binding.wv.canGoBack()) {
            binding.wv.goBack(); // 返回上一页网页
        } else {
            super.onBackPressed(); // 退出 Activity
        }
    }
}