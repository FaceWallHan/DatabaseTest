package com.example.databasetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private String CREATE_BOOK="create table Book(" +
            "id integer primary key autoincrement," +
            "author text," +
            "price real," +
            "pages integer," +
            "name text)";
    private String CREATE_CATEGORY="create table Category(" +
            "id integer primary key autoincrement," +
            "category_name text," +
            "category_code integer)";
    private Context context;
    public static final String DATABASE_NAME="BookStore.db";
    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BOOK);
        sqLiteDatabase.execSQL(CREATE_CATEGORY);
        //Toast.makeText(context, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists Book");
        sqLiteDatabase.execSQL("drop table if exists Category");
        onCreate(sqLiteDatabase);
        //通过卸载程序的方式来新增一张表毫无疑问是很极端的做法
    }
}
