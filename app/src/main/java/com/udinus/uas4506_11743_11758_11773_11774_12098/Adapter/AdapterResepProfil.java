package com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.ModelItemResepProfil;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdapterResepProfil extends RecyclerView.Adapter<AdapterResepProfil.ViewHolder> {

    ArrayList<ModelItemResepProfil> dataItem;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        TextView textCategory;
        ImageView imageResep;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.title_resep);
            textCategory = itemView.findViewById(R.id.category_resep);
            imageResep = itemView.findViewById(R.id.img_resep);
        }
    }

    public AdapterResepProfil(ArrayList<ModelItemResepProfil> data){
        this.dataItem = data;
    }

    @NonNull
    @Override
    public AdapterResepProfil.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_resep_profil, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterResepProfil.ViewHolder holder, int position) {

        TextView text_title = holder.textTitle;
        TextView text_category = holder.textCategory;
        ImageView image_resep = holder.imageResep;

        text_title.setText(dataItem.get(position).getTitle());
        text_category.setText(dataItem.get(position).getCategory());
        image_resep.setImageResource(dataItem.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }


}
