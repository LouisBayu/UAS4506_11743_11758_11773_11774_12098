package com.udinus.uas4506_11743_11758_11773_11774_12098.View.FragmentMenu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter.RecommendedAdapter;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.ResepModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.UserModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import java.util.ArrayList;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends Fragment {

    private Handler slideHandler = new Handler();
    private ViewPager2 vpRecommended;
    private View viewHome;
    TextView tvGreeting;
    CircleImageView imgProfil;
    SharedPreferences sharedPreferences;
    FirebaseDatabase db;
    DatabaseReference dbReference;
    StorageReference storageReference;
    Context context;
    ArrayList<ResepModel> arrayRecommended = new ArrayList<>();
    DatabaseReference recommendedRef;
    RecommendedAdapter recommendedAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Binding
        viewHome = inflater.inflate(R.layout.fragment_home,container,false);
        initComponent(viewHome);
        setVpRecommended();
        getResepFromFirebase();
        greeting();

        return viewHome;
    }

    private void initComponent(View view){
        context = getActivity();
        vpRecommended = view.findViewById(R.id.vpRecommended);
        tvGreeting = view.findViewById(R.id.greetingsText);
        imgProfil = view.findViewById(R.id.imgProfil);

        db = FirebaseDatabase.getInstance();
        dbReference = db.getReference("users");
        sharedPreferences = getActivity().getSharedPreferences("appSharedPref", Context.MODE_PRIVATE);
        storageReference = FirebaseStorage.getInstance().getReference();
        recommendedRef = FirebaseDatabase.getInstance().getReference("recommended");
    }


    private void greeting(){
        String email = sharedPreferences.getString("key_email", null);
        Query getUserData = dbReference.orderByChild("email").equalTo(email);

        getUserData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot x : snapshot.getChildren()){
                    UserModel user = x.getValue(UserModel.class);
                    tvGreeting.setText("Halo " + user.getUsername() + ",");
                    loadImgProfile("users/"+user.getUsername()+"-profile.jpg");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadImgProfile(String ref){
        StorageReference imgFileRef = storageReference.child(ref);
        imgFileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(imgProfil);
            }
        });
    }

    private void setVpRecommended(){
        recommendedAdapter = new RecommendedAdapter(arrayRecommended,getActivity(),vpRecommended);
        vpRecommended.setAdapter(recommendedAdapter);
        vpRecommended.setClipToPadding(false);
        vpRecommended.setClipChildren(false);
        vpRecommended.setOffscreenPageLimit(3);
        vpRecommended.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_ALWAYS);

        vpRecommended.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(sliderRunnable);
                slideHandler.postDelayed(sliderRunnable,3500);
            }
        });
    }

    private void getResepFromFirebase(){
        recommendedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayRecommended.clear();
                for (DataSnapshot x : snapshot.getChildren()){
                    ResepModel resep = new ResepModel();
                    resep.setNama(x.child("nama").getValue().toString());
                    resep.setAuthor(x.child("author").getValue().toString());
                    resep.setKategori(x.child("kategori").getValue().toString());
                    resep.setImage(x.child("image").getValue().toString());
                    resep.setBahan((ArrayList<String>) x.child("bahan").getValue());
                    resep.setLangkah((ArrayList<String>) x.child("langkah").getValue());
                    arrayRecommended.add(resep);
                }
                recommendedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            vpRecommended.setCurrentItem(vpRecommended.getCurrentItem()+1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        slideHandler.postDelayed(sliderRunnable,2500);
    }
}