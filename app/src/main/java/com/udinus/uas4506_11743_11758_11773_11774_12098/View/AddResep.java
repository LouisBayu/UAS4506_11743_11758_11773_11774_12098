package com.udinus.uas4506_11743_11758_11773_11774_12098.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.Resep;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.ResepModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import java.util.ArrayList;

public class AddResep extends AppCompatActivity {

    ImageView ivREsep;
    EditText namaResepEditText;
    Spinner spinnerKategori;
    LinearLayout layoutBahan, layoutLangkah;
    Button btnAddBahan, btnAddLangkah;
    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;
    Uri imageUri;
    ResepModel resepModel;
    ArrayList<String> bahan, langkah;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resep);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.bg));
        }
        initComponent();
        addViewBahan();
        addViewLangkah();

    }

//    void add(){
//        Resep eskuwut = new Resep();
//        eskuwut.setAuthor("kyurise");
//        eskuwut.setNama("Es Kuwut");
//        eskuwut.setKategori("Minuman");
//        eskuwut.setImage(R.drawable.eskuwut);
//        String[] bahan8 = {
//                "1 buah kelapa muda, serut daging kelapanya",
//                "1/4 buah melon, serut dagingnya memanjang atau bulat-bulat",
//                "2 sendok makan biji selasih yang telah direndam dengan air panas",
//                "2 buah jeruk nipis diperas airnya",
//                "Air kelapa muda secukupnya",
//                "Sirup rasa melon secukupnya",
//                "Es batu secukupnya",
//                "Irisan jeruk nipis secukupnya"};
//        eskuwut.setBahan(bahan8);
//        String[] langkah8 = {
//                "Siapkan semua bahan yang dibutuhkan untuk membuat es kuwut",
//                "Siapkan gelas saji lalu masukkan parutan kelapa dan melon secukupnya",
//                "Tambahkan biji selasih dan tuang air kelapa sampai 2/3 tinggi gelas.",
//                "Beri air perasan jeruk nipis dan sirup melon sesuai selera.",
//                "Baru tambahkan es batu sampai gelas penuh dan irisan jeruk nipis ke dalam gelas",
//                "Aduk rata dan es kuwut siap disajikan selagi dingin."};
//        eskuwut.setLangkah(langkah8);
//
//        Uri img = drawableToUri(eskuwut.getImage());
//        if (img != null){
//            String ref = eskuwut.getNama() + "-pic.jpg";
//
//            StorageReference imgFileRef = mStorageReference.child(ref);
//
//            imgFileRef.putFile(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    resepModel.setNama(eskuwut.getNama());
//                    resepModel.setAuthor(eskuwut.getAuthor());
//                    resepModel.setKategori(eskuwut.getKategori());
//                    resepModel.setBahan(listToArray(eskuwut.getBahan()));
//                    resepModel.setLangkah(listToArray(eskuwut.getLangkah()));
//                    resepModel.setImage("gs://uas-ppb4506.appspot.com/resep/"+ref);
//
//                    String key = resepModel.getNama().toLowerCase();
//                    mDatabaseReference.child(key).setValue(resepModel);
//                    arrayToFirebase(resepModel.getBahan(),key,"bahan");
//                    arrayToFirebase(resepModel.getLangkah(),key,"langkah");
//
//                    Toast.makeText(AddResep.this, "Berhasil Upload", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(AddResep.this, "Upload Gagal!", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            Toast.makeText(AddResep.this, "Foto Belum Ditambahkan!", Toast.LENGTH_SHORT).show();
//        }
//
//
//
//    }



    private void initComponent(){
        ivREsep = findViewById(R.id.addImage);
        namaResepEditText = findViewById(R.id.namaResepEditText);
        layoutBahan = findViewById(R.id.layout_input_bahan);
        layoutLangkah = findViewById(R.id.layout_input_langkah);
        btnAddBahan = findViewById(R.id.btnAddBahan);
        btnAddLangkah = findViewById(R.id.btnAddLangkah);
        spinnerKategori = findViewById(R.id.spinnerKategori);

        resepModel = new ResepModel();
        bahan = new ArrayList<>();
        langkah = new ArrayList<>();
        sharedPreferences = getSharedPreferences("appSharedPref", Context.MODE_PRIVATE);

        // Custom Spinner
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.kategori, R.layout.spinner_kategori);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerKategori.setAdapter(adapter);

        // Set Onclick
        btnAddBahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addViewBahan();
            }
        });

        btnAddLangkah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addViewLangkah();
            }
        });


        // Firebase Connection
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("resep");
        mStorageReference = FirebaseStorage.getInstance().getReference("resep");


    }

    private ArrayList<String> listToArray(String[] list){
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i=0; i<list.length;i++){
            arrayList.add(list[i]);
        }
        return arrayList;
    }

    private void addViewBahan(){
        final View bahanView = getLayoutInflater().inflate(R.layout.row_add_bahan,null,false);

        ImageView imageClose = (ImageView) bahanView.findViewById(R.id.ivRemoveBahan);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutBahan.removeView(bahanView);
            }
        });

        layoutBahan.addView(bahanView);
    }
    private void addViewLangkah(){
        final View langkahView = getLayoutInflater().inflate(R.layout.row_add_langkah,null,false);

        ImageView imageClose = (ImageView) langkahView.findViewById(R.id.ivRemoveLangkah);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutLangkah.removeView(langkahView);
            }
        });

        layoutLangkah.addView(langkahView);
    }


    public void onClickBack(View view){
        finish();
    }


    public void onClickAddFoto(View view){
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                this.imageUri = data.getData();
                ivREsep.setImageURI(imageUri);
            }
        }
    }

    private void uploadToFirebase(Uri imageUri){
        if (imageUri != null){
            String ref = namaResepEditText.getText().toString() + "-pic.jpg";
            StorageReference imgFileRef = mStorageReference.child(ref);

            imgFileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    resepModel.setNama(namaResepEditText.getText().toString());
                    resepModel.setAuthor(sharedPreferences.getString("key_username",null));
                    resepModel.setKategori(spinnerKategori.getSelectedItem().toString());
                    resepModel.setBahan(bahan);
                    resepModel.setLangkah(langkah);
                    resepModel.setImage("gs://uas-ppb4506.appspot.com/resep/"+ref);

                    String key = resepModel.getNama().toLowerCase();
                    mDatabaseReference.child(key).setValue(resepModel);
                    arrayToFirebase(bahan,key,"bahan");
                    arrayToFirebase(langkah,key,"langkah");

                    Toast.makeText(AddResep.this, "Resep Berhasil Dibagikan", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddResep.this, "Upload Gagal!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(AddResep.this, "Foto Belum Ditambahkan!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickAddResep(View view){
        bahan.clear();
        langkah.clear();
        if (TextUtils.isEmpty(namaResepEditText.getText().toString())
            || !checkValidAndAdd(layoutBahan,bahan,R.id.edtBahan)
            || !checkValidAndAdd(layoutLangkah,langkah,R.id.edtLangkah)){
            Toast.makeText(view.getContext(), "Inputan Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
        } else {
            uploadToFirebase(imageUri);
        }
    }

    private Uri drawableToUri(int rid){
        Resources resources = getResources();
        Uri uri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(rid))
                .appendPath(resources.getResourceTypeName(rid))
                .appendPath(resources.getResourceEntryName(rid))
                .build();
        return uri;
    }

    private boolean checkValidAndAdd(LinearLayout layout, ArrayList<String> list, int rid){
        boolean result = true;
        for (int i=0; i<layout.getChildCount();i++){
            View v = layout.getChildAt(i);
            EditText editText = (EditText) v.findViewById(rid);
            String temp = editText.getText().toString();
            if (!temp.equals(""))
                list.add(temp);
            else {
                result = false;
                list.clear();
                Toast.makeText(AddResep.this, "Inputan Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
                break;
            }

        }
        return result;
    }

    private void arrayToFirebase(ArrayList<String> array, String key, String field){
        int i = 0;
        for (String x : array){
            mDatabaseReference.child(key).child(field).child(Integer.toString(i)).setValue(x);
            i++;
        }
    }


}