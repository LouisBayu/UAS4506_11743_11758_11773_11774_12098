package com.udinus.uas4506_11743_11758_11773_11774_12098.Activity;

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
import android.os.Handler;
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
import com.google.firebase.storage.StorageTask;
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
        add();

    }

    void add(){
        Resep baksoSapiRica = new Resep();
        baksoSapiRica.setAuthor("kyurise");
        baksoSapiRica.setNama("Bakso Sapi Rica");
        baksoSapiRica.setKategori("Olahan Daging");
        baksoSapiRica.setImage(R.drawable.baksosapirica);
        String[] bahan40 = {
                "12 butir bakso sapi. Sayat menjadi 4 bagian tanpa terputus. Goreng dan sisihkan",
                "1 lembar daun jeruk",
                "1 genggam kemangi",
                "1 batang serai, ikat lalu geprek",
                "1 ruas  jahe, geprek",
                "Bumbu rica-rica (blender kasar) :",
                "3 siung bawang merah",
                "5 siung bawang putih",
                "7 buah cabai merah",
                "3 buah cabai rawit",
                "1 butir kemiri",
                "Garam, gula, penyedap secukupnya",
                "Sedikit air" };
        baksoSapiRica.setBahan(bahan40);
        String[] langkah40 = {
                "Siapkan wajan dengan minyak secukupnya. Tumis bahan rica-rica sampai harum, masukkan serai, jahe, daun jeruk.",
                "Masukkan bakso, aduk-aduk sampai rata, masukkan kemangi, garam, gula, penyedap, kecap manis. Diaduk rata. Kemudian tunggu sampai bumbu meresap. Masukkan sedikit air, masak sampai air menyusut.",
                "Baso rica-rica siap disantap!"};
        baksoSapiRica.setLangkah(langkah40);
        
        Uri img = drawableToUri(baksoSapiRica.getImage());
        if (img != null){
            String ref = baksoSapiRica.getNama() + "-pic.jpg";

            StorageReference imgFileRef = mStorageReference.child(ref);

            imgFileRef.putFile(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    resepModel.setNama(baksoSapiRica.getNama());
                    resepModel.setAuthor(baksoSapiRica.getAuthor());
                    resepModel.setKategori(baksoSapiRica.getKategori());
                    resepModel.setBahan(listToArray(baksoSapiRica.getBahan()));
                    resepModel.setLangkah(listToArray(baksoSapiRica.getLangkah()));
                    resepModel.setImage("gs://uas-ppb4506.appspot.com/resep/"+ref);

                    String key = resepModel.getNama().toLowerCase();
                    mDatabaseReference.child(key).setValue(resepModel);
                    arrayToFirebase(resepModel.getBahan(),key,"bahan");
                    arrayToFirebase(resepModel.getLangkah(),key,"langkah");

                    Toast.makeText(AddResep.this, "Berhasil Upload", Toast.LENGTH_SHORT).show();
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

    private ArrayList<String> listToArray(String[] list){
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i=0; i<list.length;i++){
            arrayList.add(list[i]);
        }
        return arrayList;
    }

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

                    Toast.makeText(AddResep.this, "Berhasil Upload", Toast.LENGTH_SHORT).show();
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