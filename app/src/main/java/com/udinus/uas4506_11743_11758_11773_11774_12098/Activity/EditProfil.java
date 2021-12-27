package com.udinus.uas4506_11743_11758_11773_11774_12098.Activity;

import static com.udinus.uas4506_11743_11758_11773_11774_12098.R.layout.*;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.style.UpdateLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment.Profil;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.UserModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

public class EditProfil extends AppCompatActivity{

    TextView edtEmail, edtFullname, edtUsername, edtPhone;
    SharedPreferences sharedPreferences;
    FirebaseDatabase db;
    DatabaseReference dbReference;
    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_edit_profil);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.bg));
        }
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
        Intent i = new Intent(EditProfil.this, Profil.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
    }

    public void onClikUbahPhoto(View view){

    }

    public void updateFullName(View view){
        setContentView(activity_update_full_name);
    }

    public void updateEmail(View view){
        setContentView(activity_update_email);
    }

    public void updatePhone(View view){
        setContentView(activity_update_phone);
    }

    public void updatePassword(View view){
        setContentView(activity_update_password);
    }

    public void onClickHelpSupport(View view){

    }

    public void onClickDeleteAccount(View view){

    }

}
