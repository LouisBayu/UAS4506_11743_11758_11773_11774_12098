package com.udinus.uas4506_11743_11758_11773_11774_12098.View;

import android.os.Build;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.udinus.uas4506_11743_11758_11773_11774_12098.R;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter.SectionsPagerAdapter;

public class KategoriTab extends AppCompatActivity {
    TabLayout tabs;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori_tab);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }

        initComponent();
        selectPage(getIntent().getIntExtra("index",0));



    }

    private void initComponent(){
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    private void selectPage(int pageIndex){
        tabs.setScrollPosition(pageIndex,0f,true);
        viewPager.setCurrentItem(pageIndex);
    }

    public void backKategori(View view){
        finish();
    }
}