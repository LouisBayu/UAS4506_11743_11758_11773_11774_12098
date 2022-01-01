package com.udinus.uas4506_11743_11758_11773_11774_12098.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class UpdateEmail extends AppCompatActivity {

    TextInputEditText emailEditText;
    TextView changeEmail;
    FirebaseAuth auth;
    SharedPreferences sharedPreferences;
    String username;
    FirebaseDatabase db;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
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
        emailEditText = findViewById(R.id.emailEditText);
        changeEmail = findViewById(R.id.changeEmail);

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmail();
            }
        });

        sharedPreferences = getSharedPreferences("appSharedPref", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("key_username",null);

        db = FirebaseDatabase.getInstance();
        dbReference = db.getReference("users");
        auth = FirebaseAuth.getInstance();
    }

    private void updateEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = emailEditText.getText().toString();

        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dbReference.child(username).child("email").setValue(email);
                Toast.makeText(UpdateEmail.this, "Email berhasil diganti", Toast.LENGTH_SHORT).show();
                finish();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateEmail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cancelUpdate(View view) {
        finish();
    }
}