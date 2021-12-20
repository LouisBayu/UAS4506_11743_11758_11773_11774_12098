package com.udinus.uas4506_11743_11758_11773_11774_12098.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

public class ForgotPassword extends AppCompatActivity {

    TextInputEditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.bg));
        }

        editTextEmail = findViewById(R.id.emailEditText);

    }

    public void onClickBack(View view) {
        Intent i = new Intent(ForgotPassword.this, WelcomebackLogin.class);
        startActivity(i);
        finish();
    }
    public void onClickSend(View view) {
        if (TextUtils.isEmpty(editTextEmail.getText().toString().trim())){
            Toast.makeText(view.getContext(),"Email Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
        }
        else if (!isValidEmail(editTextEmail.getText().toString().trim())){
            Toast.makeText(view.getContext(),"Email Tidak Valid!", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(ForgotPassword.this, ResetCode.class);
            startActivity(i);
            finish();
        }
    }

    public static boolean isValidEmail(CharSequence email){
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}