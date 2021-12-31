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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.UserModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

public class Register extends AppCompatActivity {

    TextInputEditText fullnameEditText;
    TextInputEditText usernameEditText;
    TextInputEditText emailEditText;
    TextInputEditText phoneEditText;
    TextInputEditText passwordEditText;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.bg));
        }

        fullnameEditText = findViewById(R.id.fullnameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        sharedPreferences = getSharedPreferences("appSharedPref", Context.MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();

    }

    private void saveUsernameToSP(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key_username",usernameEditText.getText().toString().trim());
        editor.apply();
    }

    public void onClickRegister(View view){
        rootNode = FirebaseDatabase.getInstance();
        reference =rootNode.getReference("users");
        mAuth = FirebaseAuth.getInstance();
        if (TextUtils.isEmpty(emailEditText.getText().toString().trim())
                || TextUtils.isEmpty(passwordEditText.getText().toString().trim())
                || TextUtils.isEmpty(usernameEditText.getText().toString().trim())
                || TextUtils.isEmpty(fullnameEditText.getText().toString().trim())){
            Toast.makeText(view.getContext(), "Inputan Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
        }
        // Validasi Email
        else if (!isValidEmail(emailEditText.getText().toString().trim())){
            Toast.makeText(view.getContext(), "Email Tidak Valid!", Toast.LENGTH_SHORT).show();
        } else {
            String fullname = fullnameEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            UserModel helperClass = new UserModel(fullname, username, email, phone, password);

            reference.child(username).setValue(helperClass);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Intent i = new Intent(Register.this, RegisterSuccess.class);
                        saveUsernameToSP();
                        startActivity(i);
                        finish();
                    }else {
                        Toast.makeText(Register.this, "Registrasi Gagal : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    public void onClickLogin(View view){
        Intent i = new Intent(Register.this, WelcomebackLogin.class);
        startActivity(i);
    }

    public static boolean isValidEmail(CharSequence email){
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }


}