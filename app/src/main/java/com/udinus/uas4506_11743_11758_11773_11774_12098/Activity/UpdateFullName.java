package com.udinus.uas4506_11743_11758_11773_11774_12098.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

public class UpdateFullName extends AppCompatActivity {

    TextInputEditText fullnameEditText;
    TextView changeFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_full_name);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.bg));
        }

        fullnameEditText = findViewById(R.id.fullnameEditText);
        changeFullName = findViewById(R.id.changeFullName);

    }

    public void cancelUpdate(View view) {
        Intent i = new Intent(UpdateFullName.this, EditProfil.class);
        startActivity(i);
        finish();
    }

}