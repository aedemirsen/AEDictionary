package com.example.aedamantium.aedictionary;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Aedamantium on 8/5/2015.
 */
public class Database extends SQLiteOpenHelper {

    public static final String databaseIsim = "KIDictionary";
    public static final int surum = 1;


    public Database(Context context){
        super(context,databaseIsim,null,surum);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tureng (turkce TEXT,ingilizce TEXT);");
        db.execSQL("create table engtur (english TEXT,turkish TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exist tureng");
        db.execSQL("drop table if exist engtur");
        onCreate(db);
    }








}
