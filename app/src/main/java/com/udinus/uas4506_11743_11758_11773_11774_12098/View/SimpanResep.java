package com.udinus.uas4506_11743_11758_11773_11774_12098.View;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

public class SimpanResep extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpan_resep);
    }

    public void backTersimpan(View view) {
        finish();
    }

}
