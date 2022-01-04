package com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment;

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
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter.ResepAdapter;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.Resep;
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
    ArrayList<Resep> arrayResep = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Binding
        viewHome = inflater.inflate(R.layout.fragment_home,container,false);
        initComponent(viewHome);
        setItemResep();
        setVpRecommended();
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
        vpRecommended.setAdapter(new ResepAdapter(arrayResep,getActivity(),vpRecommended));
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

    private void setItemResep(){
        Resep ayamMentega = new Resep();
        ayamMentega.setAuthor("Luthfi Khakim");
        ayamMentega.setNama("Ayam Goreng Mentega");
        ayamMentega.setKategori("Olahan Daging");
        ayamMentega.setImage(R.drawable.ayammentega);
        String[] bahan1 = {
                "300 gr dada ayam fillet",
                "1 butir telur",
                "Tepung krispi serbaguna, saya pakai sajiku",
                "1/2 bawang bombay",
                "2 sdm mentega",
                "2 sdm saus tiram",
                "8 sdm saus inggris",
                "2 sdm saus tomat",
                "2 sdm kecap manis"};
        ayamMentega.setBahan(bahan1);
        String[] langkah1 = {
                "Potong ayam dengan ukuran sesuai selera",
                "Campurkan ayam dengan 1 butir telur, aduk rata",
                "Masukkan ayam yg sudah terlumuri telur ke dalam tepung",
                "Goreng ayam hingga matang (warna kecoklatan). Tiriskan.",
                "Campurkan seluruh bumbu mentega di wadah mangkok, kecuali bawang bombay dan margarin",
                "Lelehkan 1 sdm margarin untuk menumis, kemudian masukkan bawang bombay yang sudah diiris halus",
                "Masukkan campuran saus, aduk rata. Tambahkan 1 sdm mentega lagi.",
                "Koreksi rasa dengan gula dan garam",
                "Masukkan ayam yang sudah digoreng, aduk rata dengan bumbu sekitar 2 menit."};
        ayamMentega.setLangkah(langkah1);

        Resep ronde = new Resep();
        ronde.setAuthor("Hafid Bastian");
        ronde.setNama("Wedang Ronde");
        ronde.setKategori("Minuman");
        ronde.setImage(R.drawable.ronde);
        String[] bahan2 = {
                "300 gram tepung ketan",
                "1,5 sdm tepung kanji/tapioka",
                "Secukupnya air",
                "Bahan filling :",
                "200 gram kacang tanah di goreng, buang kulitnya lalu di haluskan",
                "Secukupnya gula aren/jawa",
                "Secukupnya gula pasir",
                "Secukupnya garam",
                "6 bongol jahe di bakar lalu di geprak",
                "1 liter air",
                "Secukupnya gula aren/jawa"};
        ronde.setBahan(bahan2);
        String[] langkah2 = {
                "Campur tepung ketan dan tepung kanji, lalu masukan air perlahan uleni sampai bisa di pulung2 (airnya di tuang perlahan ya)",
                "Lalu bagi adonan menjadi 3 bagian dan beri pewarna",
                "Campur semua bahan filling, tes rasa (saya tambahin air sedikit biar gampang di bentuk)",
                "Pipihkan adonan, isi dengan filling",
                "Lalu masak jahe dan gulmer di air tunggu hingga mendidih (tes rasa pedas jahe sesuai selera ya)",
                "Masak adonan onde ke air mendidih, tunggu sampai menggapung (ud mateng)",
                "Siram onde dengan kuah jahe d mangkuk, jdi deh",
                "Note : jangan rendam onde di dalam air jahe (nanti bisa ancur) kalo mau makan baru di masak ya ondenya"};
        ronde.setLangkah(langkah2);

        Resep tongseng = new Resep();
        tongseng.setAuthor("Arsya Phristi");
        tongseng.setNama("Tongseng Daging Sapi");
        tongseng.setKategori("Olahan Daging");
        tongseng.setImage(R.drawable.tongsengsapi);
        String[] bahan3 = {
                "500 gr Daging Sapi",
                "Secukupnya Sayur Kol (rajang sesuai selera)",
                "3 buah Tomat Iris",
                "2 batang Daun Bawang",
                "2 batang Daun Prei",
                "1/2 sashet santan Kara (opsional)",
                "Secukupnya Kecap manis",
                "Secukuonya gula dan garam",
                "1 sdm air asam jawa (opsional)",
                "1 ruas jahe geprek",
                "1 btg sereh geprek",
                "3 lbr daun jeruk",
                "2 lbr daun salam",
                "1 ruas lengkuas geprek",
                "2 bj bunga lawang (opsional)",
                "1 ruas jahe kayu manis (opsional)",
                "6 bj cengkeh (opsional)",
                "5 siung bamer",
                "3 siung baput",
                "1/2 sdm ketumbar bubuk",
                "1/2 sdm merica bubuk",
                "1/2 sdm kunyit bubuk",
                "4 buah Cabe merah besar",
                "4 btr kemiri",
                "1/2 ruas jari pala",
                "1/2 sdt jinten (opsional)",
                "15 biji cabe rawit"};
        tongseng.setBahan(bahan3);
        String[] langkah3 = {
                "Rebus daging setengah matang dengan 2 lbr daun jeruk.. iris² sesuai selera sisihkan",
                "Blender bahan bumbu halus (bamer, baput, ketumbar, merica, kunyit, cabe merah, kemiri, pala, jinten, caber rawit). Tumis tambahkan bumbu cemplung (jahe, sereh, daun jeruk, daun salam, lengkuas, bunga lawang, jahe, kayu manis, cengkeh). Tumis hingga harum.",
                "Masukkan daging yg sudah di iris² aduk² sebentar lalu tambahkan air secukupnya,kecap manis,santan,air asam,gula dan garam masak hingga mendidih",
                "Lalu tambahkan kol,tomat,daun bawang dan prei.masak hingga daging matang dan air sedikit menyusut.koreksi rasa"};
        tongseng.setLangkah(langkah3);

        Resep mujairBakar = new Resep();
        mujairBakar.setAuthor("Noval Nugroho");
        mujairBakar.setNama("Mujair Bakar Kecap");
        mujairBakar.setKategori("Seafood");
        mujairBakar.setImage(R.drawable.mujair);
        String[] bahan4 = {
                "Ikan mujair - 1 ekor / 650 gram",
                "Air jeruk lemon - 1 sdm",
                "Garam - 1 sdt",
                "Kecap manis - 4 sdm",
                "Air asam jawa - 1 sdm",
                "Minyak untuk menumis - 3 sdm",
                "Cabe merah keriting - 4 buah",
                "Bawang merah - 5 siung",
                "Bawang putih - 2 siung",
                "Jahe - 2 cm",
                "Ketumbar sangrai - 2 sdt",
                "Garam - secukupnya",
                "Minyak untuk menumis - 2 sdm"};
        mujairBakar.setBahan(bahan4);
        String[] langkah4 = {
                "Siangi ikan kemudian cuci bersih. Kerat-kerat bagian tubuhnya agar bumbu mudah meresap.",
                "Lumuri ikan dengan air jeruk lemon dan garam. Diamkan selama 10 menit di kulkas.",
                "Panaskan minyak. Tumis bumbu halus (bawang merah, bawang putih, cabe merah,jahe, ketumbar, garam, minyak) hingga harum, beri air asam jawa, aduk hingga merata. Matikan api.",
                "Masukkan kecap manis, aduk hingga merata dan sisihkan.",
                "Oles ikan dengan bumbu dikedua bagian badan ikan.",
                "Bakar/ panggang ikan di atas teflon atau menggunakan oven. Sesekali oles badan ikan dengan sisa bumbu hingga matang.",
                "Sajikan hangat dengan sambal favorit dan lalapan sesuai selera."};
        mujairBakar.setLangkah(langkah4);

        Resep seblak = new Resep();
        seblak.setAuthor("Dhanang Argya");
        seblak.setNama("Seblak Ceker");
        seblak.setKategori("Jajanan");
        seblak.setImage(R.drawable.seblak);
        String[] bahan5 = {
                "250 gram ceker ayam",
                "1 genggam kerupuk bawang, rendam sebentar sampai lunak",
                "2 butir telur ayam dikocok lepas",
                "4 lembar daun caisim, potong sesuai selera",
                "1 sendok makan kecap manis",
                "1/4 sendok teh lada bubuk",
                "1/2 sendok teh gula pasir",
                "1/2 sendok teh penyedap rasa",
                "Garam secukupnya",
                "Air secukupnya",
                "Minyak goreng secukupnya untuk menumis",
                "5 siung bawang merah",
                "3 siung bawang putih",
                "3 cm kencur",
                "5 buah cabai merah keriting",
                "8 buah cabai rawit merah"};
        seblak.setBahan(bahan5);
        String[] langkah5 = {
                "Cuci bersih ceker dengan air mengalir dan potong bagian kukunya.",
                "Lalu rebus ceker hingga matang dan benar-benar lunak. Angkat dan tiriskan.",
                "Panaskan minyak secukupnya dalam wajan untuk menumis. Tumis bumbu halus(bawang merah dan putih, kencur, cabe merah dan rawit) sampai benar-benar harum.",
                "Tuang air secukupnya ke dalam wajan dan aduk rata.",
                "Baru masukkan rebusan ceker ke dalam wajan dan masak hingga kuah mendidih.",
                "Tambahkan kerupuk bawang dan caisim. Masak sebentar hingga daun layu.",
                "Tuang telur kocok dan aduk rata sampai kuah agak mengental.",
                "Beri kecap manis dan bumbui dengan lada bubuk, gula pasir, penyedap rasa, dan garam.",
                "Masak seblak hingga semua bahan matang dan kuah mengental. Jangan lupa koreksi rasanya.",
                "Setelah seblak matang, siapkan mangkuk saji dan tuang seblak ke dalam mangkuk.",
                "Seblak ceker siap disajikan selagi hangat."};
        seblak.setLangkah(langkah5);

        Resep piesusu = new Resep();
        piesusu.setAuthor("Pandu Wijaya");
        piesusu.setNama("Pie Susu Teflon");
        piesusu.setKategori("Kue");
        piesusu.setImage(R.drawable.piesusu);
        String[] bahan6 = {
                "250 gram tepung terigu",
                "100 gram mentega",
                "1 butir telur",
                "1 sendok makan tepung maizena",
                "120 ml susu kental manis",
                "1 sendok makan tepung maizena",
                "1/2 sendok teh vanila bubuk",
                "100 ml air matang",
                "2 butir kuning telur"};
        piesusu.setBahan(bahan6);
        String[] langkah6 = {
                "Untuk membuat kulit pie, campurkan tepung terigu dan mentega. Aduk rata adonan agar bahan tercampur sempurna.",
                "Lalu tambahkan telur dan tepung maizena. Aduk dan uleni adonan lagi adonan sampai benar-benar kalis dan tidak lengket di tangan. Diamkan adonan kulit pie.",
                "Untuk membuat fla, larutkan terlebih dahulu tepung maizena dengan air matang dalam wadah.",
                "Lalu tuang susu kental manis, vanila bubuk, dan kuning telur. Aduk sampai semua bahan untuk fla tercampur.",
                "Siapkan teflon dan olesi dengan margarin.",
                "Kemudian taruh adonan kulit pie dalam teflon dan gilas serta ratakan adonan sesuai bentuk teflon atau seperti mangkuk.",
                "Tusuk-tusuk dasar adonan dengan garpu.",
                "Baru tuang isian fla di atasnya dan ratakan.",
                "Tutup teflon dan masak pie dengan api sangat kecil. Agar kulitnya tidak cepat godong, taruh tatakan di atas kompor agar teflon tidak terlalu menempel api.",
                "Masak pie sampai permukaannya mengeras dan benar-benar matang.",
                "Setelah matang, angkat dan biarkan dingin. Lalu potong pie menjadi 6 bagian.",
                "Pie susu teflon siap disajikan."};
        piesusu.setLangkah(langkah6);

        Resep lodeh = new Resep();
        lodeh.setAuthor("Arsya Phristi");
        lodeh.setNama("Sayur Lodeh");
        lodeh.setKategori("Sayur");
        lodeh.setImage(R.drawable.lodeh);
        String[] bahan7 = {
                "1 ikat kacang panjang potong panjang sesuai selera",
                "1 buah labu siam potong-potong kotak",
                "1 buah jagung manis potong menjadi 4 bagian",
                "1 genggam daun melinjo atau daun so potong sesuai selera",
                "1 buah terong hijau potog-potong sesuai selera",
                "600 ml air santan",
                "2 lembar daun salam",
                "3 cm lengkuas memarkan",
                "5 cabe merah keriting diiris serong",
                "Garam secukupnya",
                "Gula merah secukupnya",
                "Penyedap rasa secukupnya",
                "Minyak goreng secukupnya untuk menumis",
                "5 siung bawang merah",
                "3 siung bawang putih",
                "2 butir kemiri",
                "5 cabai rawit merah",
                "1 sendok teh ketumbar"};
        lodeh.setBahan(bahan7);
        String[] langkah7 = {
                "Cuci berish semua sayuran dan potong-potong sesuai selera.",
                "Panaskan minyak secukupnya untuk menumis. Tumis bumbu halus (bawang merah dan putih, kemiri, rawit merah, ketumbar) sampai harum.",
                "Masukkan lengkuas, daun salam, dan potongan cabai merah keriting. Tumis sampai layu.",
                "Tuang air santan dan tambahkan potongan jagung serta potongan labu siam. Masak sampai kuah mendidih dan sayur setengah matang.",
                "Baru masukkan sisa sayurannya (kacang panjang, daun melinjo, dan terung hijau). Aduk rata.",
                "Bumbui dengan garam, gula merah, dan penyedap rasa secukupnya. Masak sampai semua sayuran matang. Jangan lupa koreksi rasa sampai pas.",
                "Setelah matang, sayur lodeh siap disajikan."};
        lodeh.setLangkah(langkah7);

        Resep eskuwut = new Resep();
        eskuwut.setAuthor("Hafid Bastian");
        eskuwut.setNama("Es Kuwut");
        eskuwut.setKategori("Minuman");
        eskuwut.setImage(R.drawable.eskuwut);
        String[] bahan8 = {
                "1 buah kelapa muda, serut daging kelapanya",
                "1/4 buah melon, serut dagingnya memanjang atau bulat-bulat",
                "2 sendok makan biji selasih yang telah direndam dengan air panas",
                "2 buah jeruk nipis diperas airnya",
                "Air kelapa muda secukupnya",
                "Sirup rasa melon secukupnya",
                "Es batu secukupnya",
                "Irisan jeruk nipis secukupnya"};
        eskuwut.setBahan(bahan8);
        String[] langkah8 = {
                "Siapkan semua bahan yang dibutuhkan untuk membuat es kuwut",
                "Siapkan gelas saji lalu masukkan parutan kelapa dan melon secukupnya",
                "Tambahkan biji selasih dan tuang air kelapa sampai 2/3 tinggi gelas.",
                "Beri air perasan jeruk nipis dan sirup melon sesuai selera.",
                "Baru tambahkan es batu sampai gelas penuh dan irisan jeruk nipis ke dalam gelas",
                "Aduk rata dan es kuwut siap disajikan selagi dingin."};
        eskuwut.setLangkah(langkah8);

        Resep cumiAsamManis = new Resep();
        cumiAsamManis.setAuthor("Eky Marcellino");
        cumiAsamManis.setNama("Cumi Asam Manis");
        cumiAsamManis.setKategori("Seafood");
        cumiAsamManis.setImage(R.drawable.cumiasmas);
        String[] bahan9 = {
                "500 gram cumi-cumi ukuran sedang",
                "Air perasan 1 buah jeruk nipis",
                "1/2 buah bawang bombay diiris tipis panjang",
                "5 siung bawang merah dicincang halus",
                "3 siung bawang putih dicincang halus",
                "2 batang daun bawang diiris serong",
                "6 sendok makan saus tomat",
                "3 sendok makan saus cabai",
                "1 sendok teh kecap manis",
                "1/4 sendok teh lada bubuk",
                "1/4 sendok teh penyedap rasa",
                "1 sendok teh gula pasir atau gula merah",
                "2 cm jahe digeprek",
                "2 cm lengkuas di geprek",
                "Garam secukupnya",
                "Minyak goreng secukupnya untuk menumis",
                "Air secukupnya"};
        cumiAsamManis.setBahan(bahan9);
        String[] langkah9 = {
                "Buang kulit dan tulang cumi-cumi lalu cuci bersihkan dengan air mengalir dan potong-potong cincin.",
                "Lumuri cumi dengan air perasan jeruk nipis dan diamkan selama 10 menit agar amisnya hilang. Baru cuci bersih cumi lagi.",
                "Panaskan minyak secukupnya untuk menumis. Tumis bawang merah, bawang putih,dan bawang bombay sampai layu dan harum.",
                "Tambahkan lengkuas dan jahe. Tumis sebentar.",
                "Masukkan potongan cumi dan tumis sebentar agar tercampur bumbu dan cumi tampak kaku.",
                "Baru tuang sedikit air dan tambahkan saus tomat, saus cabai, kecap manis. Aduk rata.",
                "Bumbui dengan lada bubuk, penyedap rasa, gula, dan garam sesuai selera.",
                "Masak cumi sampai matang dan kuah mengental. Jangan lupa koreksi rasanya.",
                "Ketika mau matang, baru masukkan potongan daun bawang dan masak sebentar sampai layu.",
                "Angkat dan taruh cumi ke dalam piring saji.",
                "Cumi asam manis siap disajikan selagi hangat."};
        cumiAsamManis.setLangkah(langkah9);

        Resep garangAsemAyam = new Resep();
        garangAsemAyam.setAuthor("Noval Nugroho");
        garangAsemAyam.setNama("Garang Asem Ayam");
        garangAsemAyam.setKategori("Olahan Daging");
        garangAsemAyam.setImage(R.drawable.garangasem);
        String[] bahan10 = {
                "500 gram daging ayam bagian manapun, potong-potong sesuai selera",
                "5 buah belimbing wuluh, dipotong kasar",
                "3 buah tomat hijau, potong menjadi 4 bagian",
                "10 buah cabai rawit merah utuh",
                "1 ruas lengkuas, diiris tipis",
                "200 ml air santan",
                "5 lembar daun salam",
                "1 batang serai, potong menjadi 5 bagian",
                "Air secukupnya untuk merebus",
                "Daun pisang secukupnya untuk membungkus",
                "8 siung bawang merah",
                "4 siung bawang putih",
                "1 sendok teh garam",
                "1/4 sendok teh penyedap rasa"};
        garangAsemAyam.setBahan(bahan10);
        String[] langkah10 = {
                "Siapkan semua bahan yang dibutuhkan untuk membuat garang asem ayam.",
                "Cuci bersih daging ayam lalu rebus hingga mendidih dan daging matang. Angkat dan tiriskan daging ayam lalu masukkan ke dalam mangkuk besar.",
                "Kemudian tambahkan bumbu halus, potongan belimbing wuluh, tomat hijau, cabai rawit merah utuh, dan lengkuas.",
                "Tuang air santan ke dalam mangkuk ayam dan aduk-aduk secara merata agar semua bahan dan bumbu tercampur.",
                "Ambil lembaran daun pisang dan lipat salah satu ujungnya lalu masukkan potongan 1 potongan serai dan 1 lembar daun salam.",
                "Masukkan daging ayam dan bahan campurannya yaitu potongan belimbing wuluh, tomat hijau, lengkuas, dan cabai rawit utuh.",
                "Jangan lupa tambahkan kuah santannya lalu bungkus rapat daging ayam dengan daun pisang dan tusuk dengan lidi atau tusuk gigi. Lakukan proses ini sampai semua bahan habis.",
                "Panaskan pengukus dan kukus garang asem ayam dengan api sedang sampai matang sekitar 30 menitan.",
                "Setelah matang, angkat garang asem ayam dan sajikan selagi hangat."};
        garangAsemAyam.setLangkah(langkah10);

        arrayResep.add(ayamMentega);
        arrayResep.add(ronde);
        arrayResep.add(tongseng);
        arrayResep.add(mujairBakar);
        arrayResep.add(seblak);
        arrayResep.add(piesusu);
        arrayResep.add(lodeh);
        arrayResep.add(eskuwut);
        arrayResep.add(cumiAsamManis);
        arrayResep.add(garangAsemAyam);

    }

}