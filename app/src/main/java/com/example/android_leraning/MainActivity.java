package com.example.android_leraning;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

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
        binding.BtnTakeawayMenu.setOnClickListener(this);
        binding.BtnAccountSave.setOnClickListener(this);
        binding.BtnCantactQuery.setOnClickListener(this);
        binding.BtnDataObserver.setOnClickListener(this);
        binding.BtnServer.setOnClickListener(this);
        binding.BtnBroadcast.setOnClickListener(this);
        binding.BtnServer.setOnClickListener(this);
        binding.BtnWebView.setOnClickListener(this);
        binding.BtnVideoPlayer.setOnClickListener(this);
        binding.BtnTcpSocketClient.setOnClickListener(this);
        binding.BtnTcpSocketServer.setOnClickListener(this);
        binding.BtnUdpSocket.setOnClickListener(this);
        binding.BtnHttp.setOnClickListener(this);
        binding.BtnBluetooth.setOnClickListener(this);
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
            Intent intent = new Intent(this, News.class);
            startActivity(intent);
        }else if(view.getId() == R.id.Btn_takeaway_menu){
            Intent intent = new Intent(this, Takeaway_menu.class);
            startActivity(intent);
        }else if(view.getId() == R.id.Btn_account_save){
            Intent intent = new Intent(this, save_acount.class);
            startActivity(intent);
        }else if(view.getId() == R.id.Btn_cantact_query){
            Intent intent = new Intent(this, get_Contact.class);
            startActivity(intent);
        }else if(view.getId() == R.id.Btn_data_observer){
            Intent intent = new Intent(this, data_observer.class);
            startActivity(intent);
        }else if(view.getId() == R.id.Btn_broadcast){
            Intent intent = new Intent(this, broadcast.class);
            startActivity(intent);
        }else if(view.getId() == R.id.Btn_server){
            Intent intent = new Intent(this, musicplayer.class);
            startActivity(intent);
        }else if(view.getId() == R.id.Btn_video_player){
            Intent intent = new Intent(this, video.class);
            startActivity(intent);
        }else if(view.getId() == R.id.Btn_tcp_socket_client) {
            Intent intent = new Intent(this, tcp_socket.class);
            startActivity(intent);
        }else if (view.getId() == R.id.Btn_tcp_socket_server) {
            Intent intent = new Intent(this, tcp_socket_server.class);
            startActivity(intent);
        }else if(view.getId() == R.id.Btn_udp_socket){

        }else if(view.getId() == R.id.Btn_http){

        }else if(view.getId() == R.id.Btn_bluetooth){

        }else if(view.getId() == R.id.Btn_web_view){

        }
    }
}