package com.example.aedamantium.aedictionary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Aedamantium on 8/4/2015.
 */
public class BabaSayfa extends Activity {


    static EditText et1 ;
    ListView lv1;
    TextView tv1;
    ImageView iv1;
    ImageView iv2;
    static String tMieMi;
    Button b1;
    ArrayAdapter<String> adapter;
    static String kelime;
    KidDatabase kd;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.babasayfa);



        et1 = (EditText)findViewById(R.id.editText);
        lv1 = (ListView)findViewById(R.id.listView);
        tv1 = (TextView) findViewById(R.id.textView2);
        iv1 = (ImageView)findViewById(R.id.imageView3);
        iv2 = (ImageView)findViewById(R.id.imageView4);
        b1 = (Button)findViewById(R.id.button);
        kd = new KidDatabase(this);

        if (Dil.dil.equals("Türkçe")){
            iv1.setImageResource(R.drawable.geri);
            tv1.setText("Kelimeyi gir :)");
            b1.setText("ARA");
            iv2.setImageResource(R.drawable.i);
            tMieMi = "i";
        }
        else if (Dil.dil.equals("English")){
            iv1.setImageResource(R.drawable.back);
            tv1.setText("Enter the word :)");
            b1.setText("SEARCH");
            iv2.setImageResource(R.drawable.ee);
            tMieMi = "ee";
        }

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BabaSayfa.this, AnaSayfa.class));
                BabaSayfa.this.finish();
            }
        });

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tMieMi.equals("i")) {
                    iv2.setImageResource(R.drawable.t);
                    tMieMi = "t";
                } else if (tMieMi.equals("ee")) {
                    iv2.setImageResource(R.drawable.tt);
                    tMieMi = "tt";
                } else if (tMieMi.equals("t")) {
                    iv2.setImageResource(R.drawable.i);
                    tMieMi = "i";
                } else if (tMieMi.equals("tt")) {
                    iv2.setImageResource(R.drawable.ee);
                    tMieMi = "ee";
                }
            }
        });

        Typeface font = Typeface.createFromAsset(getAssets(), "ASMAN.TTF");
        tv1.setTypeface(font);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1);
        b1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                    if (!et1.getText().toString().equals("")) {
                         adapter.clear();
                    String girilen = et1.getText().toString();
                    if (tMieMi.equals("i") || tMieMi.equals("ee")) {
                        ingilizcedenTurk(girilen,adapter);
                    }
                    else if (tMieMi.equals("t") || tMieMi.equals("tt")) {
                        turkcedenIng(girilen,adapter);
                    }
                }
            }
        });
    }

    public String dialog(String baslik,String mesaj,String buttonText1,String buttonText2){
        String kelime = et1.getText().toString();
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setTitle(baslik);
        d.setMessage(mesaj);
        d.setPositiveButton(buttonText1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(BabaSayfa.this, KelimeEkle.class));
            }
        })
        .setNegativeButton(buttonText2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = d.create();
        alertDialog.show();
        return kelime;
    }

    public void turkcedenIng(String girilen, ArrayAdapter<String> adapter){
        String sutunlar[] = {"turkce","ingilizce"};
        SQLiteDatabase db = kd.getReadableDatabase();
        boolean kontrol = true;
        Cursor okunanlar = db.query("tureng", sutunlar, null, null, null, null, null);
        while(okunanlar.moveToNext()){
            String kelime = okunanlar.getString(okunanlar.getColumnIndex("turkce"));
            String karsilik = okunanlar.getString(okunanlar.getColumnIndex("ingilizce"));
            Pattern p = Pattern.compile("\\b" + girilen + "\\b");
            Matcher m = p.matcher(kelime);
            if (m.find()){
                kontrol = false;
                adapter.add(kelime.concat(": ").concat(karsilik));
            }
        }
        lv1.setAdapter(adapter);
        if (kontrol){
            if (Dil.dil.equals("Türkçe")){
                kelime = dialog("EKLE","Bu kelime sözlükte bulunamadı. Eklemek ister misin?","Evet","Hayır");
            }
            else if (Dil.dil.equals("English")){
                kelime = dialog("ADD","This word doesn't exist in dictionary. Would you like to add?","Yes","No");
            }
        }

    }

    public void ingilizcedenTurk(String girilen, ArrayAdapter<String> adapter){
        String sutunlar[] = {"english","turkish"};
        SQLiteDatabase db = kd.getReadableDatabase();
        boolean kontrol = true;
        Cursor okunanlar = db.query("engtur",sutunlar,null,null,null,null,null);
        while(okunanlar.moveToNext()){
            String kelime = okunanlar.getString(okunanlar.getColumnIndex("english"));
            String karsilik = okunanlar.getString(okunanlar.getColumnIndex("turkish"));
            Pattern p = Pattern.compile("\\b" + girilen + "\\b");
            Matcher m = p.matcher(kelime);
            if (m.find()){
                kontrol = false;
                adapter.add(kelime.concat(": ").concat(karsilik));
            }
        }
        lv1.setAdapter(adapter);
        if (kontrol){
            if (Dil.dil.equals("Türkçe")){
                kelime = dialog("EKLE","Bu kelime sözlükte bulunamadı. Eklemek ister misin?","Evet","Hayır");
            }
            else if (Dil.dil.equals("English")){
                kelime = dialog("ADD","This word doesn't exist in dictionary. Would you like to add?","Yes","No");
            }
        }

    }

   /* public ArrayList<String> turkcedenIng(String girilen){
        ArrayList<String> kelimeler = new ArrayList<>();
        String dosya = "file/tureng.txt";
        String okunanKelime,karsiligi;
        InputStream is;
        try {
            is = getAssets().open(dosya);
            InputStreamReader isr =  new InputStreamReader(is);
            BufferedReader bf = new BufferedReader(isr);
            String s = bf.readLine();
            while(s != null){
                okunanKelime =  s.substring(0,s.indexOf("/"));
                karsiligi = s.substring(s.indexOf("/")+1,s.length());
                Pattern p = Pattern.compile("\\b"+girilen+"\\b");
                Matcher m = p.matcher(okunanKelime);
                if (m.find()){
                    kelimeler.add(okunanKelime.concat(": ").concat(karsiligi));
                }
                s = bf.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return kelimeler;
    }

    public ArrayList<String> ingilizcedenTurk(String girilen){
        ArrayList<String> kelimeler = new ArrayList<>();
        String dosya = "file/engtur.txt";
        String okunanKelime,karsiligi;
        InputStream is;
        try {
            is = getAssets().open(dosya);
            InputStreamReader isr =  new InputStreamReader(is);
            BufferedReader bf = new BufferedReader(isr);
            String s = bf.readLine();
            while(s != null){
                okunanKelime =  s.substring(0,s.indexOf("/"));
                karsiligi = s.substring(s.indexOf("/")+1,s.length());
                Pattern p = Pattern.compile("\\b"+girilen+"\\b");
                Matcher m = p.matcher(okunanKelime);
                if (m.find()){
                    kelimeler.add(okunanKelime.concat(": ").concat(karsiligi));
                }
                s = bf.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return kelimeler;
    }*/



















    /*public ArrayList<String> turkcedenIngilizceye(String girilen){
        ArrayList<String> kelimeler = new ArrayList<>();
        String columns[] = {"turkce","ingilizce"};
        SQLiteDatabase sdb = database.getReadableDatabase();
        Cursor okunanlar = sdb.query("tureng", columns, null, null, null, null, null);
        while (okunanlar.moveToNext()){
            String okunanKelime = okunanlar.getString(okunanlar.getColumnIndex("turkce"));
            String karsiligi = okunanlar.getString(okunanlar.getColumnIndex("ingilizce"));
            Pattern p = Pattern.compile("\\b"+girilen+"\\b");
            Matcher m = p.matcher(okunanKelime);
            if (m.find()){
                kelimeler.add(okunanKelime.concat(": ").concat(karsiligi));
            }
        }
        return kelimeler;
    }*/

   /* public ArrayList<String> ingilizcedenTurkceye(String girilen){
        ArrayList<String> kelimeler = new ArrayList<>();
        String columns[] = {"english","turkish"};
        SQLiteDatabase sdb = database.getReadableDatabase();
        Cursor okunanlar = sdb.query("engtur", columns, null, null, null, null, null);
        int a = okunanlar.getCount();

        while (okunanlar.moveToNext()){
            String okunanKelime = okunanlar.getString(okunanlar.getColumnIndex("english"));
            String karsiligi = okunanlar.getString(okunanlar.getColumnIndex("turkish"));
            Pattern p = Pattern.compile("\\b"+girilen+"\\b");
            Matcher m = p.matcher(okunanKelime);
            if (m.find()){
                kelimeler.add(okunanKelime.concat(": ").concat(karsiligi));
            }
        }
        return kelimeler;
    }*/







}
