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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.PhoneAuthProvider;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;
import com.udinus.uas4506_11743_11758_11773_11774_12098.databinding.ActivityMainBinding;

public class ForgotPassword extends AppCompatActivity {

    public TextInputEditText phoneEditText;

    public ActivityMainBinding binding;

    public PhoneAuthProvider.ForceResendingToken forceResendingToken;
    public PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    public String mVerificationId;

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

        phoneEditText = findViewById(R.id.phoneEditText);

    }

    public void onClickBack(View view) {
        Intent i = new Intent(ForgotPassword.this, WelcomebackLogin.class);
        startActivity(i);
        finish();
    }

    public void onClickSend(View view) {
        if (TextUtils.isEmpty(phoneEditText.getText().toString().trim())){
            Toast.makeText(view.getContext(),"No Hp Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent i = new Intent(ForgotPassword.this, ResetCode.class);
            startActivity(i);
            finish();
        }
    }
//
//    public void verifyPhoneNumber(View view) {
//        CheckInternet
//    }

}