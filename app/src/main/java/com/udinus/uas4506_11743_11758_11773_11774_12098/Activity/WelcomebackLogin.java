package com.udinus.uas4506_11743_11758_11773_11774_12098.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.UserModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;
import java.lang.ref.Reference;

public class WelcomebackLogin extends AppCompatActivity {
    TextInputEditText emailEditText;
    TextInputEditText passwordEditText;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseAuth auth;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomeback_login);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.bgSplash));
        }

        // Binding Edit Text
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        sharedPreferences = getSharedPreferences("appSharedPref", Context.MODE_PRIVATE);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null){
            Intent i = new Intent(WelcomebackLogin.this, LoginSuccess.class);
            startActivity(i);
            finish();
        }

    }

    private void saveEmailToSP(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key_email",emailEditText.getText().toString().trim());
        editor.apply();
    }

    public void onClickLogin(View view){
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        mAuth = FirebaseAuth.getInstance();
//        Validasi Inputan Kosong
        if (TextUtils.isEmpty(emailEditText.getText().toString().trim())
            && TextUtils.isEmpty(passwordEditText.getText().toString().trim())){
            Toast.makeText(view.getContext(), "Inputan Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(emailEditText.getText().toString().trim())){
            Toast.makeText(view.getContext(), "Email Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(passwordEditText.getText().toString().trim())){
            Toast.makeText(view.getContext(), "Password Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
        }
        // Validasi Email
        else if (!isValidEmail(emailEditText.getText().toString().trim())){
            Toast.makeText(view.getContext(), "Email Tidak Valid!", Toast.LENGTH_SHORT).show();
        }else {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Intent i = new Intent(WelcomebackLogin.this, LoginSuccess.class);
                        saveEmailToSP();
                        startActivity(i);
                        finish();
                    }else {
                        Toast.makeText(WelcomebackLogin.this, "Log in Error : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public static boolean isValidEmail(CharSequence email){
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public void onClickForgotPassword(View view){
        Intent i = new Intent(WelcomebackLogin.this, ForgotPassword.class);
        startActivity(i);
    }

    public void onClickDaftar(View view){
        Intent i = new Intent(WelcomebackLogin.this, Register.class);
        startActivity(i);
    }

}