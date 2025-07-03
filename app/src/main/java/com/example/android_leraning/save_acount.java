package com.example.android_leraning;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android_leraning.databinding.ActivitySvaeAcountBinding;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class save_acount extends AppCompatActivity implements View.OnClickListener {

    ActivitySvaeAcountBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySvaeAcountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            readData();
        } catch (IOException e) {
            Log.e("mieye",e.toString());
        }
        binding.BtnSave1.setOnClickListener(this);
        binding.BtnSave2.setOnClickListener(this);
    }

    public void readData() throws IOException {
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        String account = sp.getString("account", null);
        String password = sp.getString("password", null);
        if(!account.isEmpty() && !password.isEmpty()){
            binding.editAccount2.setText(account);
            binding.editPassword2.setText(password);
        }

        FileInputStream fis = openFileInput("data1");
        byte[] content = new byte[fis.available()];
        fis.read(content);
        String user = new String(content);
        account = user.split(" ")[0];
        password = user.split(" ")[1];
        if(!account.isEmpty() && !password.isEmpty()){
            binding.editAccount1.setText(account);
            binding.editPassword1.setText(password);
        }
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == binding.BtnSave1.getId()){
            String account = binding.editAccount1.getText().toString().trim();
            String password = binding.editPassword1.getText().toString().trim();
            if(!account.isEmpty() && !password.isEmpty()){
                try {
                    FileOutputStream fos = openFileOutput("data1", MODE_PRIVATE);
                    fos.write(account.getBytes());
                    fos.write(" ".getBytes());
                    fos.write(password.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "账号密码为空", Toast.LENGTH_SHORT).show();
            }
        }else if(view.getId() == binding.BtnSave2.getId()){
            String account = binding.editAccount2.getText().toString().trim();
            String password = binding.editPassword2.getText().toString().trim();
            if(!account.isEmpty() && !password.isEmpty()){
                SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("account", account);
                editor.putString("password", password);
                editor.commit();
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "账号密码为空", Toast.LENGTH_SHORT).show();
            }
        }
    }
}