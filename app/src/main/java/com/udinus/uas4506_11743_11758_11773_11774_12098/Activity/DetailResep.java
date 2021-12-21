package com.udinus.uas4506_11743_11758_11773_11774_12098.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

public class DetailResep extends AppCompatActivity {


    TextView namaTV,authorTV,bahanTV,langkahTV;
    ImageView imageIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_resep);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.bg));
        }
        namaTV = findViewById(R.id.title);
        authorTV = findViewById(R.id.author);
        bahanTV = findViewById(R.id.bahanResep);
        langkahTV = findViewById(R.id.langkahResep);
        imageIV = findViewById(R.id.image);

        getIncomingIntent();
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("nama") && getIntent().hasExtra("author") && getIntent().hasExtra("langkah") && getIntent().hasExtra("bahan") && getIntent().hasExtra("image")){
            String nama = getIntent().getStringExtra("nama");
            String author = getIntent().getStringExtra("author");
            String[] bahan = getIntent().getStringArrayExtra("bahan");
            String[] langkah = getIntent().getStringArrayExtra("langkah");
            int image = getIntent().getIntExtra("image",0);

            setDataDetails(nama,author,bahan,langkah,image);
        }
    }

    private void setDataDetails(String nama, String author, String[] bahan, String[] langkah, int image){
        namaTV.setText(nama);
        authorTV.setText("From : " + author);
        bahanTV.setText(listBahantoString(bahan));
        langkahTV.setText(listLangkahtoString(langkah));
        imageIV.setImageResource(image);
    }

    private String listBahantoString(String[] data){
        String result = "";

        for (int i = 0; i < data.length ; i++){
            if (i < data.length-1){
                result += (data[i] + "\n\n");
            } else {
                result += data[i];
            }
        }

        return result;
    }
    private String listLangkahtoString(String[] data){
        String result = "";

        for (int i = 0; i < data.length ; i++){
            if (i < data.length-1){
                result += (data[i] + "\n\n\n");
            } else {
                result += data[i];
            }
        }

        return result;
    }

    public void onClickBack(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void onClickSave(View view){
        Toast.makeText(view.getContext(), "Resep Tersimpan!", Toast.LENGTH_SHORT).show();
    }



}