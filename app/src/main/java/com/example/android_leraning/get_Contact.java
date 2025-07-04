
package com.example.android_leraning;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_leraning.databinding.ActivityGetContactBinding;
import com.example.android_leraning.databinding.ShopItemBinding;

import java.util.ArrayList;
import java.util.List;

public class get_Contact extends AppCompatActivity {

    ActivityGetContactBinding binding;
    List<GoodsBean> list = new ArrayList<>();
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //获取通讯录权限
        String[] permission = {
                "android.permission.READ_CONTACTS",
                "android.permission.WRITE_CONTACTS"
        };
        ArrayList<String> permission_list= new ArrayList<>();
        for(int i = 0; i < permission.length; i++){
            if(checkSelfPermission(permission[i]) != PackageManager.PERMISSION_GRANTED){
                permission_list.add(permission[i]);
            }
        }
        if(!permission_list.isEmpty()){
            requestPermissions(permission_list.toArray(new String[permission_list.size()]), 1);
        }else{
            SetData();
        }
        adapter = new MyAdapter(list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.rvGetContact.setLayoutManager(new LinearLayoutManager(this));
        binding.rvGetContact.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
    }

    @SuppressLint("Range")
    public void SetData(){
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            int ishas = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            String phone = null;
            Cursor cursor_phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if(ishas > 0){
                while(cursor_phone.moveToNext()){
                    phone = cursor_phone.getString(cursor_phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
            }
            cursor_phone.close();
            list.add(new GoodsBean(name, phone, R.drawable.ic_launcher_foreground));
        }
        cursor.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            boolean granted = true;
            for (int grantResult : grantResults) {
                granted = granted && (grantResult == PackageManager.PERMISSION_GRANTED);
            }
            if (granted) {
                SetData();
            } else {
                Toast.makeText(this, "通讯录权限获取失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder>{
        //利用之前结构体，节省时间
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
            holder.binding.ivShop.setBackgroundResource(list.get(position).pic);
            holder.binding.txtShopName.setText(list.get(position).name);
            holder.binding.txtShopPrice.setText(list.get(position).price);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder{
            ShopItemBinding binding;
            public MyHolder(@NonNull ShopItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}