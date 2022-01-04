package com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Activity.EditProfil;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Activity.MainActivity;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Activity.WelcomebackLogin;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter.AdapterResepProfil;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.ItemResepProfil;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.ModelItemResepProfil;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.ResepModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.UserModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import java.util.ArrayList;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profil extends Fragment {

    CircleImageView imgProfil;
    MaterialButton btnEditProfil;
    TextView logout, username, fullname;

    RecyclerView recyclerView;
    AdapterResepProfil adapterResepProfil;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ModelItemResepProfil> data;
    ArrayList<ResepModel> resepUserArray;

    SharedPreferences sharedPreferences;
    FirebaseDatabase db;
    DatabaseReference dbReference, resepRef;
    FirebaseAuth auth;
    StorageReference storageReference;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil,container,false);
        initComponent(view);

        getDataProfil();
        getDataResepUser();
        return view;
    }

    private void initComponent(View view){
        context = getActivity();
        username = view.findViewById(R.id.Username);
        fullname = view.findViewById(R.id.FullName);
        btnEditProfil = view.findViewById(R.id.edtProfile);
        logout = view.findViewById(R.id.logout);
        imgProfil = view.findViewById(R.id.imgProfil);

        db = FirebaseDatabase.getInstance();
        dbReference = db.getReference("users");
        resepRef = db.getReference("resep");
        sharedPreferences = getActivity().getSharedPreferences("appSharedPref", Context.MODE_PRIVATE);


        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        recyclerView = view.findViewById(R.id.rv_resep_profil);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        resepUserArray = new ArrayList<>();

        adapterResepProfil = new AdapterResepProfil(resepUserArray, context);
        recyclerView.setAdapter(adapterResepProfil);



        setButtonsOnClick();
    }

    private void getDataResepUser(){
        String username = sharedPreferences.getString("key_username",null);
        Query getUserResep = resepRef.orderByChild("author").equalTo(username);

        getUserResep.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                resepUserArray.clear();
                for (DataSnapshot x : snapshot.getChildren()){
                    ResepModel resep = new ResepModel();
                    resep.setNama(x.child("nama").getValue().toString());
                    resep.setAuthor(x.child("author").getValue().toString());
                    resep.setKategori(x.child("kategori").getValue().toString());
                    resep.setImage(x.child("image").getValue().toString());
                    resep.setBahan((ArrayList<String>) x.child("bahan").getValue());
                    resep.setLangkah((ArrayList<String>) x.child("bahan").getValue());
                    resepUserArray.add(resep);
                }
                adapterResepProfil.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setButtonsOnClick(){
        btnEditProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), EditProfil.class);
                startActivity(i);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent i = new Intent(getActivity(), WelcomebackLogin.class);
                startActivity(i);
                getActivity().finish();
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.remove("key_keep_login");
                editor.apply();
            }
        });
    }

    private void getDataProfil(){
        String email = sharedPreferences.getString("key_email", null);
        Query getUserData = dbReference.orderByChild("email").equalTo(email);

        getUserData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot x : snapshot.getChildren()){
                    UserModel user = x.getValue(UserModel.class);
                    username.setText(user.getUsername());
                    fullname.setText(user.getFullname());
                    loadImgProfile("users/"+user.getUsername()+"-profile.jpg");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
}