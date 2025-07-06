package com.example.android_leraning;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android_leraning.databinding.ActivityMusicplayerBinding;

public class musicplayer extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private ActivityMusicplayerBinding binding;
    public static MyHanlder hanlder;
    private music_server.myPlayerControl control;
    private Intent service;
    private myServiceCon conn;

    private boolean isBind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicplayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.BtnPlay.setOnClickListener(this);
        binding.BtnContinue.setOnClickListener(this);
        binding.BtnStop.setOnClickListener(this);
        binding.sbMusic.setOnSeekBarChangeListener(this);

        hanlder = new MyHanlder();
        conn = new myServiceCon();

        service = new Intent(this, music_server.class);
        isBind = bindService(service, conn, BIND_AUTO_CREATE);
    }


    @Override
    public void onClick(View view) {
        if (control == null) {
            // 控制对象未就绪
            Toast.makeText(this, "播放器未准备好", Toast.LENGTH_SHORT).show();
            return;
        }
        if(view.getId() == binding.BtnPlay.getId()){
            control.Play();
        }else if(view.getId() == binding.BtnContinue.getId()){
            control.Start();
        }else if(view.getId() == binding.BtnStop.getId()){
            control.Pause();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        control.Seek(seekBar.getProgress());
    }

    class MyHanlder extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int duration = bundle.getInt("duration");
            int currentPosition = bundle.getInt("currentPosition");
            binding.sbMusic.setMax(duration);
            binding.sbMusic.setProgress(currentPosition);
            binding.txtSumTime.setText((duration / 1000 / 60) + ":" + (duration / 1000 % 60));
            binding.txtSeekTime.setText((currentPosition / 1000 / 60) + ":" + (currentPosition / 1000 % 60));

        }
    }

    class myServiceCon implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            control = (music_server.myPlayerControl) iBinder;
            Log.d("mieye","服务已连接");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isBind){
            control.Pause();
            unbindService(conn);
            stopService(service);
        }
    }
}