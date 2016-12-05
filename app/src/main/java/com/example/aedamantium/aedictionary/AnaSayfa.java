package com.example.aedamantium.aedictionary;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.Statement;

public class AnaSayfa extends Activity {

    TextView tv1;
    Spinner s1;
    String [] array_spinner;
    ImageView iv1;
    private KidDatabase kd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);
        kd = new KidDatabase(this);
        try{
            String sutunlar[] = {"turkce","ingilizce"};
            if (!veritabanindaElemanVarMi(sutunlar)){
                veritabaniniDoldur();
            }
        }catch (Exception e){
            System.out.println(e.getClass().toString());
        }



        iv1 = (ImageView)findViewById(R.id.imageView);
        iv1.setImageResource(R.drawable.sozluk);
        tv1 = (TextView)findViewById(R.id.textView);
        if (Dil.dil.equals("")) {
            Dil.dil = "Türkçe";
            tv1.setText("Dil");
        }
        else if (Dil.dil.equals("English")){
            Dil.dil = "English";
            tv1.setText("Language");
        }
        Typeface font = Typeface.createFromAsset(getAssets(), "citycontrasts.ttf");
        tv1.setTypeface(font);
        s1 = (Spinner)findViewById(R.id.spinner);
        array_spinner=new String[2];
        array_spinner[0]="Türkçe";
        array_spinner[1]="English";

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, array_spinner);
        s1.setAdapter(adapter);

        if (Dil.dil.equals("Türkçe")) {
            tv1.setText("Dil");
            s1.setSelection(0);
            iv1.setImageResource(R.drawable.sozluk);
        } else if (Dil.dil.equals("English")) {
            tv1.setText("Language");
            s1.setSelection(1);
            iv1.setImageResource(R.drawable.dictionary);
        }



        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnaSayfa.this, BabaSayfa.class));
                AnaSayfa.this.finish();
            }
        });

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (s1.getSelectedItem().toString().equals("Türkçe")) {
                    tv1.setText("Dil");
                    Dil.dil = "Türkçe";
                    iv1.setImageResource(R.drawable.sozluk);
                } else if (s1.getSelectedItem().toString().equals("English")) {
                    tv1.setText("Language");
                    Dil.dil = "English";
                    iv1.setImageResource(R.drawable.dictionary);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ana_sayfa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }






    boolean veritabanindaElemanVarMi(String [] sutunlar){
        boolean var = false;
        Cursor okunanlar;
        SQLiteDatabase db = kd.getReadableDatabase();
        okunanlar = db.query("tureng", sutunlar, null, null, null, null, null);
        while(okunanlar.moveToNext()){
            return true;
        }
        return var;
    }




    void veritabaniniDoldur(){
        String dosya1 = "file/tureng.txt";
        String dosya2 = "file/engtur.txt";
        String okunanKelime,karsiligi;
        InputStream is1;
        InputStream is2;
        try {
            is1 = getAssets().open(dosya1);
            InputStreamReader isr1 =  new InputStreamReader(is1);
            BufferedReader bf1 = new BufferedReader(isr1);
            String s1 = bf1.readLine();
            while(s1 != null){
                okunanKelime =  s1.substring(0,s1.indexOf("/"));
                karsiligi = s1.substring(s1.indexOf("/")+1,s1.length());
                ekle(okunanKelime,karsiligi,"tureng","turkce","ingilizce");
                s1 = bf1.readLine();
            }
            bf1.close();
            isr1.close();
            is1.close();

            is2 = getAssets().open(dosya2);
            InputStreamReader isr2 =  new InputStreamReader(is2);
            BufferedReader bf2 = new BufferedReader(isr2);
            String s2 = bf2.readLine();
            while(s2 != null){
                okunanKelime =  s2.substring(0,s2.indexOf("/"));
                karsiligi = s2.substring(s2.indexOf("/")+1,s2.length());
                ekle(okunanKelime,karsiligi,"engtur","english","turkish");
                s2 = bf2.readLine();
            }
            bf2.close();
            isr2.close();
            is2.close();
            kd.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    void ekle(String word1,String word2,String table,String kolon1,String kolon2){
        SQLiteDatabase db = kd.getWritableDatabase();
        ContentValues cv1 = new ContentValues();
        cv1.put(kolon1,word1);
        cv1.put(kolon2,word2);
        db.insertOrThrow(table,null,cv1);
    }



}
