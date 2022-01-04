package com.udinus.uas4506_11743_11758_11773_11774_12098.View.FragmentKategori;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter.AdapterResepKategori;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.ResepModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import java.util.ArrayList;

public class FragmentJajanan extends Fragment {

    RecyclerView recyclerView;
    ArrayList<ResepModel> resepArray;
    Context context;
    DatabaseReference resepRef;
    StorageReference storageReference;
    AdapterResepKategori adapterResepKategori;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jajanan, container, false);
        initComponent(view);
        setRecyclerView();
        getResepFromFirebase("Jajanan");
        return view;
    }

    private void initComponent(View view){
        context = getActivity();
        recyclerView = (RecyclerView)view.findViewById(R.id.viewJajanan);
        resepRef = FirebaseDatabase.getInstance().getReference("resep");
        storageReference = FirebaseStorage.getInstance().getReference();
        resepArray = new ArrayList<>();
    }
    private void setRecyclerView(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapterResepKategori = new AdapterResepKategori(resepArray,context);
        recyclerView.setAdapter(adapterResepKategori);
    }

    private void getResepFromFirebase(String kategori) {
        Query getUserResep = resepRef.orderByChild("kategori").equalTo(kategori);

        getUserResep.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                resepArray.clear();
                for (DataSnapshot x : snapshot.getChildren()) {
                    ResepModel resep = new ResepModel();
                    resep.setNama(x.child("nama").getValue().toString());
                    resep.setAuthor(x.child("author").getValue().toString());
                    resep.setKategori(x.child("kategori").getValue().toString());
                    resep.setImage(x.child("image").getValue().toString());
                    resep.setBahan((ArrayList<String>) x.child("bahan").getValue());
                    resep.setLangkah((ArrayList<String>) x.child("langkah").getValue());
                    resepArray.add(resep);
                }
                adapterResepKategori.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
