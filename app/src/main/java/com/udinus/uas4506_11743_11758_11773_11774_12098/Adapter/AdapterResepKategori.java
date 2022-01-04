package com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.ResepModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.DetailResep;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterResepKategori extends RecyclerView.Adapter<AdapterResepKategori.ViewHolder> {
    ArrayList<ResepModel> dataItem;
    Context context;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    public AdapterResepKategori(ArrayList<ResepModel> data, Context context){
        this.dataItem = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resep_kategori, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StorageReference imgRef = firebaseStorage.getReferenceFromUrl(dataItem.get(position).getImage());
        ResepModel resep = dataItem.get(position);

        TextView nama = holder.nama;
        TextView author = holder.author;
        CircleImageView imageResep = holder.imageResep;
        CardView cardView = holder.cardView;

        nama.setText(resep.getNama());
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
    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, author;
        CircleImageView imageResep;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.nama_resep_search);
            author = itemView.findViewById(R.id.author_resep_search);
            imageResep = itemView.findViewById(R.id.img_resep_search);
            cardView = itemView.findViewById(R.id.cardViewSearch);
        }
    }
}
