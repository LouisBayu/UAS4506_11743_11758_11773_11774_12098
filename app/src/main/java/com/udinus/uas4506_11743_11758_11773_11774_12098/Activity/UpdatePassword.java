package com.udinus.uas4506_11743_11758_11773_11774_12098.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

public class UpdatePassword extends AppCompatActivity {

    Boolean isPasswordVisible = false;
    TextInputEditText editTextCurrentPassword, editTextNewPassword, editTextConfirmPassword;
    TextView textShowPassword, changePassword;

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

        editTextCurrentPassword = findViewById(R.id.currentPasswordEditText);
        editTextNewPassword = findViewById(R.id.newPasswordEditText);
        editTextConfirmPassword = findViewById(R.id.confirmPasswordEditText);
        textShowPassword = findViewById(R.id.showPassword);
        changePassword = findViewById(R.id.changePassword);
    }

    public void cancelUpdate(View view) {
        Intent i = new Intent(UpdatePassword.this, EditProfil.class);
        startActivity(i);
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