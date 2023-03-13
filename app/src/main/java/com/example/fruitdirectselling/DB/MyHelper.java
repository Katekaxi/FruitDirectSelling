package com.example.fruitdirectselling.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyHelper extends SQLiteOpenHelper {
    public MyHelper(@Nullable Context context) {
        //super参数（上下文、数据库名字(base)、游标工厂、版本号）
        super(context,"base.db",null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE member(NAME VARCHAR(20) NOT NULL PRIMARY KEY, PASSWORD VARCHAR(20) NOT NULL, SEX VARCHAR(6) NOT NULL, BIRTHDAY VARCHAR(20) NOT NULL, EMAIL VARCHAR(30) NOT NULL,PHONE VARCHAR(11) NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
