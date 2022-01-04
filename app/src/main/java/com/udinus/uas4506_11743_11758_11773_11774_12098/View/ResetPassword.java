package com.udinus.uas4506_11743_11758_11773_11774_12098.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

public class ResetPassword extends AppCompatActivity {

    Boolean isPasswordVisible = false;
    TextInputEditText editTextNewPassword, editTextConfirmPassword;
    TextView textShowPassword;
    SharedPreferences sharedPreferences;
    String emailReset, oldPass, username;
    FirebaseUser user;
    FirebaseDatabase db;
    DatabaseReference dbReference;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
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
        editTextNewPassword = findViewById(R.id.newPasswordEditText);
        editTextConfirmPassword = findViewById(R.id.confirmPasswordEditText);
        textShowPassword = findViewById(R.id.showPassword);

        sharedPreferences = getSharedPreferences("appSharedPref", Context.MODE_PRIVATE);
        emailReset = sharedPreferences.getString("key_email_reset", null);
        oldPass = sharedPreferences.getString("key_old_password", null);
        username = sharedPreferences.getString("key_username", null);

        System.out.println(emailReset + oldPass + username);

        db = FirebaseDatabase.getInstance();
        dbReference = db.getReference("users");
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(emailReset,oldPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    user = auth.getCurrentUser();
                } else {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onClickChange(View view) {
        // Validasi Inputan Confirm & New
        if (TextUtils.isEmpty(editTextNewPassword.getText().toString().trim())
                || TextUtils.isEmpty(editTextConfirmPassword.getText().toString().trim())){
            Toast.makeText(view.getContext(), "Inputan Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
        } else if (!editTextNewPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())){
            Toast.makeText(view.getContext(), "New dan Confirm Password Harus Sama!", Toast.LENGTH_SHORT).show();
        } else if (editTextNewPassword.getText().toString().length() < 6
                || editTextConfirmPassword.getText().toString().length() < 6){
            Toast.makeText(ResetPassword.this, "Panjang Password Minimal 6 Karakter!", Toast.LENGTH_SHORT).show();
        }
        else {
            String newPass = editTextNewPassword.getText().toString();
            System.out.println(newPass);
            user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Reset Password Gagal!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Change Password
                        dbReference.child(username).child("password").setValue(newPass);
                        Toast.makeText(getApplicationContext(), "Reset Password Sukses!", Toast.LENGTH_SHORT).show();
                        auth.signOut();
                        Intent i = new Intent(ResetPassword.this, ResetPasswordSuccess.class);
                        startActivity(i);
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ResetPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void onClickShowPassword(View view){
        if(isPasswordVisible){
            isPasswordVisible = false;
            editTextNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editTextConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editTextNewPassword.setSelection(editTextNewPassword.getText().length());
            editTextConfirmPassword.setSelection(editTextConfirmPassword.getText().length());
            textShowPassword.setText("Show Password");
        }
        else {
            isPasswordVisible = true;
            editTextNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editTextConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editTextNewPassword.setSelection(editTextNewPassword.getText().length());
            editTextConfirmPassword.setSelection(editTextConfirmPassword.getText().length());
            textShowPassword.setText("Hide Password");
        }
    }
}