package com.udinus.uas4506_11743_11758_11773_11774_12098.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment.Profil;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

public class UpdateData extends AppCompatActivity {

    Boolean isPasswordVisible = false;
    TextInputEditText editTextCurrentPassword, editTextNewPassword, editTextConfirmPassword;
    TextView textShowPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    public void klikKembaliEdit(View view) {
        Intent i = new Intent(UpdateData.this, EditProfil.class);
        startActivity(i);
        finish();
    }

    public void onClickUpdateFullName(View view) {

    }

    public void onClickUpdateEmail(View view) {

    }

    public void onClickUpdatePhone(View view) {

    }

    public void onClickUpdatePassword(View view) {

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