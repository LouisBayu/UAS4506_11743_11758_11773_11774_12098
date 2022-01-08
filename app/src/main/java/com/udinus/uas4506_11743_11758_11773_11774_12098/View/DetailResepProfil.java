package com.udinus.uas4506_11743_11758_11773_11774_12098.View;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.util.ArrayList;

import butterknife.ButterKnife;

public class DetailResepProfil extends AppCompatActivity implements  AppBarLayout.OnOffsetChangedListener {

    HeaderView toolbarHeaderView;
    HeaderView floatHeaderView;
    AppBarLayout appBarLayout;
    Toolbar toolbar;
    TextView bahanTV,langkahTV;
    ImageView imageIV;
    FirebaseStorage firebaseStorage;
    Context context;
    FrameLayout btnDelete;

    SharedPreferences sharedPreferences;
    Boolean favChecker = false;
    boolean isHideToolbarView = false;
    String user, keyResep;
    DatabaseReference favRef, favListRef;
    ResepModel resepModel;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_resep_profil);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.bg));
        }
        initComponent();
        getIncomingIntent();
        setOnClickDelete();
    }

    private void initComponent(){
        bahanTV = findViewById(R.id.bahanResep);
        langkahTV = findViewById(R.id.langkahResep);
        imageIV = findViewById(R.id.image);
        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appbar);
        floatHeaderView = findViewById(R.id.float_header_view);
        toolbarHeaderView = findViewById(R.id.toolbar_header_view);
        btnDelete = findViewById(R.id.btn_delete);

        resepModel = new ResepModel();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        context = this;
        sharedPreferences = context.getSharedPreferences("appSharedPref", Context.MODE_PRIVATE);
        user = sharedPreferences.getString("key_username",null);
        keyResep = getIntent().getStringExtra("nama").toLowerCase();

        firebaseStorage = FirebaseStorage.getInstance();
        favRef = FirebaseDatabase.getInstance().getReference("favorites");
        favListRef = FirebaseDatabase.getInstance().getReference("favoriteList").child(user);
        appBarLayout.addOnOffsetChangedListener(this);
    }

    private void setOnClickDelete(){
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaResep = getIntent().getStringExtra("nama");
                System.out.println(namaResep);
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(DetailResepProfil.this);
                builder.setTitle("Peringatan!");
                builder.setMessage("Anda yakin akan menghapus resep " + namaResep + " Anda?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Query query = FirebaseDatabase.getInstance().getReference("resep")
                                .child(namaResep.toLowerCase());

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot x : snapshot.getChildren()){
                                    x.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            StorageReference imgRef = firebaseStorage.getReferenceFromUrl(getIntent().getStringExtra("image"));
                                            DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("favorites").child(namaResep.toLowerCase());
                                            DatabaseReference favListRef = FirebaseDatabase.getInstance().getReference("favoriteList");
                                            imgRef.delete();
                                            favRef.removeValue();
                                            favListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot x : snapshot.getChildren()){
                                                        x.child(namaResep.toLowerCase()).getRef().removeValue();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });



                                            finish();

                                        }
                                    });
                                    Toast.makeText(context, "Resep berhasil dihapus", Toast.LENGTH_SHORT).show();
                                }
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("nama") && getIntent().hasExtra("author") && getIntent().hasExtra("langkah") && getIntent().hasExtra("bahan") && getIntent().hasExtra("image")){
            String nama = getIntent().getStringExtra("nama");
            String author = getIntent().getStringExtra("author");
            String[] bahan = getIntent().getStringArrayExtra("bahan");
            String[] langkah = getIntent().getStringArrayExtra("langkah");
            String image = getIntent().getStringExtra("image");

            setDataDetails(nama,author,bahan,langkah,image);
        }
    }

    private void setDataDetails(String nama, String author, String[] bahan, String[] langkah, String image){
        StorageReference imgRef = firebaseStorage.getReferenceFromUrl(image);

        bahanTV.setText(listBahantoString(bahan));
        langkahTV.setText(listLangkahtoString(langkah));
        toolbarHeaderView.bindTo(nama, author);
        floatHeaderView.bindTo(nama, author);
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .centerCrop()
                        .into(imageIV);
            }
        });
    }

    private String listBahantoString(String[] data){
        String result = "";

        for (int i = 0; i < data.length ; i++){
            if (i < data.length-1){
                result += (data[i] + "\n\n");
            } else {
                result += data[i];
            }
        }

        return result;
    }
    private String listLangkahtoString(String[] data){
        String result = "";

        for (int i = 0; i < data.length ; i++){
            if (i < data.length-1){
                result += ((i+1) + "#  " + data[i] + "\n\n\n");
            } else {
                result += (i+1) + "#  " + data[i];
            }
        }

        return result;
    }

    public void onClickBack(View view){
        finish();
    }

    public void onClickSave(View view){
        Toast.makeText(view.getContext(), "Resep Tersimpan!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.GONE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
            }
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            btnDelete.setVisibility(View.VISIBLE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(this.getResources().getColor(R.color.bg));
            }
            isHideToolbarView = !isHideToolbarView;
        }
    }

    private ArrayList<String> listToArray(String[] list){
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i=0; i<list.length;i++){
            arrayList.add(list[i]);
        }
        return arrayList;
    }
}