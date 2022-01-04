package com.udinus.uas4506_11743_11758_11773_11774_12098.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

public class UpdateFullName extends AppCompatActivity {

    TextInputEditText fullnameEditText;
    TextView changeFullName;
    SharedPreferences sharedPreferences;
    String username;
    FirebaseDatabase db;
    DatabaseReference dbReference;

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
        initComponent();


    }

    private void initComponent(){
        fullnameEditText = findViewById(R.id.fullnameEditText);
        changeFullName = findViewById(R.id.changeFullName);

        sharedPreferences = getSharedPreferences("appSharedPref", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("key_username", null);
        db = FirebaseDatabase.getInstance();
        dbReference = db.getReference("users");
    }

    public void onClickSave(View view){
        String newFullname = fullnameEditText.getText().toString().trim();
        dbReference.child(username).child("fullname").setValue(newFullname).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Data Berhasil Di Update!",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Update Gagal : "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cancelUpdate(View view) {
        finish();
    }

}