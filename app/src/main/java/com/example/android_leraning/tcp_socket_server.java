package com.example.android_leraning;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.GnssAntennaInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_leraning.databinding.ActivityTcpSocketServerBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class tcp_socket_server extends AppCompatActivity {
    private int PROT = 19191;
    private Thread thread;
    private ActivityTcpSocketServerBinding binding = null;
    private ServerSocket serverSocket = null;
    private List<String> list = null;
    private ArrayAdapter<String> adapter = null;
    private String information = null, ip = null;
    private Handler handler = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTcpSocketServerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            serverSocket = new ServerSocket(PROT);
        } catch (IOException e) {
            Toast.makeText(this, "未连接",Toast.LENGTH_SHORT).show();
        }
        handler = new Handler();
        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list);
        binding.lvText.setAdapter(adapter);

        thread = new Thread(() -> {
            try {
                while (true) {
                    Socket socket = serverSocket.accept();
                    ip = socket.getInetAddress().toString().trim();
                    InputStream inputStream = socket.getInputStream();
                    /// 不要用inputStream.available()!!!!!!不可靠 ///
                    byte[] data = new byte[1024];
                    inputStream.read(data);
                    String[] sp = new String(data).split(":");
                    if(sp[0].equals("txt")){
                        information = sp[2];
                        handler.post(() -> {
                            list.add(ip + ":" + information);
                            adapter.notifyDataSetChanged();
                        });
                    }else if(sp[0].equals("pic")){
                        int receivedBytes = data.length;
                        int totalSize = Integer.parseInt(sp[2]);
                        information = sp[1];
                        FileOutputStream fos = openFileOutput(sp[1], MODE_PRIVATE);
                        String head = sp[0] + ":" + sp[1] + ":" + sp[2] + ":";
                        fos.write(data, head.getBytes().length, data.length - head.getBytes().length);

                        while (receivedBytes < totalSize) {
                            int bytesRead = inputStream.read(data);
                            if (bytesRead == -1) break;
                            fos.write(data, 0, bytesRead);
                            receivedBytes += bytesRead;
                            Log.d("TCP", "进度: " + receivedBytes + "/" + totalSize);
                        }
                        fos.flush();
                        fos.close();
                        handler.post(() -> {
                            list.add(ip + ":(接收图片)" + information);
                            adapter.notifyDataSetChanged();

                            //加载本地图片
                            File file = new File(getFilesDir(), information);
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            binding.ivRev.setImageBitmap(bitmap);

                        });
                    }
                    inputStream.close();
                }
            } catch (IOException e) {
                handler.post(()->{
                    Toast.makeText(this, "接收失败",Toast.LENGTH_SHORT).show();
                });
            }
        });
        thread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            serverSocket.close();
        } catch (IOException e) {
            Toast.makeText(this, "关闭失败",Toast.LENGTH_SHORT).show();
        }
    }
}