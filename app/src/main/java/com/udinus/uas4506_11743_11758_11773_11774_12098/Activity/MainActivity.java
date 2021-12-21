package com.udinus.uas4506_11743_11758_11773_11774_12098.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment.Add;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment.Home;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment.Profil;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment.Search;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment.Tersimpan;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.bg));
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        loadFragment(new Home());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()){
                    case (R.id.page_home):
                        fragment = new Home();
                        break;
                    case (R.id.page_search):
                        fragment = new Search();
                        break;
                    case (R.id.page_add):
                        fragment = new Add();
                        break;
                    case (R.id.page_saved):
                        fragment = new Tersimpan();
                        break;
                    case (R.id.page_profil):
                        fragment = new Profil();
                        break;
                }
                loadFragment(fragment);
                return true;
            }
        });

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()){
                    case (R.id.page_home):
                        fragment = new Home();
                        break;
                    case (R.id.page_search):
                        fragment = new Search();
                        break;
                    case (R.id.page_add):
                        fragment = new Add();
                        break;
                    case (R.id.page_saved):
                        fragment = new Tersimpan();
                        break;
                    case (R.id.page_profil):
                        fragment = new Profil();
                        break;
                }
                loadFragment(fragment);
            }
        });
    }

    private void loadFragment(Fragment fragment){
        // Load Fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}