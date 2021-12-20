package com.udinus.uas4506_11743_11758_11773_11774_12098.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

public class ResetCode extends AppCompatActivity {

    PinView pinView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_code);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.bg));
        }

        pinView = findViewById(R.id.pinView);
    }

    public void onClickVerify(View view){
        String code = pinView.getText().toString();
        // Code Validation
        if (code.length() == 0){
            Toast.makeText(view.getContext(),"Inputan Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
        } else if (code.length() < 6){
            Toast.makeText(view.getContext(),"Kode Tidak Valid!", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(ResetCode.this, ResetPassword.class);
            startActivity(i);
            finish();
        }
    }

    public void onClickResend(View view){
        Toast.makeText(view.getContext(),"Kode Telah Dikirim Ulang", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(ResetCode.this, ResetCode.class);
                startActivity(i);
                finish();
            }
        }, 1500);
    }
}