package com.example.android_leraning;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android_leraning.databinding.ActivitySvaeAcountBinding;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class save_acount extends AppCompatActivity implements View.OnClickListener {

    ActivitySvaeAcountBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySvaeAcountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            readData();
        } catch (IOException e) {
            Log.e("mieye",e.toString());
        }
        binding.BtnSave1.setOnClickListener(this);
        binding.BtnSave2.setOnClickListener(this);
        binding.BtnSave3.setOnClickListener(this);
    }

    @SuppressLint("Range")
    public void readData() throws IOException {
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        String account = sp.getString("account", null);
        String password = sp.getString("password", null);
        if(account != null && password != null && !account.isEmpty() && !password.isEmpty()){
            binding.editAccount2.setText(account);
            binding.editPassword2.setText(password);
        }

        FileInputStream fis = openFileInput("data1");
        byte[] content = new byte[fis.available()];
        fis.read(content);
        String user = new String(content);
        account = user.split(" ")[0];
        password = user.split(" ")[1];
        if(account != null && password != null && !account.isEmpty() && !password.isEmpty()){
            binding.editAccount1.setText(account);
            binding.editPassword1.setText(password);
        }

        MySqlHelper helper = new MySqlHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("user", null, null, null, null, null, null);
        if(cursor.getCount() != 0){
            while(cursor.moveToNext()){
                account = cursor.getString(cursor.getColumnIndex("account"));
                password = cursor.getString(cursor.getColumnIndex("password"));
                Log.d("mieye", account + ' ' + password);
            }
            if(account != null && password != null && !account.isEmpty() && !password.isEmpty()){
                binding.editAccount3.setText(account);
                binding.editPassword3.setText(password);
            }
        }
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == binding.BtnSave1.getId()){
            String account = binding.editAccount1.getText().toString().trim();
            String password = binding.editPassword1.getText().toString().trim();
            if(!account.isEmpty() && !password.isEmpty()){
                try {
                    FileOutputStream fos = openFileOutput("data1", MODE_PRIVATE);
                    fos.write(account.getBytes());
                    fos.write(" ".getBytes());
                    fos.write(password.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "账号密码为空", Toast.LENGTH_SHORT).show();
            }
        }else if(view.getId() == binding.BtnSave2.getId()) {
            String account = binding.editAccount2.getText().toString().trim();
            String password = binding.editPassword2.getText().toString().trim();
            if (!account.isEmpty() && !password.isEmpty()) {
                SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("account", account);
                editor.putString("password", password);
                editor.commit();
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "账号密码为空", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == binding.BtnSave3.getId()) {
            String account = binding.editAccount3.getText().toString().trim();
            String password = binding.editPassword3.getText().toString().trim();
            if (!account.isEmpty() && !password.isEmpty()) {
                MySqlHelper helper = new MySqlHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("account", account);
                values.put("password", password);
                db.insert("user", null, values);
                db.close();
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "账号密码为空", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class MySqlHelper extends SQLiteOpenHelper{

        private static final String DATABASE_NAME = "my_database.db";
        private static final int DATABASE_VERSION = 1;
        // 正确构造方法
        public MySqlHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            // 修正SQL语法（IF NOT EXISTS，PRIMARY KEY）
            String createTableSql = "CREATE TABLE IF NOT EXISTS user (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "account VARCHAR(50), " +
                    "password VARCHAR(50))";
            db.execSQL(createTableSql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}