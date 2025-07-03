package com.example.android_leraning;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_leraning.databinding.RightLayoutBinding;
import com.example.android_leraning.databinding.ShopItemBinding;

import java.util.List;

public class right_fragment extends Fragment {


    RightLayoutBinding binding;
    List<GoodsBean> list;
    public right_fragment(List<GoodsBean> list){
        this.list = list;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RightLayoutBinding.inflate(getLayoutInflater());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MyAdapter adapter = new MyAdapter(list);
        binding.rvGoodsList.setLayoutManager(new LinearLayoutManager(container.getContext()));
        binding.rvGoodsList.setAdapter(adapter);
        return binding.getRoot();
    }

    static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder>{
        List<GoodsBean> list;
        public MyAdapter(List<GoodsBean> list){
            this.list = list;
        }

        @NonNull
        @Override
        public MyAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ShopItemBinding binding = ShopItemBinding.inflate(inflater, parent, false);
            return new MyHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyHolder holder, int position) {
            holder.binding.txtShopName.setText(list.get(position).name);
            holder.binding.txtShopPrice.setText(list.get(position).price);
            holder.binding.ivShop.setBackgroundResource(list.get(position).pic);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public static class MyHolder extends RecyclerView.ViewHolder{
            ShopItemBinding binding;
            public MyHolder(@NonNull ShopItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
