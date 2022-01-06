package com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter;

import android.app.Activity;
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
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.ResepModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.DetailResep;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.Resep;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import java.util.ArrayList;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.ResepViewHolder> {

    ArrayList<ResepModel> resepArray;
    Activity activity;
    ViewPager2 vpRecommended;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    public RecommendedAdapter(ArrayList<ResepModel> resepArray, Activity activity, ViewPager2 vp) {
        this.resepArray = resepArray;
        this.activity = activity;
        this.vpRecommended = vp;
    }

    @NonNull
    @Override
    public ResepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ResepViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_recipe_design, parent, false
                )
        );

    }

    @Override
    public void onBindViewHolder(@NonNull ResepViewHolder holder, int position) {
        ResepModel resep = resepArray.get(position);
        StorageReference imgRef = firebaseStorage.getReferenceFromUrl(resepArray.get(position).getImage());

        holder.name.setText(resep.getNama());
        holder.author.setText("Dibagikan Oleh : " + resep.getAuthor());

        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(activity)
                        .load(uri)
                        .centerCrop()
                        .into(holder.imageView);
            }
        });

        holder.itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( activity, DetailResep.class);
                intent.putExtra("nama",resep.getNama());
                intent.putExtra("author",resep.getAuthor());
                intent.putExtra("bahan",resep.getBahan().toArray(new String[0]));
                intent.putExtra("langkah",resep.getLangkah().toArray(new String[0]));
                intent.putExtra("image", resep.getImage());
                activity.startActivity(intent);
            }
        });

        if (position == resepArray.size()-2){
            vpRecommended.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return resepArray.size();
    }


    public static class ResepViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView name, author;
        CardView itemList;
        Context context;

        public ResepViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            imageView = itemView.findViewById(R.id.resepImage);
            name = itemView.findViewById(R.id.namaResep);
            author = itemView.findViewById(R.id.author);
            itemList = itemView.findViewById(R.id.itemList);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            resepArray.addAll(resepArray);
            notifyDataSetChanged();
        }
    };
}
