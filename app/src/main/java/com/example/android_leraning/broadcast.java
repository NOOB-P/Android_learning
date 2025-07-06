package com.example.android_leraning;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android_leraning.databinding.ActivityBroadcastBinding;

public class broadcast extends AppCompatActivity implements View.OnClickListener {

    ActivityBroadcastBinding binding;
    MyBatteryReceiver myBatteryReceiver;
    MyReceiver1 myReceiver1;
    MyReceiver2 myReceiver2;
    MyReceiver3 myReceiver3;
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBroadcastBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.BtnSendBoradcast.setOnClickListener(this);
        binding.BtnSendOrderBoradcast.setOnClickListener(this);

        myBatteryReceiver = new MyBatteryReceiver();
        myReceiver1 = new MyReceiver1();
        myReceiver2 = new MyReceiver2();
        myReceiver3 = new MyReceiver3();

        //动态注册
        IntentFilter filterbroadcast = new IntentFilter();
        filterbroadcast.addAction(Intent.ACTION_BATTERY_CHANGED);
        filterbroadcast.addAction(Intent.ACTION_BATTERY_OKAY);
        filterbroadcast.addAction(Intent.ACTION_BATTERY_LOW);
        registerReceiver(myBatteryReceiver, filterbroadcast);

        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("my_broadcast");
        filter1.setPriority(1000);
        registerReceiver(myReceiver1, filter1);

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction("my_broadcast");
        filter2.setPriority(800);
        registerReceiver(myReceiver2, filter2);

        IntentFilter filter3 = new IntentFilter();
        filter3.addAction("my_broadcast");
        filter3.setPriority(500);
        registerReceiver(myReceiver3, filter3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBatteryReceiver);
        unregisterReceiver(myReceiver1);
        unregisterReceiver(myReceiver2);
        unregisterReceiver(myReceiver3);
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == binding.BtnSendBoradcast.getId()){
            binding.txt1.setVisibility(View.GONE);
            binding.txt2.setVisibility(View.GONE);
            binding.txt3.setVisibility(View.GONE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                // 延迟2秒后执行
                Intent intent = new Intent();
                intent.setAction("my_broadcast");
                sendBroadcast(intent);
            }, 1000);

        }else if(view.getId() == binding.BtnSendOrderBoradcast.getId()){
            binding.txt1.setVisibility(View.GONE);
            binding.txt2.setVisibility(View.GONE);
            binding.txt3.setVisibility(View.GONE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                // 延迟2秒后执行
                Intent intent = new Intent();
                intent.setAction("my_broadcast");
                sendOrderedBroadcast(intent,null);
            }, 1000);
        }
    }

    public class MyReceiver1 extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("my_broadcast")){
                binding.txt1.setVisibility(View.VISIBLE);
            }

        }
    }

    public class MyReceiver2 extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("my_broadcast")) {
                binding.txt2.setVisibility(View.VISIBLE);
                Toast.makeText(context, "2开始截断", Toast.LENGTH_SHORT).show();
                abortBroadcast();
            }
        }
    }
    public class MyReceiver3 extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("my_broadcast")) {
                binding.txt3.setVisibility(View.VISIBLE);
            }
        }
    }

    public class MyBatteryReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;

            float batteryPct = level * 100 / (float)scale;

            Log.d("Battery", "电量: " + batteryPct + "%，是否充电：" + isCharging);
            binding.txtBattery.setText("电量: " + batteryPct + "%，是否充电：" + isCharging);
        }
    }
}