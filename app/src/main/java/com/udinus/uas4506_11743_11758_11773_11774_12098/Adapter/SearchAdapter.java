package com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Activity.SearchList;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<SearchList> listSearch;

    public SearchAdapter(List<SearchList> list) {
        this.listSearch = list;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        SearchList searchList = listSearch.get(position);
        Glide.with(holder.itemView.getContext())
                .load(searchList.getFoto())
                .apply(new RequestOptions().override(1000, 1000))
                .into(holder.imgPhoto);
        holder.txtnama.setText(searchList.getNama());
        holder.txtdetail.setText(searchList.getDetail());
    }

    @Override
    public int getItemCount() {
        return listSearch.size();
    }

    public void filterList(List<SearchList> filteredList){
        listSearch = filteredList;
        notifyDataSetChanged();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView txtnama;
        TextView txtdetail;

        SearchViewHolder(View view) {
            super(view);
            imgPhoto = view.findViewById(R.id.img_item_foto);
            txtnama = view.findViewById(R.id.nama);
            txtdetail = view.findViewById(R.id.detail);
        }
    }

}
