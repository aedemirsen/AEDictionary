package com.example.aedamantium.aedictionary;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Aedamantium on 8/22/2015.
 */
public class KelimeEkle extends Activity {

    TextView tv1;
    EditText et1;
    EditText et2;
    TextView tv2;
    TextView tv3;
    ImageView iv1;
    ImageView iv2;
    KidDatabase kd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kelimeekle);
        kd = new KidDatabase(this);



        tv2 = (TextView)findViewById(R.id.textView4);
        Typeface font1 = Typeface.createFromAsset(getAssets(), "Leadcoat.ttf");
        tv2.setTypeface(font1);
        tv3 = (TextView)findViewById(R.id.textView5);
        Typeface font2 = Typeface.createFromAsset(getAssets(), "Leadcoat.ttf");
        tv3.setTypeface(font2);
        tv1 = (TextView)findViewById(R.id.textView3);
        if (Dil.dil.equals("Türkçe")){
            tv1.setText("Kelime Ekle");
            tv2.setText("Kelime : ");
            tv3.setText("Anlami : ");
        }else if (Dil.dil.equals("English")){
            tv1.setText("Add Word");
            tv2.setText("Word : ");
            tv3.setText("Meaning : ");
        }
        Typeface font3 = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
        tv1.setTypeface(font3);

        et1 = (EditText)findViewById(R.id.editText2);
        et1.setText(BabaSayfa.et1.getText());
        et2 = (EditText)findViewById(R.id.editText3);

        iv1 = (ImageView)findViewById(R.id.imageView2);
        if (Dil.dil.equals("Türkçe")){
            iv1.setImageResource(R.drawable.button);
        }else if(Dil.dil.equals("English")){
            iv1.setImageResource(R.drawable.add);
        }

        iv2 = (ImageView)findViewById(R.id.imageView5);

        if (Dil.dil.equals("Türkçe")){
            iv2.setImageResource(R.drawable.gerike);
        }else if(Dil.dil.equals("English")){
            iv2.setImageResource(R.drawable.backke);
        }

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KelimeEkle.this.finish();
            }
        });

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BabaSayfa.tMieMi.equals("t") || BabaSayfa.tMieMi.equals("tt")){
                    insertToDatabase("turkce","ingilizce",et1.getText().toString(),et2.getText().toString(),"tureng");
                }
                else if(BabaSayfa.tMieMi.equals("i") || BabaSayfa.tMieMi.equals("ee")){
                    insertToDatabase("english","turkish",et1.getText().toString(),et2.getText().toString(),"engtur");
                }
                if (Dil.dil.equals("Türkçe")){
                    Toast.makeText(getApplicationContext(),"Kelime kaydedildi.", Toast.LENGTH_SHORT).show();
                }
                else if (Dil.dil.equals("English")){
                    Toast.makeText(getApplicationContext(),"The word has been recorded.", Toast.LENGTH_SHORT).show();
                }
                KelimeEkle.this.finish();


            }
        });

    }



    public void insertToDatabase(String kolon1,String kolon2,String kelime,String karsiligi,String table){
            SQLiteDatabase db = kd.getWritableDatabase();
            ContentValues cv1 = new ContentValues();
            cv1.put(kolon1,kelime);
            cv1.put(kolon2,karsiligi);
            db.insertOrThrow(table,null,cv1);
    }



   /*public ArrayList<String> turengKelimeEkle(String girilen){
        ArrayList<String> kelimeler = new ArrayList<>();
        String dosya = "file/tureng.txt";
        String okunanKelime,karsiligi;
        String yeniSatir;
        InputStream is;
        OutputStream os;
        try {

            String s = getAssets()+"file/tureng.txt";
            is = getAssets().open(dosya);
            OutputStreamWriter osw = new OutputStreamWriter(os);
            InputStreamReader isr =  new InputStreamReader();
            BufferedWriter bf = new BufferedWriter(osw);
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
}
