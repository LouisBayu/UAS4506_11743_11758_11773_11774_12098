package com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.udinus.uas4506_11743_11758_11773_11774_12098.View.DetailResep;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.Resep;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import java.util.ArrayList;

public class ResepAdapter extends RecyclerView.Adapter<ResepAdapter.ResepViewHolder> {

    ArrayList<Resep> resepArray;
    Activity activity;
    ViewPager2 vpRecommended;

    public ResepAdapter(ArrayList<Resep> resepArray, Activity activity, ViewPager2 vp) {
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
        Resep resep = resepArray.get(position);

        holder.setResepData(resep);
        holder.itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( activity, DetailResep.class);
                intent.putExtra("nama",resep.getNama());
                intent.putExtra("author",resep.getAuthor());
                intent.putExtra("bahan",resep.getBahan());
                intent.putExtra("langkah",resep.getLangkah());
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

        void setResepData(Resep resep){
            name.setText(resep.getNama());
            author.setText("Dibagikan Oleh : " + resep.getAuthor());
            imageView.setImageResource(resep.getImage());
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
