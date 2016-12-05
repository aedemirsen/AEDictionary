package com.example.aedamantium.aedictionary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aed on 16.10.2015.
 */
public class KidDatabase extends SQLiteOpenHelper {

    public static final String databaseName = "kelime";
    public static final int version = 1;


    public KidDatabase(Context context) {
        super(context, databaseName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE engtur (english TEXT,turkish TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE tureng (turkce TEXT,ingilizce TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST sozluk");
        onCreate(sqLiteDatabase);
    }
}
