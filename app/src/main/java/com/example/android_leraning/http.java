package com.example.android_leraning;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_leraning.databinding.ActivityHttpBinding;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class http extends AppCompatActivity implements View.OnClickListener {
    ActivityHttpBinding binding;
    URL url;
    Handler handler;
    Thread thread;
    String output;

    Runnable http = new Runnable() {
        @Override
        public void run() {
            try {
                url = new URL(binding.EtHost.getText().toString().trim());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(6000);
                byte[] data  = new byte[1024];
                int len = 0;
                output = "host:" + url.getHost() + "\n";
                output += "Protocol:" + url.getProtocol() + "\n";
                output += "port:" + url.getPort() + "\n";
               do{
                    len = conn.getInputStream().read(data);
                    if(len != -1){
                        output += new String(data, 0, len);
                    }
                }while(len != -1);

                handler.post(()->{
                   binding.tvOutput.setText(output);
                });
            } catch (MalformedURLException e) {
                Log.e("mieye", e.toString());
            } catch (IOException e) {
                Log.e("mieye", e.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHttpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.BtnAccess.setOnClickListener(this);

        handler = new Handler();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == binding.BtnAccess.getId()){
            thread = new Thread(http);
            thread.start();
        }
    }
}