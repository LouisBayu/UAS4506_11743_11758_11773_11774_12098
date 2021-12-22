package com.udinus.uas4506_11743_11758_11773_11774_12098.Activity;

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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

public class ResetPassword extends AppCompatActivity {

    Boolean isPasswordVisible = false;
    TextInputEditText editTextNewPassword;
    TextInputEditText editTextConfirmPassword;
    TextInputEditText editTextPhone;
    TextView textShowPassword;

    String _NEWPASSWORD, _CONFIRMPASSWORD, _PHONE;

    DatabaseReference reference;

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

        reference = FirebaseDatabase.getInstance().getReference("users");

        editTextNewPassword = findViewById(R.id.newPasswordEditText);
        editTextConfirmPassword = findViewById(R.id.confirmPasswordEditText);
        textShowPassword = findViewById(R.id.showPassword);
        editTextPhone = findViewById(R.id.phoneEditText);

        showAllUserData();

    }

    private void showAllUserData(){
        Intent intent = getIntent();
        _NEWPASSWORD = intent.getStringExtra("password");
        _CONFIRMPASSWORD = intent.getStringExtra("password");
        _PHONE = intent.getStringExtra("phone");

        editTextNewPassword.setText(_NEWPASSWORD);
        editTextConfirmPassword.setText(_CONFIRMPASSWORD);
        editTextPhone.setText(_PHONE);
    }

    public void onClickChange(View view) {
        if (isPasswordChanged()) {
            Toast.makeText(this, "Data terubah", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Data gagal terubah", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ResetPassword.this, ResetPasswordSuccess.class);
            startActivity(i);
            finish();
        }
//        // Validasi Inputan Confirm & New
//        if (TextUtils.isEmpty(editTextNewPassword.getText().toString().trim())
//            || TextUtils.isEmpty(editTextConfirmPassword.getText().toString().trim())){
//            Toast.makeText(view.getContext(), "Inputan Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
//        } else if (!editTextNewPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())){
//            Toast.makeText(view.getContext(), "New dan Confirm Password Harus Sama!", Toast.LENGTH_SHORT).show();
//        } else {
//            Intent i = new Intent(ResetPassword.this, ResetPasswordSuccess.class);
//            startActivity(i);
//            finish();
//        }
    }

    private boolean isPasswordChanged(){
        if (!_NEWPASSWORD.equals(editTextNewPassword.getText().toString())) {
            reference.child(_PHONE).child("password").setValue(editTextPhone.getText().toString());
            _NEWPASSWORD = editTextNewPassword.getText().toString();
            return true;
        }else {
            return false;
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