package com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.DetailResep;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.ResepModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import java.util.ArrayList;

public class AdapterResepProfil extends RecyclerView.Adapter<AdapterResepProfil.ViewHolder> {

    ArrayList<ResepModel> dataItem;
    Context context;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    public AdapterResepProfil(ArrayList<ResepModel> data, Context context){
        this.dataItem = data;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterResepProfil.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_resep_profil, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterResepProfil.ViewHolder holder, int position) {

        StorageReference imgRef = firebaseStorage.getReferenceFromUrl(dataItem.get(position).getImage());
        ResepModel resep = dataItem.get(position);

        TextView text_title = holder.textTitle;
        TextView text_category = holder.textCategory;
        ImageView image_resep = holder.imageResep;
        CardView cardView = holder.cardView;

        // Set data to view
        text_title.setText(resep.getNama());
        text_category.setText(resep.getKategori());

        // Get image from firebase
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .centerCrop()
                        .into(image_resep);
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( context, DetailResep.class);
                intent.putExtra("nama",resep.getNama());
                intent.putExtra("author",resep.getAuthor());
                intent.putExtra("kategori",resep.getKategori());
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
        TextView textTitle;
        TextView textCategory;
        ImageView imageResep;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.title_resep);
            textCategory = itemView.findViewById(R.id.category_resep);
            imageResep = itemView.findViewById(R.id.img_resep);
            cardView = itemView.findViewById(R.id.cardResep);
        }
    }

}
