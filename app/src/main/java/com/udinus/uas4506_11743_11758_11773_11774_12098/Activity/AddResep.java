package com.udinus.uas4506_11743_11758_11773_11774_12098.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

public class AddResep extends AppCompatActivity {

    ImageView addFoto;
    EditText langkahEditText, bahanEditText, namaResepEditText;
    int SELECT_IMAGE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resep);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.bg));
        }

        addFoto = findViewById(R.id.addImage);
        namaResepEditText = findViewById(R.id.namaResepEditText);
        langkahEditText = findViewById(R.id.langkahEditText);
        bahanEditText = findViewById(R.id.bahanEditText);


    }

    public void onClickBack(View view){
        Intent i = new Intent(this, Home.class);
        startActivity(i);
        finish();
    }


    public void onClickAddFoto(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Title"), SELECT_IMAGE_CODE);
    }

    public void onClickAddResep(View view){
        if (TextUtils.isEmpty(namaResepEditText.getText().toString())
            || TextUtils.isEmpty(langkahEditText.getText().toString())
            || TextUtils.isEmpty(bahanEditText.getText().toString())){
            Toast.makeText(view.getContext(), "Inputan Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(view.getContext(), "Resep Berhasil Dibagikan!", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(AddResep.this, Home.class);
                    startActivity(i);
                    finish();
                }
            }, 1500);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            Uri uri = data.getData();
            addFoto.setImageURI(uri);
            addFoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}