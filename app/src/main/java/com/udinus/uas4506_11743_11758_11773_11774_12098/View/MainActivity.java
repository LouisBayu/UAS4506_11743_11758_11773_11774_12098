package com.udinus.uas4506_11743_11758_11773_11774_12098.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.FragmentMenu.Add;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.FragmentMenu.Home;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.FragmentMenu.Profil;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.FragmentMenu.Search;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.FragmentMenu.Tersimpan;
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
                switch (item.getItemId()){
                    default:

                }
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

    public void onClickAdd(View view){
        loadFragment(new Add());
        bottomNavigationView.setSelectedItemId(R.id.page_add);
    }

    public void katgrOlahanDaging(View view){
        Intent i = new Intent(MainActivity.this, KategoriTab.class);
        i.putExtra("index",0);
        startActivity(i);
    }

    public void katgrSayuran(View view){
        Intent i = new Intent(MainActivity.this, KategoriTab.class);
        i.putExtra("index",1);
        startActivity(i);
    }

    public void katgrSeafood(View view){
        Intent i = new Intent(MainActivity.this, KategoriTab.class);
        i.putExtra("index",2);
        startActivity(i);
    }

    public void katgrJajanan(View view){
        Intent i = new Intent(MainActivity.this, KategoriTab.class);
        i.putExtra("index",3);
        startActivity(i);
    }

    public void katgrKue(View view){
        Intent i = new Intent(MainActivity.this, KategoriTab.class);
        i.putExtra("index",4);
        startActivity(i);
    }

    public void katgrMinuman(View view){
        Intent i = new Intent(MainActivity.this, KategoriTab.class);
        i.putExtra("index",5);
        startActivity(i);
    }

}