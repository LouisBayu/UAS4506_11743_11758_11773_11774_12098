package com.udinus.uas4506_11743_11758_11773_11774_12098.Activity;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

public class UpdatePassword extends AppCompatActivity {

    Boolean isPasswordVisible = false;
    TextInputEditText editTextCurrentPassword, editTextNewPassword, editTextConfirmPassword;
    TextView textShowPassword, changePassword;
    SharedPreferences sharedPreferences;
    String username;
    FirebaseDatabase db;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
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
        editTextCurrentPassword = findViewById(R.id.currentPasswordEditText);
        editTextNewPassword = findViewById(R.id.newPasswordEditText);
        editTextConfirmPassword = findViewById(R.id.confirmPasswordEditText);
        textShowPassword = findViewById(R.id.showPassword);
        changePassword = findViewById(R.id.changePassword);

        sharedPreferences = getSharedPreferences("appSharedPref", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("key_username",null);

        db = FirebaseDatabase.getInstance();
        dbReference = db.getReference("users");

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = editTextCurrentPassword.getText().toString();
                String newPassword = editTextNewPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();
                String currentUserPassword = sharedPreferences.getString("key_current_user_password", null);

                if (TextUtils.isEmpty(currentPassword)){
                    Toast.makeText(UpdatePassword.this, "Masukkan Password saat ini", Toast.LENGTH_SHORT).show();
                }
                else if (!currentPassword.equals(currentUserPassword)){
                    Toast.makeText(UpdatePassword.this, "Current Password Salah!", Toast.LENGTH_SHORT).show();
                }
                else if (newPassword.isEmpty()){
                    Toast.makeText(UpdatePassword.this, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                else if (confirmPassword.isEmpty()){
                    Toast.makeText(UpdatePassword.this, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                else if (newPassword.compareTo(confirmPassword) != 0){
                    Toast.makeText(UpdatePassword.this, "Password tidak cocok", Toast.LENGTH_SHORT).show();
                }
                else if (newPassword.length() < 6 || confirmPassword.length() < 6){
                    Toast.makeText(UpdatePassword.this, "Panjang Password Minimal 6 Karakter!", Toast.LENGTH_SHORT).show();
                }
                else{
                    updatePassword(currentPassword, newPassword);
                }
            }
        });
    }

    public void updatePassword(String currentPassword, String newPassword){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

        user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            dbReference.child(username).child("password").setValue(newPassword);
                            Toast.makeText(UpdatePassword.this, "Password berhasil diganti", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdatePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdatePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void cancelUpdate(View view) {
        finish();
    }

    public void onClickShowPassword(View view){
        if(isPasswordVisible){
            isPasswordVisible = false;
            editTextCurrentPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editTextNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editTextConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editTextCurrentPassword.setSelection(editTextCurrentPassword.getText().length());
            editTextNewPassword.setSelection(editTextNewPassword.getText().length());
            editTextConfirmPassword.setSelection(editTextConfirmPassword.getText().length());
            textShowPassword.setText("Show Password");
        }
        else {
            isPasswordVisible = true;
            editTextCurrentPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editTextNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editTextConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editTextCurrentPassword.setSelection(editTextCurrentPassword.getText().length());
            editTextNewPassword.setSelection(editTextNewPassword.getText().length());
            editTextConfirmPassword.setSelection(editTextConfirmPassword.getText().length());
            textShowPassword.setText("Hide Password");
        }
    }
}