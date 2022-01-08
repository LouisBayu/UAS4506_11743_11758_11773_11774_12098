package com.udinus.uas4506_11743_11758_11773_11774_12098.View;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.FragmentMenu.Profil;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.UserModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfil extends AppCompatActivity{

    TextView edtEmail, edtFullname, edtUsername, edtPhone, changePhoto;
    MaterialButton hapusAkun;
    CircleImageView  imgProfil;
    SharedPreferences sharedPreferences;
    FirebaseDatabase db;
    DatabaseReference dbReference;
    UserModel user;
    StorageReference storageReference;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
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

    @Override
    protected void onResume() {
        super.onResume();
        getDataUser();
    }

    private void initComponent(){
        imgProfil = findViewById(R.id.imgProfil);
        edtEmail = findViewById(R.id.edtEmail);
        edtFullname = findViewById(R.id.edtFullName);
        edtUsername = findViewById(R.id.edtUsername);
        edtPhone = findViewById(R.id.edtPhone);
        changePhoto = findViewById(R.id.changePhoto);
        hapusAkun = findViewById(R.id.hapusAkun);
        context = this;
        user = new UserModel();
        db = FirebaseDatabase.getInstance();
        dbReference = db.getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference();

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        hapusAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });
    }

    private void deleteAccount() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(EditProfil.this, WelcomebackLogin.class));
                    finish();
                } else {
                    Log.w(TAG,"Something is wrong!");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();

                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri){
        String username = sharedPreferences.getString("key_username", null);
        String ref = "users/" + username + "-profile.jpg";
        StorageReference imgFileRef = storageReference.child(ref);
        imgFileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                loadImgProfile(ref);
                Toast.makeText(EditProfil.this, "Berhasil Upload", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfil.this, "Upload Gagal!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadImgProfile(String ref){
        StorageReference imgFileRef = storageReference.child(ref);
        imgFileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(imgProfil);
            }
        });
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
                        setDataProfile();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setDataProfile(){
        edtUsername.setText(user.getUsername());
        edtFullname.setText(user.getFullname());
        edtEmail.setText(user.getEmail());
        edtPhone.setText(user.getPhone());
        loadImgProfile("users/" + user.getUsername()+ "-profile.jpg");
    }

    public void klikKembali(View view) {
        Intent i = new Intent(EditProfil.this, Profil.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        setResult(Activity.RESULT_OK);
    }

    public void updateFullName(View view){
        Intent i = new Intent(EditProfil.this, UpdateFullName.class);
        startActivity(i);
    }

    public void updateEmail(View view){
        Intent i = new Intent(EditProfil.this, UpdateEmail.class);
        startActivity(i);
    }

    public void updatePhone(View view){
        Intent i = new Intent(EditProfil.this, UpdatePhone.class);
        startActivity(i);
    }

    public void updatePassword(View view){
        Intent i = new Intent(EditProfil.this, UpdatePassword.class);
        startActivity(i);
    }

    public void onClickHelpSupport(View view){
        Intent i = new Intent(EditProfil.this, HelpSupport.class);
        startActivity(i);
    }

    public void onClickDeleteAccount(View view){

    }

}
