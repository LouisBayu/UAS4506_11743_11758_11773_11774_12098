package com.udinus.uas4506_11743_11758_11773_11774_12098.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case (R.id.page_home):
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    case (R.id.page_search):
                        Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();
                    case (R.id.page_add):
                        Toast.makeText(MainActivity.this, "Bagikan Resep", Toast.LENGTH_SHORT).show();
                    case (R.id.page_saved):
                        Toast.makeText(MainActivity.this, "Tersimpan", Toast.LENGTH_SHORT).show();
                    case (R.id.page_profil):
                        Toast.makeText(MainActivity.this, "Profil", Toast.LENGTH_SHORT).show();

                }

                return true;
            }
        });

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case (R.id.page_home):
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    case (R.id.page_search):
                        Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();
                    case (R.id.page_add):
                        Toast.makeText(MainActivity.this, "Bagikan Resep", Toast.LENGTH_SHORT).show();
                    case (R.id.page_saved):
                        Toast.makeText(MainActivity.this, "Tersimpan", Toast.LENGTH_SHORT).show();
                    case (R.id.page_profil):
                        Toast.makeText(MainActivity.this, "Profil", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}