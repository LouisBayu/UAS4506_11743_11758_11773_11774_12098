package com.udinus.uas4506_11743_11758_11773_11774_12098.View.FragmentMenu;

import android.content.Context;
import android.content.SharedPreferences;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter.AdapterResepProfil;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.ResepModel;
import com.google.android.material.button.MaterialButton;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.AddResep;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.EditProfil;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.SimpanResep;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class Tersimpan extends Fragment {

    ArrayList<ResepModel> resepBookmarked;
    RecyclerView rvBookmark;
    RecyclerView.LayoutManager layoutManager;
    SharedPreferences sharedPreferences;
    DatabaseReference bookmarkRef;
    StorageReference storageReference;
    AdapterResepProfil adapterResepProfil;
    LinearLayout emptyView, notEmptyView;

    MaterialButton btnSimpanResep;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved, container, false);
        initComponent(view);
        setRvBookmark(view);
        getBookmarkedResep();

        return view;
    }

    private void initComponent(View view){
        emptyView = view.findViewById(R.id.empty_bookmark);
        notEmptyView = view.findViewById(R.id.not_empty_bookmark);
        sharedPreferences = getActivity().getSharedPreferences("appSharedPref", Context.MODE_PRIVATE);
        bookmarkRef = FirebaseDatabase.getInstance()
                .getReference("favoriteList")
                .child(sharedPreferences.getString("key_username", null));
        storageReference = FirebaseStorage.getInstance().getReference();
        resepBookmarked = new ArrayList<>();

    }

    private void setRvBookmark(View view){
        rvBookmark = (RecyclerView) view.findViewById(R.id.rv_bookmark);
        rvBookmark.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this.getContext(), 2);
        rvBookmark.setLayoutManager(layoutManager);

        adapterResepProfil = new AdapterResepProfil(resepBookmarked, getActivity());
        rvBookmark.setAdapter(adapterResepProfil);
    }

    private void getBookmarkedResep(){

        bookmarkRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                resepBookmarked.clear();
                for (DataSnapshot x : snapshot.getChildren()){
                    ResepModel resep = new ResepModel();
                    resep.setNama(x.child("nama").getValue().toString());
                    resep.setAuthor(x.child("author").getValue().toString());
                    resep.setKategori(x.child("kategori").getValue().toString());
                    resep.setImage(x.child("image").getValue().toString());
                    resep.setBahan((ArrayList<String>) x.child("bahan").getValue());
                    resep.setLangkah((ArrayList<String>) x.child("langkah").getValue());
                    resepBookmarked.add(resep);
                }
                adapterResepProfil.notifyDataSetChanged();
                setViewBookmark();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setViewBookmark(){
        if (resepBookmarked.isEmpty()){
            emptyView.setVisibility(View.VISIBLE);
            notEmptyView.setVisibility(View.GONE);
        } else {
            notEmptyView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

}