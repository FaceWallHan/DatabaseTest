package com.example.databasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MyDatabaseHelper dbHelper;
    private Button create_database,add_data,update_data,delete_data,query_data;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inView();
        setListener();
    }
    private void inView(){
        query_data=findViewById(R.id.query_data);
        delete_data=findViewById(R.id.delete_data);
        update_data=findViewById(R.id.update_data);
         create_database=findViewById(R.id.create_database);
         add_data=findViewById(R.id.add_data);
        dbHelper=new MyDatabaseHelper(this,MyDatabaseHelper.DATABASE_NAME,null,2);
        db=dbHelper.getWritableDatabase();
    }
    private void setListener(){
        create_database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.getWritableDatabase();
            }
        });
        delete_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.delete("Book","pages>?",new String[]{"500"});
            }
        });
        update_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values=new ContentValues();
                values.put("price",10.99);
                db.update("Book",values,"name=?",new String[]{"the Da Vince code"});
                Toast.makeText(MainActivity.this, "update succeeded", Toast.LENGTH_SHORT).show();
            }
        });
        query_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor=db.query("Book",null,null,null,null,null,null);
                if (cursor.moveToFirst()){
                    while (cursor.moveToNext()){
                        String name=cursor.getColumnName(cursor.getColumnIndex("name"));
                        String pages=cursor.getColumnName(cursor.getColumnIndex("pages"));
                        String price=cursor.getColumnName(cursor.getColumnIndex("price"));
                        String author=cursor.getColumnName(cursor.getColumnIndex("author"));
                        Log.d(TAG, "author:--"+author+"price:--"+price+"pages:--"+pages+"name:--"+name);
                    }
                }
                cursor.close();
            }
        });
        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values=new ContentValues();
                //开始组装第一条数据
                values.put("name","the Da Vince code");
                values.put("pages",454);
                values.put("price",16.96);
                values.put("author","Dan Brown");
                db.insert("Book",null,values);//插入第一条数据
                values.clear();
                //开始组装第2条数据
                values.put("name","the Lost Symbol");
                values.put("pages",510);
                values.put("price",19.95);
                values.put("author","Dan Brown");
                db.insert("Book",null,values);//插入第2条数据

            }
        });
    }
}