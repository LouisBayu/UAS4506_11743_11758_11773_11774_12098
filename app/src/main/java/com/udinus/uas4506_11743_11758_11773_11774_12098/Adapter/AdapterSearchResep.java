package com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.DetailResep;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.ResepModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterSearchResep extends RecyclerView.Adapter<AdapterSearchResep.ViewHolder> {
    ArrayList<ResepModel> dataItem;
    Context context;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    SharedPreferences sharedPreferences;
    DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("favorites");
    DatabaseReference favListRef;
    Boolean favChecker = false;
    String user;
    ResepModel resepModel = new ResepModel();

    public AdapterSearchResep(ArrayList<ResepModel> data, Context context){
        this.dataItem = data;
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("appSharedPref", Context.MODE_PRIVATE);
        this.user = this.sharedPreferences.getString("key_username",null);
        favListRef = FirebaseDatabase.getInstance().getReference("favoriteList")
                .child(user);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StorageReference imgRef = firebaseStorage.getReferenceFromUrl(dataItem.get(position).getImage());
        ResepModel resep = dataItem.get(position);
        final String keyResep = dataItem.get(position).getNama().toLowerCase();

        TextView nama = holder.nama;
        TextView kategori = holder.kategori;
        TextView author = holder.author;
        CircleImageView imageResep = holder.imageResep;
        CardView cardView = holder.cardView;
        ImageButton favButton = holder.btnFavorite;

        nama.setText(resep.getNama());
        kategori.setText(resep.getKategori());
        author.setText("Oleh : "+resep.getAuthor());

        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .centerCrop()
                        .into(imageResep);
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( context, DetailResep.class);
                intent.putExtra("nama",resep.getNama());
                intent.putExtra("author",resep.getAuthor());
                intent.putExtra("bahan",resep.getBahan().toArray(new String[0]));
                intent.putExtra("langkah",resep.getLangkah().toArray(new String[0]));
                intent.putExtra("image", resep.getImage());
                context.startActivity(intent);
            }
        });

        holder.favoriteChecker(keyResep);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favChecker = true;

                favRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (favChecker.equals(true)){
                            if (snapshot.child(keyResep).hasChild(user)){
                                favRef.child(keyResep).child(user).removeValue();
                                delete(resep.getNama());
                                favChecker = false;
                            } else {
                                favRef.child(keyResep).child(user).setValue(true);
                                resepModel.setNama(resep.getNama());
                                resepModel.setAuthor(resep.getAuthor());
                                resepModel.setKategori(resep.getKategori());
                                resepModel.setImage(resep.getImage());
                                resepModel.setBahan((ArrayList<String>) resep.getBahan());
                                resepModel.setLangkah((ArrayList<String>) resep.getLangkah());

                                favListRef.child(keyResep).setValue(resepModel);
                                Toast.makeText(context, "Berhasil Ditambahkan ke Bookmark", Toast.LENGTH_SHORT).show();
                                favChecker = false;

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }

    public void filterResep(ArrayList<ResepModel> filteredArrayResep){
        dataItem = filteredArrayResep;
        notifyDataSetChanged();
    }

    void delete(String nama){
        Query query = favListRef.orderByChild("nama").equalTo(nama);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot x : snapshot.getChildren()){
                    x.getRef().removeValue();
                    Toast.makeText(context, "Berhasil Dihapus Dari Bookmark", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, kategori, author;
        CircleImageView imageResep;
        CardView cardView;
        ImageButton btnFavorite;
        DatabaseReference favoriteRef;
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.nama_resep_search);
            kategori = itemView.findViewById(R.id.kategori_resep_search);
            author = itemView.findViewById(R.id.author_resep_search);
            imageResep = itemView.findViewById(R.id.img_resep_search);
            cardView = itemView.findViewById(R.id.cardViewSearch);
            btnFavorite = itemView.findViewById(R.id.favorite_resep);
        }

        public void favoriteChecker(String keyResep) {
            btnFavorite = itemView.findViewById(R.id.favorite_resep);
            favoriteRef = db.getReference("favorites");

            favoriteRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(keyResep).hasChild(user)){
                        btnFavorite.setImageResource(R.drawable.ic_bookmarked);
                    } else {
                        btnFavorite.setImageResource(R.drawable.ic_not_bookmarked);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
