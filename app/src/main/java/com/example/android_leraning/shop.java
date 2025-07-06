package com.example.android_leraning;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android_leraning.databinding.ActivityShopBinding;
import com.example.android_leraning.databinding.ShopItemBinding;

public class shop extends AppCompatActivity {
    ActivityShopBinding binding;
    private String[] name = {"苹果", "雪梨", "香蕉", "菠萝", "西瓜", "冬瓜", "南瓜","北瓜"};
    private String[] price = {"100元/kg", "100元/kg","100元/kg","100元/kg","100元/kg","100元/kg","100元/kg","100元/kg"};
    private int[] pic = {
            R.drawable.ic_launcher_foreground,R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,R.drawable.ic_launcher_foreground
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MyBaseAdapter myBaseAdapter = new MyBaseAdapter();
        binding.listViewShop.setAdapter(myBaseAdapter);
    }

    class MyBaseAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return name.length;
        }

        @Override
        public Object getItem(int i) {
            return name[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ShopItemBinding binding;
            if(view == null){
                binding = ShopItemBinding.inflate(getLayoutInflater(), viewGroup, false);
                view = binding.getRoot();
                view.setTag(binding);
            }else{
                binding = (ShopItemBinding) view.getTag();
            }

            view.setOnClickListener(v -> {
                Log.d("mieye", "item:" + name[i]);
            });

            binding.txtShopName.setText(name[i]);
            binding.txtShopPrice.setText(price[i]);
            binding.ivShop.setBackgroundResource(pic[i]);
            return view;
        }
    }
}