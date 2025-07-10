package com.example.android_leraning;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_leraning.databinding.ActivityBluetoothBinding;

import java.util.ArrayList;

public class bluetooth extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    ActivityBluetoothBinding binding;
    BluetoothAdapter mBlueToothAdapter;
    ArrayAdapter<String> mMsgListAdapter;
    ArrayList<String> mMsgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBluetoothBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnCheck.setOnClickListener(this);
        binding.btnSend.setOnClickListener(this);
        binding.spBluetooth.setOnItemClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == binding.btnSend.getId()){

        }else if(view.getId() == binding.btnCheck.getId()){
            mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(mBlueToothAdapter == null){
                Toast.makeText(this, "无蓝牙设备", Toast.LENGTH_SHORT).show();
                return;
            }else if(!mBlueToothAdapter.isEnabled()){
                Toast.makeText(this, "蓝牙未开启", Toast.LENGTH_SHORT).show();
                return;
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}