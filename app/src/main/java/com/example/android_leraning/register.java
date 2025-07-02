package com.example.android_leraning;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_leraning.databinding.ActivityRegisterBinding;

public class register extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {

    ActivityRegisterBinding binding;
    String output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.BtnRegister1.setOnClickListener(this);
        binding.radioGroupSex.setOnCheckedChangeListener(this);
        binding.checkBasketball.setOnCheckedChangeListener(this);
        binding.checkJump.setOnCheckedChangeListener(this);
        binding.checkRap.setOnCheckedChangeListener(this);
        binding.checkSing.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.Btn_register1){
            if(binding.editPassword.getText().toString().isEmpty() || binding.editUserName.getText().toString().isEmpty()){
                output = "用户名密码为空";
            }else{
                output = "用户名：" + binding.editUserName.getText().toString().trim() + "\n";
                output += "密码： " + binding.editPassword.getText().toString().trim();
            }
            Toast.makeText(this, output, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if(radioGroup.getId() == R.id.radio_group_sex){
            if(i == R.id.radio_man){
                Log.d("mieye", "radio:男");
            }else if(i == R.id.radio_woman){
                Log.d("mieye", "radio:女");
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(compoundButton.getId() == R.id.check_basketball){
            Log.d("mieye", "checkbox:篮球");
        }
        else if(compoundButton.getId() == R.id.check_rap){
            Log.d("mieye", "checkbox:rap");
        }
        else if(compoundButton.getId() == R.id.check_sing){
            Log.d("mieye", "checkbox:唱");
        }
        else if(compoundButton.getId() == R.id.check_jump){
            Log.d("mieye", "checkbox:跳");
        }

    }
}