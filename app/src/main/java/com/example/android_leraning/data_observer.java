package com.example.android_leraning;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_leraning.databinding.ActivityDataObserverBinding;

import java.net.URI;

public class data_observer extends AppCompatActivity implements View.OnClickListener {
    ActivityDataObserverBinding binding;
    Handler mhandler;
    Myobserver myobserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDataObserverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.BtnAdd.setOnClickListener(this);
        binding.BtnDelete.setOnClickListener(this);
        binding.BtnChange.setOnClickListener(this);
        binding.BtnQuery.setOnClickListener(this);
        mhandler=new Handler(Looper.getMainLooper());

        myobserver = new Myobserver(mhandler);
        Uri uri = Uri.parse(myContentProvide.CONTENT_URI.toString());
        getContentResolver().registerContentObserver(uri, true, myobserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(myobserver);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == binding.BtnAdd.getId()){
            ContentValues values = new ContentValues();
            values.put("data", "test");
            getContentResolver().insert(myContentProvide.CONTENT_URI, values);
        } else if (view.getId() == binding.BtnDelete.getId()) {
            getContentResolver().delete(myContentProvide.CONTENT_URI, "data=?", new String[]{"test"});
        } else if (view.getId() == binding.BtnChange.getId()) {
            ContentValues values = new ContentValues();
            values.put("data", "test1");
            getContentResolver().update(myContentProvide.CONTENT_URI, values, "data=?", new String[]{"test"});
        } else if (view.getId() == binding.BtnQuery.getId()) {
            getContentResolver().query(myContentProvide.CONTENT_URI, null, null, null, null);
        }
    }

    class Myobserver extends ContentObserver{

        public Myobserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.d("mieye", "检测到数据变动");
            mhandler.post(()->{
                Toast.makeText(data_observer.this, "检测到数据变动", LENGTH_SHORT).show();
            });
        }
    }
}

