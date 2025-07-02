package com.example.android_leraning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_leraning.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.BtnRegister.setOnClickListener(this);
        binding.BtnNews.setOnClickListener(this);
        binding.BtnStore.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //用switch case 报错
        if(view.getId() == R.id.Btn_register){
            Intent intent = new Intent(this, register.class);
            startActivity(intent);
        }else if(view.getId() == R.id.Btn_store){
            Intent intent = new Intent(this, shop.class);
            startActivity(intent);
        }else if(view.getId() == R.id.Btn_news){

        }
    }
}