package com.example.android_leraning;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class myContentProvide extends ContentProvider {
    private  static  String AUTHORITY = "com.example.android_leraning";

    private  static final String DATABASE = "observer";
    private  static final String TABLE = "test";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DATA = "data";
    private static final String BASE_PATH = DATABASE +"/" + TABLE;
    public  static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + DATABASE + "/" + TABLE);
    private final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public final int CODE = 1;
    public final int CODE_ID = 2;
    myHelper mHelper;
    Context context;

    public myContentProvide(){
    }

    @Override
    public boolean onCreate() {
        this.context = getContext();
        mHelper = new myHelper(context);
        uriMatcher.addURI(AUTHORITY, DATABASE + "/" + TABLE, CODE);
        uriMatcher.addURI(AUTHORITY, DATABASE + "/" + TABLE + "/#", CODE_ID);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor;
        switch (uriMatcher.match(uri)){
            case CODE:
                cursor = db.query("test", strings, s, strings1, null, null, s1);
                break;
            case CODE_ID:
                String id = uri.getLastPathSegment();
                cursor = db.query("test", strings, "_id=?", new String[]{id}, null, null, s1);
                break;
            default:
                return null;
        }
        cursor.setNotificationUri(context.getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "";
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long id;
        switch (uriMatcher.match(uri)) {
            case CODE:
                id = db.insert("test", null, contentValues);
                break;
            default:
                return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case CODE:
                count = db.delete("test", s, strings);
                break;
            case CODE_ID:
                String id = uri.getLastPathSegment();
                count = db.delete("test", "_id = ?", new String[]{id});
                break;
            default:
                return 0;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case CODE:
                count = db.update("test", contentValues, s, strings);
                break;
            case CODE_ID:
                String id = uri.getLastPathSegment();
                count = db.update("test", contentValues, "_id = ?", new String[]{id});
                break;
            default:
                return 0;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
    class myHelper extends SQLiteOpenHelper {
        static final String DATABASE = "observer.db";
        static final int VERSION_CODE = 1;

        public myHelper(@Nullable Context context) {
            super(context, DATABASE, null, VERSION_CODE);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String exc = "CREATE TABLE IF NOT EXISTS test(_id INTEGER PRIMARY KEY AUTOINCREMENT, data VARCHAR(50))";
            sqLiteDatabase.execSQL(exc);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }

}

