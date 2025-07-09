package com.example.android_leraning;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ComponentCaller;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_leraning.databinding.ActivityTcpSocketBinding;
import com.google.android.material.color.utilities.Scheme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class tcp_socket extends AppCompatActivity implements View.OnClickListener {
    private int PROT = 19191;
    private File file;
    private Uri fileUri;
    private ActivityTcpSocketBinding binding;
    private Socket socket;
    private Handler handler;
    private List<String> list;
    private ArrayAdapter<String> adapter;
    private String filepath;
    //@SuppressLint("Range")
    @SuppressLint("Range")
    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    fileUri = data.getData();
                    String scheme = fileUri.getScheme();
                    if (scheme == null || ContentResolver.SCHEME_FILE.equals(scheme)) {
                        filepath = fileUri.getPath();
                    } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                        Cursor cursor = getContentResolver().query(fileUri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                        if (cursor.moveToNext())
                            filepath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                    }
                    file = new File(filepath);
                    binding.tvPath.setText(file.getName().toString().trim());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTcpSocketBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.BtnSend1.setOnClickListener(this);
        binding.BtnSend2.setOnClickListener(this);
        binding.BtnChoice.setOnClickListener(this);

        String[] permission = {
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE"
        };
        List<String> permissionlist = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            if(checkSelfPermission(permission[i]) != PackageManager.PERMISSION_GRANTED){
                permissionlist.add(permission[i]);
            }
        }
        if(!permissionlist.isEmpty()){
            requestPermissions(permissionlist.toArray(new String[permissionlist.size()]), 2);
        }

        handler = new Handler();
        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        binding.lvText.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 2){
            boolean granted = true;
            for (int grantResult : grantResults) {
                granted = granted && (grantResult == PackageManager.PERMISSION_GRANTED);
            }
            if (!granted) {
                Toast.makeText(this, "权限获取失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == binding.BtnSend1.getId()){
            String serverip = binding.ETIp.getText().toString().trim();
            String content = binding.ETText.getText().toString().trim();
            Thread thread = new Thread(() -> {
                try {
                    socket = new Socket(serverip, PROT);
                    OutputStream outputStream = socket.getOutputStream();
                    String content_package = "txt:" + content.getBytes().length + ":" + content;
                    outputStream.write(content_package.getBytes());
                    outputStream.flush();
                    outputStream.close();
                    handler.post(()->{
                        list.add(serverip + ":" + content);
                        adapter.notifyDataSetChanged();
                    });
                } catch (IOException e) {
                    handler.post(()->{
                        Toast.makeText(this, "发送失败",Toast.LENGTH_SHORT).show();
                    });

                }
            });
            thread.start();
        }else if(view.getId() == binding.BtnSend2.getId()){
            String serverip = binding.ETIp.getText().toString().trim();
            try {
                if(fileUri == null){
                    return;
                }
                InputStream inputStream = getContentResolver().openInputStream(fileUri);
                if (inputStream == null) {
                    return;
                }
                int count = inputStream.available();
                byte[] pic = new byte[count];
                inputStream.read(pic);
                inputStream.close();
                String head = "pic:" + file.getName() + ":" + pic.length + ":";
                byte[] data = new byte[head.length() + count];
                System.arraycopy(head.getBytes(), 0, data, 0, head.length());
                System.arraycopy(pic, 0, data, head.length(), pic.length);
                Thread thread = new Thread(() -> {
                    try {
                        socket = new Socket(serverip, PROT);
                        OutputStream outputStream = socket.getOutputStream();
                        outputStream.write(data);
                        outputStream.flush();
                        outputStream.close();
                        handler.post(()->{
                            list.add(serverip + "（文件发送）:" + file.getName());
                            adapter.notifyDataSetChanged();
                        });
                    } catch (IOException e) {
                        handler.post(()->{
                            Toast.makeText(this, "发送失败",Toast.LENGTH_SHORT).show();
                        });

                    }
                });
                thread.start();
            } catch (IOException e) {
                Log.e("mieye", e.toString());
            }

        }else if(view.getId() == binding.BtnChoice.getId()){
            Intent intent = new Intent();
            intent.setType("image/ *");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            resultLauncher.launch(intent);
        }
    }
}