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
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.checkbox.MaterialCheckBox;
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
import com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment.Home;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.UserModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;
import java.lang.ref.Reference;

public class WelcomebackLogin extends AppCompatActivity {
    TextInputEditText emailEditText;
    TextInputEditText passwordEditText;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    SharedPreferences sharedPreferences;
    MaterialCheckBox chkRememberUsername;
    MaterialCheckBox chkKeepLogin;

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

        initComponent();
        loadSavedEmailForLogin();


    }

    private void initComponent(){
        // Binding Komponen
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        chkKeepLogin = findViewById(R.id.chk_keep_login);
        chkRememberUsername = findViewById(R.id.chk_remember_username);

        sharedPreferences = getSharedPreferences("appSharedPref", Context.MODE_PRIVATE);


        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        mAuth = FirebaseAuth.getInstance();
    }

    private void saveUserDataToSP(){
        String email = emailEditText.getText().toString().trim();
        Query queryGetData = FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("email")
                .equalTo(email);

        queryGetData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot x : snapshot.getChildren()){
                        UserModel user = x.getValue(UserModel.class);

                        // save user data to SP
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("key_email",user.getEmail());
                        editor.putString("key_username",user.getUsername());
                        editor.putString("key_current_user_password",user.getPassword());
                        editor.putString("key_fullname",user.getFullname());
                        editor.putString("key_phone",user.getPhone());
                        editor.apply();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onClickLogin(View view){
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
                        saveUserDataToSP();
                        startActivity(i);
                        saveEmailForLogin();
                        makeAutoLogin();
                        finish();
                    }else {
                        Toast.makeText(WelcomebackLogin.this, "Log in Error : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void loadSavedEmailForLogin()
    {
        String savedUsername = sharedPreferences.getString("key_saved_email", null);

        if (savedUsername != null){
            emailEditText.setText(savedUsername);
            chkRememberUsername.setChecked(true);
        }
    }

    private void saveEmailForLogin()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (chkRememberUsername.isChecked()){
            editor.putString("key_saved_email", emailEditText.getText().toString());
        } else {
            editor.remove("key_saved_email");
        }

        editor.apply();
    }

    private void makeAutoLogin()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (this.chkKeepLogin.isChecked()){
            editor.putBoolean("key_keep_login", true);
        } else {
            editor.remove("key_keep_login");
        }

        editor.apply();
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