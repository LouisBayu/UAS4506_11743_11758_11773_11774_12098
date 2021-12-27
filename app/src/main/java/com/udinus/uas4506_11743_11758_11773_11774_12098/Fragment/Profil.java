package com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Activity.EditProfil;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Activity.MainActivity;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Activity.WelcomebackLogin;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter.AdapterResepProfil;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.ItemResepProfil;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.ModelItemResepProfil;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.UserModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class Profil extends Fragment {

    MaterialButton btnEditProfil;
    TextView logout, username, fullname;

    RecyclerView recyclerView;
    AdapterResepProfil adapterResepProfil;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ModelItemResepProfil> data;

    SharedPreferences sharedPreferences;
    FirebaseDatabase db;
    DatabaseReference dbReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil,container,false);
        username = view.findViewById(R.id.Username);
        fullname = view.findViewById(R.id.FullName);

        db = FirebaseDatabase.getInstance();
        dbReference = db.getReference("users");
        sharedPreferences = getActivity().getSharedPreferences("appSharedPref", Context.MODE_PRIVATE);

        profil();

        recyclerView = view.findViewById(R.id.rv_resep_profil);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        data = new ArrayList<>();
        for (int i = 0; i < ItemResepProfil.title.length; i++){
            data.add(new ModelItemResepProfil(
                    ItemResepProfil.title[i],
                    ItemResepProfil.category[i],
                    ItemResepProfil.image[i]
            ));
        }

        adapterResepProfil = new AdapterResepProfil(data);
        recyclerView.setAdapter(adapterResepProfil);

        btnEditProfil = view.findViewById(R.id.edtProfile);
        logout = view.findViewById(R.id.logout);
        btnEditProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditProfil.class);
                startActivity(i);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), WelcomebackLogin.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        return view;
    }

    private void profil(){
        String email = sharedPreferences.getString("key_email", null);
        Query getUserData = dbReference.orderByChild("email").equalTo(email);

        getUserData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot x : snapshot.getChildren()){
                    UserModel user = x.getValue(UserModel.class);
                    username.setText(user.getUsername());
                    fullname.setText(user.getFullname());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}