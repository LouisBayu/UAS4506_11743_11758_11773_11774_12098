package com.udinus.uas4506_11743_11758_11773_11774_12098.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment.Home;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment.Profil;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

public class EditProfil extends AppCompatActivity {

//    TextInputLayout fullname, email, phone, password;
//    TextView fullnameLabel, usernameLabel;
//
//    DatabaseReference reference;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);


        // hooks
//        fullname = findViewById(R.id.edtFullName);
//        email = findViewById(R.id.edtEmail);
//        phone = findViewById(R.id.edtPhone);
//        password = findViewById(R.id.edtPassword);
//        fullnameLabel = findViewById(R.id.fullnameEditText);
//        usernameLabel = findViewById(R.id.usernameEditText);

        // show all data
//        showAllUserData();

    }

//    private void showAllUserData() {
//        Intent intent = getIntent();
//        _USERNAME = intent.getStringExtra("username");
//        _NAME = intent.getStringExtra("name");
//        _EMAIL = intent.getStringExtra("email");
//        _PHONE = intent.getStringExtra("phone");
//        _PASSWORD = intent.getStringExtra("password");
//
//        fullnameLabel.setText(_NAME);
//        usernameLabel.setText(_USERNAME);
//        fullname.getEditText().setText(_NAME);
//        email.getEditText().setText(_EMAIL);
//        phone.getEditText().setText(_PHONE);
//        password.getEditText().setText(_PASSWORD) ;
//
//    }

//    public void btnUpdate(View view) {
//        if (isNameChanged() || isPasswordChanged()) {
//            Toast.makeText(this, "Data has been updated", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    private boolean isPasswordChanged() {
//
//    }
//
//    private boolean isNameChanged() {
//    }

    public void klikKembali(View view) {
        Intent i = new Intent(EditProfil.this, Profil.class);
        startActivity(i);
    }

    public void onClikUbahPhoto(View view){

    }
}
