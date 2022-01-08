package com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.ResepModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.DetailResep;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdapterResepBookmark extends RecyclerView.Adapter<AdapterResepBookmark.ViewHolder> {

    ArrayList<ResepModel> dataItem;
    Context context;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    SharedPreferences sharedPreferences;
    Boolean favChecker = false;
    String user;
    DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("favorites");

    public AdapterResepBookmark(ArrayList<ResepModel> data, Context context){
        this.dataItem = data;
        this.context = context;
        sharedPreferences = context.getSharedPreferences("appSharedPref", Context.MODE_PRIVATE);
        this.user = this.sharedPreferences.getString("key_username",null);
    }

    @NonNull
    @Override
    public AdapterResepBookmark.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_resep_bookmark, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterResepBookmark.ViewHolder holder, int position) {

        StorageReference imgRef = firebaseStorage.getReferenceFromUrl(dataItem.get(position).getImage());
        final String keyResep = dataItem.get(position).getNama().toLowerCase();
        ResepModel resep = dataItem.get(position);

        TextView text_title = holder.textTitle;
        TextView text_category = holder.textCategory;
        TextView text_author = holder.textAuthor;
        ImageView image_resep = holder.imageResep;
        CardView cardView = holder.cardView;
        ImageView btnBookmark = holder.btnBookmark;

        // Set data to view
        text_title.setText(resep.getNama());
        text_category.setText(resep.getKategori());
        text_author.setText("Oleh : "+resep.getAuthor());

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

        // onclick intent
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

        // onclick button bookmark
        holder.favoriteChecker(keyResep);
        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnBookmark.setImageResource(R.drawable.ic_not_bookmarked);
                favChecker = true;
                favRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (favChecker.equals(true)){
                            onClickBtnBookmark(resep.getNama(), user, btnBookmark);
                            favChecker = false;
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

    private void onClickBtnBookmark(String resep, String username, ImageView btnBookmark) {
        new MaterialAlertDialogBuilder(context)
                .setTitle("Peringatan")
                .setMessage("Anda yakin akan menghapus resep " + resep + " dari bookmark Anda?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Query query = FirebaseDatabase.getInstance().getReference("favoriteList")
                                .child(username)
                                .orderByChild("nama")
                                .equalTo(resep);

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                favRef.child(resep.toLowerCase()).child(user).removeValue();
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
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                btnBookmark.setImageResource(R.drawable.ic_bookmarked);
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                btnBookmark.setImageResource(R.drawable.ic_bookmarked);
            }
        }).show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textCategory, textAuthor;
        ImageView imageResep, btnBookmark;
        CardView cardView;
        DatabaseReference favoriteRef;
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.title_resep);
            textCategory = itemView.findViewById(R.id.category_resep);
            textAuthor = itemView.findViewById(R.id.author_resep_bookmark);
            imageResep = itemView.findViewById(R.id.img_resep);
            cardView = itemView.findViewById(R.id.cardResep);
            btnBookmark = itemView.findViewById(R.id.btn_bookmark);
        }

        public void favoriteChecker(String keyResep) {
            btnBookmark = itemView.findViewById(R.id.btn_bookmark);
            favoriteRef = db.getReference("favorites");

            favoriteRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(keyResep).hasChild(user)){
                        btnBookmark.setImageResource(R.drawable.ic_bookmarked);
                    } else {
                        btnBookmark.setImageResource(R.drawable.ic_not_bookmarked);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}
