package com.udinus.uas4506_11743_11758_11773_11774_12098.View.FragmentMenu;

import static java.util.Objects.requireNonNull;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter.AdapterSearchResep;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.ResepModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class Search extends Fragment {

    EditText mSearchField;
    TextView tvHasil;
    RecyclerView rvSearch;
    ArrayList<ResepModel> resepArray;
    Context context;
    DatabaseReference resepRef;
    AdapterSearchResep adapterSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        initComponent(view);
        setRvSearch();
        getResepFromFirebase();

        return view;
    }

    private void initComponent(View view){
        context = getActivity();
        rvSearch = (RecyclerView)view.findViewById(R.id.viewCariResep);
        mSearchField = view.findViewById(R.id.search_view);
        tvHasil = view.findViewById(R.id.tvHasil);
        resepRef = FirebaseDatabase.getInstance().getReference("resep");
        resepArray = new ArrayList<>();

        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
                if (editable.toString().equals("")){
                    tvHasil.setText("Semua Resep");
                } else {
                    tvHasil.setText("Hasil Pencarian Resep");
                }
            }
        });
    }

    private void filter(String textSearch){
        ArrayList<ResepModel> filteredArray = new ArrayList<>();

        for (ResepModel x : resepArray){
            if (x.getNama().toLowerCase().contains(textSearch.toLowerCase())){
                filteredArray.add(x);
            }
        }

        adapterSearch.filterResep(filteredArray);
    }

    private void setRvSearch(){
        rvSearch.setHasFixedSize(true);
        rvSearch.setLayoutManager(new LinearLayoutManager(context));
        adapterSearch = new AdapterSearchResep(resepArray,context);
        rvSearch.setAdapter(adapterSearch);
    }

    private void getResepFromFirebase(){
        resepRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                resepArray.clear();
                for (DataSnapshot x : snapshot.getChildren()){
                    ResepModel resep = new ResepModel();
                    resep.setNama(x.child("nama").getValue().toString());
                    resep.setAuthor(x.child("author").getValue().toString());
                    resep.setKategori(x.child("kategori").getValue().toString());
                    resep.setImage(x.child("image").getValue().toString());
                    resep.setBahan((ArrayList<String>) x.child("bahan").getValue());
                    resep.setLangkah((ArrayList<String>) x.child("langkah").getValue());
                    resepArray.add(resep);
                }
                adapterSearch.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}