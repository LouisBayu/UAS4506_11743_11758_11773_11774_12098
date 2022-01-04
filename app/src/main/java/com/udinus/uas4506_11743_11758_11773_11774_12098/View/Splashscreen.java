package com.udinus.uas4506_11743_11758_11773_11774_12098.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

public class Splashscreen extends AppCompatActivity {

    Animation titleAnim;
    ImageView appName;
    SharedPreferences sharedPreferences;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.bgSplash));
        }

        // Set & Binding Animation
        titleAnim = AnimationUtils.loadAnimation(this, R.anim.appname_anim);
        appName = findViewById(R.id.app_name);
        appName.setAnimation(titleAnim);
        sharedPreferences = getSharedPreferences("appSharedPref", Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                autoLogin();
            }
        }, 3000);
    }

    private void autoLogin()
    {
        String email = sharedPreferences.getString("key_email", null);
        String password = sharedPreferences.getString("key_current_user_password", null);
        System.out.println(email+password);
        boolean keepLogin = sharedPreferences.getBoolean("key_keep_login",false);

        if (keepLogin){
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Intent i = new Intent(Splashscreen.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(Splashscreen.this, OnBoarding.class);
                        startActivity(i);
                        finish();
                    }
                }
            });
        } else {
            Intent i = new Intent(Splashscreen.this, OnBoarding.class);
            startActivity(i);
            finish();
        }
    }
}
