
package com.example.android_leraning;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_leraning.databinding.ActivityUdpSocketBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class udp_socket extends AppCompatActivity implements View.OnClickListener {

    private int IO_SIZE = 1024;
    private int PROT = 38383;
    private ActivityUdpSocketBinding binding;
    private DatagramSocket datagramSocket;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private Thread thread_send1, thread_send2, thread_rev;
    private Handler handler;
    private Uri fileUri;
    private String fileName;
    private boolean isThreadRun;
    @SuppressLint("Range")
    private ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result->{
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    fileUri = data.getData();
                    String scheme = fileUri.getScheme();
                    String filepath = "";
                    if (scheme == null || ContentResolver.SCHEME_FILE.equals(scheme)) {
                        filepath = fileUri.getPath();
                    } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                        Cursor cursor = getContentResolver().query(fileUri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                        if (cursor.moveToNext()){
                            filepath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                        }
                    }
                    File file = new File(filepath);
                    fileName = file.getName();
                    binding.tvPath.setText(fileName.trim());
                }
    });
    private Runnable sendWork1 = new Runnable() {
        @Override
        public void run() {
            try {
                String content = binding.ETText.getText().toString().trim();
                String ip = binding.ETIp.getText().toString().trim();
                String data = "txt:" + content.getBytes().length + ":" + content;
                InetAddress inetAddress = InetAddress.getByName(ip);
                DatagramPacket packet = new DatagramPacket(data.getBytes(), data.getBytes().length, inetAddress, PROT);
                datagramSocket.send(packet);
                handler.post(()->{
                    arrayList.add("发送文本(" + ip + "):" + content);
                    adapter.notifyDataSetChanged();
                });
            } catch (IOException e) {
                Log.e("mieye", e.toString());
            }
        }
    };

    private Runnable sendWork2 = new Runnable() {
        @Override
        public void run() {

            try {
                int sendSize = 0;
                int offset = 0;
                byte[] data = new byte[IO_SIZE];
                String ip = binding.ETIp.getText().toString().trim();
                InputStream fis = getContentResolver().openInputStream(fileUri);
                int realSize = fis.available();
                byte[] pic = new byte[realSize];
                fis.read(pic);
                fis.close();

                //发送内容
                while(offset < realSize){
                    int len = Math.min(950, realSize - offset);
                    String head = "pic:" + fileName + ":" + realSize + ":" + offset + ":" + len + ":";
                    System.arraycopy(head.getBytes(), 0, data, 0, head.getBytes().length);
                    System.arraycopy(pic, offset, data, head.getBytes().length, len);
                    offset += len;
                    DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(ip), PROT);
                    datagramSocket.send(packet);
                    Log.d("mieye", fileName + ":" + offset + "/" + realSize);
                }
                handler.post(()->{
                   arrayList.add("发送图片(" + ip + "):" + fileName);
                   adapter.notifyDataSetChanged();
                });
            } catch (FileNotFoundException e) {
                Log.e("mieye", e.toString());
            } catch (IOException e) {
                Log.e("mieye", e.toString());
            }
        }
    };

    private Runnable receive = new Runnable() {
        @Override
        public void run() {
            byte[] data = new byte[IO_SIZE];
            while(isThreadRun){
                try {
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    datagramSocket.receive(packet);
                    String[] sp = new String(packet.getData()).split(":");
                    if(sp[0].equals("txt")){
                        int len = Integer.parseInt(sp[1]);
                        String head = "txt:" + sp[1] + ":";
                        String content = new String(packet.getData(), head.length(), len);
                        handler.post(()->{
                            arrayList.add("接收文本(" + packet.getAddress().toString() + "):" + content);
                            adapter.notifyDataSetChanged();
                        });
                    }else if(sp[0].equals("pic")){
                        String head = sp[0] + ":" + sp[1] + ":" + sp[2] + ":" + sp[3] + ":" + sp[4] + ":";
                        int realSize = Integer.parseInt(sp[2]);
                        String save_name = sp[1];
                        int offset = Integer.parseInt(sp[3]);
                        int len = Integer.parseInt(sp[4]);
                        File file = new File(getFilesDir(), save_name);
                        RandomAccessFile raf = new RandomAccessFile(file, "rw");
                        byte[] pic = new byte[len];
                        System.arraycopy(packet.getData(), head.getBytes().length, pic, 0, len);
                        raf.seek(offset);
                        raf.write(pic);
                        raf.close();
                        Log.d("mieye", save_name + ":" + offset + "/" + realSize);
                        if(offset + len == realSize){
                            handler.post(()->{
                                arrayList.add("接收图片(" + packet.getAddress().toString() + "):" + save_name);
                                adapter.notifyDataSetChanged();
                            });
                        }
                    }
                } catch (IOException e) {
                    Log.e("mieye", e.toString());
                }
            }
        }
    };

    private void getPermission(){
        String[] permissions = {
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE"
        };
        ArrayList<String> permission_list = new ArrayList<>();
        for(String permission : permissions){
            if(checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
                permission_list.add(permission);
            }
        }
        if(!permission_list.isEmpty()){
            requestPermissions(permission_list.toArray(new String[permission_list.size()]), 2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 2){
            boolean isGrant = true;
            for(int result : grantResults){
                if(result != PackageManager.PERMISSION_GRANTED){
                    isGrant = false;
                }
            }
            if(!isGrant){
                handler.post(()->{
                   Toast.makeText(this, "权限申请失败", Toast.LENGTH_SHORT).show();
                });
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUdpSocketBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.BtnChoice.setOnClickListener(this);
        binding.BtnSend1.setOnClickListener(this);
        binding.BtnSend2.setOnClickListener(this);

        getPermission();

        try {
            datagramSocket = new DatagramSocket(PROT);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        isThreadRun = true;
        handler = new Handler();
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        binding.lvText.setAdapter(adapter);

        thread_rev = new Thread(receive);
        thread_rev.start();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == binding.BtnChoice.getId()){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            resultLauncher.launch(intent);
        }else if(view.getId() == binding.BtnSend1.getId()){
            thread_send1 = new Thread(sendWork1);
            thread_send1.start();
        }else if(view.getId() == binding.BtnSend2.getId()){
            thread_send2 = new Thread(sendWork2);
            thread_send2.start();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        isThreadRun =false;

        if (thread_rev != null) {
            thread_rev.interrupt();
        }
        if (thread_send1 != null) {
            thread_send1.interrupt();
        }
        if (thread_send2 != null) {
            thread_send2.interrupt();
        }
        if(datagramSocket != null){
            datagramSocket.close();
        }
    }
}