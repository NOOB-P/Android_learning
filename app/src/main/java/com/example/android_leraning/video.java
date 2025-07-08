package com.example.android_leraning;

import android.content.pm.ActivityInfo;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_leraning.databinding.ActivityVideoBinding;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class video extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener, SurfaceHolder.Callback, SeekBar.OnSeekBarChangeListener {
    ActivityVideoBinding binding;
    MediaPlayer mediaPlayer;
    SurfaceHolder holder;
    String uri;
    Timer timer;
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.BtnVvContinue.setOnClickListener(this);
        binding.BtnVvPause.setOnClickListener(this);
        binding.BtnVvPlay.setOnClickListener(this);
        binding.vvPlayer.setOnCompletionListener(this);
        binding.sbSv.setOnSeekBarChangeListener(this);
        binding.BtnSvContinue.setOnClickListener(this);
        binding.BtnSvPause.setOnClickListener(this);
        binding.BtnSvPlay.setOnClickListener(this);

        uri = "android.resource://" + getPackageName() + "/" + R.raw.test;

        binding.vvPlayer.setVideoURI(Uri.parse(uri));

        holder = binding.svPlay.getHolder();
        holder.addCallback(this);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == binding.BtnVvPlay.getId()){
            binding.vvPlayer.seekTo(0);
            binding.vvPlayer.start();
        }else if(view.getId() == binding.BtnVvPause.getId()){
            binding.vvPlayer.pause();
        } else if (view.getId() == binding.BtnVvContinue.getId()) {
            binding.vvPlayer.start();
        }else if(view.getId() == binding.BtnSvPlay.getId()){
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        }else if(view.getId() == binding.BtnSvPause.getId()){
            mediaPlayer.pause();
        }else if(view.getId() == binding.BtnSvContinue.getId()){
            mediaPlayer.start();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Toast.makeText(this, "视频播放完毕", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );
            try {
                mediaPlayer.setDataSource(video.this, Uri.parse(uri));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    binding.BtnSvContinue.setVisibility(View.VISIBLE);
                    binding.BtnSvPause.setVisibility(View.VISIBLE);
                    binding.BtnSvPlay.setVisibility(View.VISIBLE);
                    addTimer();
                    countDownTimer = new CountDownTimer(3000, 1000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            binding.sbSv.setVisibility(View.INVISIBLE);
                        }
                    };
                    countDownTimer.start();
                }
            });
            mediaPlayer.prepareAsync();
        }
        //息屏后重新绑定
        mediaPlayer.setDisplay(surfaceHolder);

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        mediaPlayer.pause();
    }

    private void addTimer(){
        if(timer != null){
            return;
        }
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                binding.sbSv.setMax(mediaPlayer.getDuration());
                binding.sbSv.setProgress(mediaPlayer.getCurrentPosition());
            }
        };
        timer.schedule(task, 5, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(binding.vvPlayer.isPlaying()) {
            binding.vvPlayer.pause();
        }
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        if(mediaPlayer != null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
            mediaPlayer.release();
            mediaPlayer = null;
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
        if(seekBar.getId() == binding.sbSv.getId()){
             mediaPlayer.seekTo(seekBar.getProgress());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if(binding.sbSv.getVisibility() == View.INVISIBLE){
                binding.sbSv.setVisibility(View.VISIBLE);
                countDownTimer = new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        binding.sbSv.setVisibility(View.INVISIBLE);
                    }
                };
                countDownTimer.start();
            }else{
                countDownTimer.cancel();
                binding.sbSv.setVisibility(View.INVISIBLE);
            }
            return true;
        }
        return super.onTouchEvent(event);
    }
}