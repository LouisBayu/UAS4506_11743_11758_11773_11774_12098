package com.udinus.uas4506_11743_11758_11773_11774_12098.Model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

public class SearchResepHolder extends RecyclerView.ViewHolder {

    public TextView nama, kategori, author;
    public ImageView img_resep;

    public SearchResepHolder(View itemView) {
        super(itemView);

        nama = (TextView)itemView.findViewById(R.id.nama_resep_search);
        kategori = (TextView) itemView.findViewById(R.id.kategori_resep_search);
        author = (TextView) itemView.findViewById(R.id.author_resep_search);
        img_resep = (ImageView) itemView.findViewById(R.id.img_resep_search);
    }
}
