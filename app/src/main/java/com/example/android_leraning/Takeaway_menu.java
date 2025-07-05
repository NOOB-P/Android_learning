package com.example.android_leraning;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.graphics.colorspace.Rgb;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_leraning.databinding.ActivityTakeawayMenuBinding;

import java.util.ArrayList;
import java.util.List;

public class Takeaway_menu extends AppCompatActivity {

    List<GoodsBean> list_goods1 = new ArrayList<>();
    List<GoodsBean> list_goods2 = new ArrayList<>();
    ActivityTakeawayMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTakeawayMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        clickEvent();
    }

    public void init(){
        list_goods1.add(new GoodsBean("苹果", "100元/kg", R.drawable.ic_launcher_foreground));
        list_goods1.add(new GoodsBean("雪梨", "10元/kg", R.drawable.ic_launcher_foreground));
        list_goods1.add(new GoodsBean("香蕉", "30元/kg", R.drawable.ic_launcher_foreground));
        list_goods1.add(new GoodsBean("苹果", "100元/kg", R.drawable.ic_launcher_foreground));
        list_goods1.add(new GoodsBean("雪梨", "10元/kg", R.drawable.ic_launcher_foreground));
        list_goods1.add(new GoodsBean("香蕉", "30元/kg", R.drawable.ic_launcher_foreground));

        list_goods2.add(new GoodsBean("菠萝", "60元/kg", R.drawable.ic_launcher_foreground));
        list_goods2.add(new GoodsBean("西瓜", "70元/kg", R.drawable.ic_launcher_foreground));
        list_goods2.add(new GoodsBean("冬瓜", "20元/kg", R.drawable.ic_launcher_foreground));
        list_goods2.add(new GoodsBean("菠萝", "60元/kg", R.drawable.ic_launcher_foreground));
        list_goods2.add(new GoodsBean("西瓜", "70元/kg", R.drawable.ic_launcher_foreground));
        list_goods2.add(new GoodsBean("冬瓜", "20元/kg", R.drawable.ic_launcher_foreground));
        list_goods2.add(new GoodsBean("菠萝", "60元/kg", R.drawable.ic_launcher_foreground));
        list_goods2.add(new GoodsBean("西瓜", "70元/kg", R.drawable.ic_launcher_foreground));
        list_goods2.add(new GoodsBean("冬瓜", "20元/kg", R.drawable.ic_launcher_foreground));
        list_goods2.add(new GoodsBean("菠萝", "60元/kg", R.drawable.ic_launcher_foreground));
        list_goods2.add(new GoodsBean("西瓜", "70元/kg", R.drawable.ic_launcher_foreground));
        list_goods2.add(new GoodsBean("冬瓜", "20元/kg", R.drawable.ic_launcher_foreground));
    }

    @SuppressLint("ResourceAsColor")
    public void clickEvent(){
        //由于主题原因，用color.xml中的标签会被主题颜色覆盖，懒得改了
        binding.tvOrder.setOnClickListener(view -> {
            switchFragment(list_goods1);
            binding.tvOrder.setTextColor(Color.rgb(0, 0,0));
            binding.tvRecommend.setTextColor(Color.rgb(0x63, 0x63,0x63));
        });
        binding.tvRecommend.setOnClickListener(view -> {
            switchFragment(list_goods2);
            binding.tvOrder.setTextColor(Color.rgb(0x63, 0x63,0x63));
            binding.tvRecommend.setTextColor(Color.rgb(0, 0,0));
        });
        switchFragment(list_goods1);
    }

    public void switchFragment(List<GoodsBean> list){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        right_fragment rightFragment = new right_fragment(list);
        fragmentTransaction.replace(binding.fragmentRight.getId(), rightFragment);
        fragmentTransaction.commit();
    }
}