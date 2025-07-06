package com.example.android_leraning;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class music_server extends Service {

    private MediaPlayer player;
    private Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new myPlayerControl();
    }

    public void addTimer(){
        if(timer == null){
            timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if(player == null){
                        return;
                    }
                    int duration = player.getDuration();
                    int currentPosition = player.getCurrentPosition();
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putInt("duration", duration);
                    bundle.putInt("currentPosition", currentPosition);
                    message.setData(bundle);
                    musicplayer.hanlder.sendMessage(message);
                }
            };
            timer.schedule(task, 5, 500);
        }
    }

    public class myPlayerControl extends Binder{
        public void Play(){
            player.seekTo(0);
            player.start();
        }

        public void Pause(){
            player.pause();
        }

        public void Start(){
            player.start();
        }

        public void Seek(int progress){
            player.seekTo(progress);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("mieye", "服务创建");
        player = new MediaPlayer();
        player.reset();
        player = MediaPlayer.create(getApplicationContext(), R.raw.music);
        addTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        if(player != null){
            if(player.isPlaying()){
                player.pause();
            }
            player.release();
            player = null;
        }
        Log.d("mieye", "服务销毁");
    }
}
