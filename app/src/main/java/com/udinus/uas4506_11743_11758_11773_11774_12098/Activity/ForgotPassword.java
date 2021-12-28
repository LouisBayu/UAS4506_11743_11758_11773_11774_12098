package com.udinus.uas4506_11743_11758_11773_11774_12098.Activity;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.UserModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import java.util.ArrayList;

public class ForgotPassword extends AppCompatActivity {

    public SharedPreferences sharedPreferences;
    TextInputEditText editTextEmail;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference dbReference;

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
        initComponent();

    }

    private void initComponent(){
        editTextEmail = findViewById(R.id.emailEditText);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        dbReference = db.getReference("users");
        sharedPreferences = getSharedPreferences("appSharedPref", Context.MODE_PRIVATE);
    }

    public void onClickBack(View view) {
        Intent i = new Intent(ForgotPassword.this, WelcomebackLogin.class);
        startActivity(i);
        finish();
    }
    public void onClickNext(View view) {
        if (TextUtils.isEmpty(editTextEmail.getText().toString().trim())){
            Toast.makeText(view.getContext(),"Email Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
        }
        else if (!isValidEmail(editTextEmail.getText().toString().trim())){
            Toast.makeText(view.getContext(),"Email Tidak Valid!", Toast.LENGTH_SHORT).show();
        } else {
            checkEmailExist(editTextEmail.getText().toString().trim());
        }
    }

    public static boolean isValidEmail(CharSequence email){
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private void checkEmailExist(String email){
        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean check = !task.getResult().getSignInMethods().isEmpty();
                        if (!check){
                            Toast.makeText(getApplicationContext(), "Email Not Registered", Toast.LENGTH_SHORT).show();
                        } else {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("key_email_reset",editTextEmail.getText().toString().trim());
                            editor.apply();
                            saveOldPasswordToSP(editTextEmail.getText().toString().trim());
                        }
                    }
                });
    }

    private final void saveOldPasswordToSP(String email){
        Query getUserData = dbReference.orderByChild("email").equalTo(email);
        ArrayList<String> temp = new ArrayList<>();
        getUserData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot x : snapshot.getChildren()){
                    UserModel user = x.getValue(UserModel.class);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("key_old_password",user.getPassword());
                    editor.putString("key_username",user.getUsername());
                    editor.apply();
                    System.out.println(sharedPreferences.getString("key_old_password",null));
                    System.out.println(sharedPreferences.getString("key_username",null));
                    Intent i = new Intent(ForgotPassword.this, ResetPassword.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}