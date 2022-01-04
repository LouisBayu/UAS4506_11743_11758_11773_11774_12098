package com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment;

import static java.util.Objects.requireNonNull;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.SearchResep;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.SearchResepHolder;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class Search extends Fragment {


    EditText mSearchField;
    ImageButton mSearchBtn;
    RecyclerView mResultList;
    DatabaseReference mResepDatabase;

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseRecyclerOptions<SearchResep> options;
    FirebaseRecyclerAdapter<SearchResep, SearchResepHolder> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {   
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.viewCariResep);
        recyclerView.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("resep");

        options = new FirebaseRecyclerOptions.Builder<SearchResep>().setQuery(databaseReference, SearchResep.class).build();

        adapter = new FirebaseRecyclerAdapter<SearchResep, SearchResepHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SearchResepHolder holder, int position, @NonNull SearchResep model) {
                Glide.with(holder.itemView.getContext())
                        .load(model.getImage())
                        .apply(new RequestOptions().override(1000, 1000))
                        .into(holder.img_resep);
                holder.nama.setText(model.getNama());
                holder.kategori.setText(model.getKategori());
                holder.author.setText("Oleh : "+model.getAuthor());
            }

            @NonNull
            @Override
            public SearchResepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list,parent, false);
                return new SearchResepHolder(view);
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.startListening();
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    public void onStop(){
        if (adapter != null)
            adapter.stopListening();
        super.onStop();
    }

    @Override
    public void onResume(){
        super.onResume();
        if (adapter != null) {
            adapter.startListening();
        }
    }
}