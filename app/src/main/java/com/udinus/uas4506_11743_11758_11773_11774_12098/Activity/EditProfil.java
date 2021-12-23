package com.udinus.uas4506_11743_11758_11773_11774_12098.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment.Home;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment.Profil;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.UserModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import java.util.ArrayList;
import java.util.List;

public class EditProfil extends AppCompatActivity {

    EditText edtEmail, edtFullname, edtUsername, edtPhone;
    SharedPreferences sharedPreferences;
    FirebaseDatabase db;
    DatabaseReference dbReference;
    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
        initComponent();
        getDataUser();


    }

    private void initComponent(){
        edtEmail = findViewById(R.id.edtEmail);
        edtFullname = findViewById(R.id.edtFullName);
        edtUsername = findViewById(R.id.edtUsername);
        edtPhone = findViewById(R.id.edtPhone);
        user = new UserModel();
        db = FirebaseDatabase.getInstance();
        dbReference = db.getReference("users");
    }

    private void getDataUser(){
        sharedPreferences = getSharedPreferences("appSharedPref", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("key_email", null);
        Query queryGetData = FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("email")
                .equalTo(email);

        queryGetData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot x : snapshot.getChildren()){
                        user = x.getValue(UserModel.class);
                        setDataToEditText();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setDataToEditText(){
        edtUsername.setText(user.getUsername());
        edtFullname.setText(user.getFullname());
        edtEmail.setText(user.getEmail());
        edtPhone.setText(user.getPhone());
    }

    public void klikKembali(View view) {
        finish();
    }

    public void onClikUbahPhoto(View view){

    }


}
