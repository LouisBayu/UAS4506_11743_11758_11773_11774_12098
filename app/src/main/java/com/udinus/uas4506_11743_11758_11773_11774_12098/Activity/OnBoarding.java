package com.udinus.uas4506_11743_11758_11773_11774_12098.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter.OnBoardingAdapter;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.OnBoardingItem;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import java.util.ArrayList;
import java.util.List;

public class OnBoarding extends AppCompatActivity {

    private OnBoardingAdapter onBoardingAdapter;
    private LinearLayout dotsIndicator;
    private Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.bg));
        }
        initComponent();

    }

    private void initComponent(){
        dotsIndicator = findViewById(R.id.dots);
        buttonNext = findViewById(R.id.buttonNext);


        setupOnBoardingItems();

        ViewPager2 onBoardingViewPager = findViewById(R.id.viewPagerOnBoard);
        onBoardingViewPager.setAdapter(onBoardingAdapter);

        setupDotsIndicator();
        setCurrentDots(0);

        onBoardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentDots(position);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onBoardingViewPager.getCurrentItem() + 1 < onBoardingAdapter.getItemCount()){
                    onBoardingViewPager.setCurrentItem(onBoardingViewPager.getCurrentItem()+1);
                } else {
                    startActivity(new Intent(getApplicationContext(),WelcomebackLogin.class));
                    finish();
                }
            }
        });
    }

    private void setupOnBoardingItems(){
        List<OnBoardingItem> onBoardingItemList = new ArrayList<>();

        OnBoardingItem slide1 = new OnBoardingItem();
        slide1.setTitle(getResources().getString(R.string.first_slide_title));
        slide1.setDesc(getResources().getString(R.string.first_slide_desc));
        slide1.setImage(R.drawable.onboard_slide1);
        OnBoardingItem slide2 = new OnBoardingItem();
        slide2.setTitle(getResources().getString(R.string.second_slide_title));
        slide2.setDesc(getResources().getString(R.string.second_slide_desc));
        slide2.setImage(R.drawable.onboard_slide2);
        OnBoardingItem slide3 = new OnBoardingItem();
        slide3.setTitle(getResources().getString(R.string.third_slide_title));
        slide3.setDesc(getResources().getString(R.string.third_slide_desc));
        slide3.setImage(R.drawable.onboard_slide3);

        onBoardingItemList.add(slide1);
        onBoardingItemList.add(slide2);
        onBoardingItemList.add(slide3);

        onBoardingAdapter = new OnBoardingAdapter(onBoardingItemList);
    }

    public void onClickSkipOnboard(View view) {
        Intent i = new Intent(OnBoarding.this, WelcomebackLogin.class);
        startActivity(i);
        finish();
    }

    private void setupDotsIndicator(){
        ImageView[] indicators = new ImageView[onBoardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for (int i=0; i<indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            dotsIndicator.addView(indicators[i]);
        }
    }

    private void setCurrentDots(int index){
        int childCount = dotsIndicator.getChildCount();
        for (int i=0; i < childCount; i++){
            ImageView imageView = (ImageView) dotsIndicator.getChildAt(i);
            if (index == i){
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_active));
            }else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_inactive));
            }
        }
        if (index == onBoardingAdapter.getItemCount() - 1){
            buttonNext.setText("Get Started");

        } else{
            buttonNext.setText("Next");
        }
    }

}
