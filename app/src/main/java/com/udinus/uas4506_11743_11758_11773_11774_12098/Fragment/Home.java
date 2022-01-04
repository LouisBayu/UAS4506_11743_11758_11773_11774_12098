package com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.udinus.uas4506_11743_11758_11773_11774_12098.Activity.AddResep;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter.ResepAdapter;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Kategori_Tab;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.Resep;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.UserModel;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import java.util.ArrayList;
import com.google.firebase.storage.StorageReference;
import com.udinus.uas4506_11743_11758_11773_11774_12098.ui.main.Frag1;
import com.udinus.uas4506_11743_11758_11773_11774_12098.ui.main.SectionsPagerAdapter;

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
    private View view;


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
        ayamMentega.setAuthor("luthfiakhakim");
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
        ronde.setAuthor("louisbay");
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
        tongseng.setAuthor("arsya");
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
        mujairBakar.setAuthor("novalkris");
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
        seblak.setAuthor("louisbay");
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
        piesusu.setAuthor("novalkris");
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
        lodeh.setAuthor("kyurise");
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
        eskuwut.setAuthor("luthfiakhakim");
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
        cumiAsamManis.setAuthor("louisbay");
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
        garangAsemAyam.setAuthor("arsya");
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

        Resep rendangDaging = new Resep();
        rendangDaging.setAuthor("louisbay");
        rendangDaging.setNama("Rendang Daging");
        rendangDaging.setKategori("Olahan Daging");
        rendangDaging.setImage(R.drawable.rendangdaging);
        String[] bahan11 = {
                "1 kg daging sapi gandik atau paha, potong sesuai selera",
                "2 buah serai, geprek, potong jadi 2 bagian",
                "1 lembar daun kunyit, iris kasar (jika ada)",
                "1 buah asam kandis (ganti dengan 1 sdt asam jawa jika tidak ada)",
                "5 helai daun jeruk purut, buang tulangnya",
                "2 liter santan kental (dari 3 butir kelapa tua)",
                "1,5 sdt garam atau secukupnya",
                "Bumbu Halus :",
                "250 gram cabe merah (buang bijinya kalau tak suka pedas)",
                "10 buah/100 gram bawang merah",
                "5 siung/15 gram bawang putih",
                "25 gram lengkuas segar, kupas",
                "25 gram jahe, kupas"};
        rendangDaging.setBahan(bahan11);
        String[] langkah11 = {
                "Cincang semua bahan bumbu halus kemudian blender sampai halus.",
                "Jika perlu tambahkan sekitar 50 ml santan untuk mempermudah proses penghancuran.",
                "Campur daging dengan bumbu yang dihaluskan, taruh dalam wajan ukuran besar (kapasitas minim 4 liter).",
                "Tuang santan, tambahkan serai, asam kandis, daun jeruk purut dan daun kunyit.",
                "Masak dengan api antara sedang dan besar sampai santan mendidih (kurang lebih 0.5 jam).",
                "Kecilkan api ke posisi sedang, masak selama 1.5 jam dalam keadaan tertutup sampai keluar minyak. Aduk sekali-kali.",
                "Cicipi, tambahkan garam sesuai selera.",
                "Kecilkan lagi api, masak sambil terus diaduk sampai santan mengering dan minyak terserap oleh daging. (Kurang lebih 0.5 jam)",
                "Siap disajikan."};
        rendangDaging.setLangkah(langkah11);

        Resep esCendolDawet = new Resep();
        esCendolDawet.setAuthor("kyurise");
        esCendolDawet.setNama("Es Cendol Dawet");
        esCendolDawet.setKategori("Minuman");
        esCendolDawet.setImage(R.drawable.escendoldawet);
        String[] bahan12 = {
                "120 gram tepung hunkwe",
                "25 gram tepung beras",
                "1 sdt pasta pandan",
                "700 ml air",
                "1 sdt garam",
                "Es batu, secukupnya",
                "Bahan saus gula merah :",
                "300 gram gula merah iris halus",
                "100 gram gula pasir",
                "500 ml air",
                "3 lembar daun pandan",
                "Bahan saus santan :",
                "500 ml santan instan",
                "300 ml air",
                "1 sdt garam",
                "2 lembar daun pandan"};
        esCendolDawet.setBahan(bahan12);
        String[] langkah12 = {
                "Saus Gula: Campur semua bahan kemudian masak hingga mendidih. Angkat dan saring, kemudian dinginkan.",
                "Saus Santan: Campur semua bahan kemudian masak sambil terus diaduk-aduk hingga mendidih. Angkat dan dinginkan.",
                "Campur tepung hunkwe, tepung beras, air, pasta pandan, dan garam. Aduk hingga merata. Masak sambil terus diaduk-aduk hingga adonan meletup-letup dan matang.",
                "Siapkan air matang dalam baskom kemudian beri bongkahan es batu di dalamnya.",
                "Masukkan adonan cendol yang sudah matang tadi ke dalam cetakan cendol. Tekan pelan-pelan cetakannya. Posisikan cetakan cendol di atas baskom sehingga cendol jadi akan langsung masuk ke dalam baskom. Lakukan sampai semua adonan habis.",
                "Ambil cendol jadi dan tiriskan.",
                "Penyajian: Masukkan beberapa sendok cendol (sesuai selera) ke dalam gelas saji. Tuang saus gula merah dan santan secukupnya. Tambahkan es batu.",
                "Siap disajikan."};
        esCendolDawet.setLangkah(langkah12);

        Resep oporAyam = new Resep();
        oporAyam.setAuthor("arsya");
        oporAyam.setNama("Opor Ayam");
        oporAyam.setKategori("Olahan Daging");
        oporAyam.setImage(R.drawable.oporayam);
        String[] bahan13 = {
                "500 gr ayam bagian paha dan dada, potong sesuai selera",
                "1 buah jeruk nipis, ambil airnya",
                "3 buah daun jeruk purut",
                "1 buah serai, geprek",
                "500 ml santan kental",
                "1 sdt garam atau sesuai selera",
                "Merica bubuk secukupnya",
                "20 ml minyak goreng",
                "Bumbu Halus :",
                "6 buah bawang merah, cincang kasar",
                "4 siung bawang putih, cincang kasar",
                "5 buah kemiri, cincang kasar",
                "1 cm/5 gram lengkuas, cincang kasar",
                "2 cm/6 gram kunyit atau 1 sdt kunyit bubuk (optional, jika suka opor warna kuning)",
                "1 sdt ketumbar bubuk",
                "1/4 sdt jintan bubuk"};
        oporAyam.setBahan(bahan13);
        String[] langkah13 = {
                "Lumuri ayam dengan dengan garam dan air jeruk nipis. Sisihkan selama 15 menit.",
                "Haluskan semua bahan bumbu halus dengan blender. Jika perlu tambahkan sedikit minyak.",
                "Panaskan sekitar 5 sdm minyak, goreng ayam sampai setengah matang atau berubah warna. Tiriskan.",
                "Tumis bumbu halus dalam wajan yang agak besar hingga berbau harum.",
                "Tambahkan serai dan daun jeruk purut.",
                "Masukkan santan, masak dengan api sedang sampai mendidih.",
                "Tambahkan garam dan merica bubuk sesuai selera.",
                "Masukkan ayam yang sudah digoreng setengah matang. Masak sampai ayam empuk dan kuah mengental.",
                "Siap disajikan."};
        oporAyam.setLangkah(langkah13);

        Resep cilok = new Resep();
        cilok.setAuthor("luthfiakhakim");
        cilok.setNama("Cilok");
        cilok.setKategori("Jajanan");
        cilok.setImage(R.drawable.cilok);
        String[] bahan14 = {
                "100 gram tepung kanji",
                "100 gram tepung terigu protein sedang",
                "150 ml atau secukupnya, air panas sampai adonan bisa dipulung / bentuk menjadi bulatan",
                "2 batang daun bawang iris halus",
                "Garam, secukupnya",
                "Merica,secukupnya",
                "2 siung bawang putih halus",
                "Bahan sambal kacang :",
                "100 gram kacang tanah goring",
                "2 siung bawang, goreng sampai matang",
                "3 buah cabai merah goring",
                "30 gram gula merah",
                "1 sdt garam",
                "3 sdm kecap manis",
                "150 ml air panas"};
        cilok.setBahan(bahan14);
        String[] langkah14 = {
                "Aduk rata semua bahan sambil menuang air panas sedikit demi sedikit, sambil diaduk menggunakan spatula.",
                "Lakukan terus hingga adonan bisa di bentuk menjadi bulatan.",
                "Bentuk adonan menjadi bulatan-bulatan sesuai selera.",
                "Rebus cilok sampai matang.",
                "Sajikan.",
                "Cara Membuat saus kacang :",
                "Haluskan kacang tanah, bawang putih, cabe merah, gula dan garam.",
                "Setelah halus, tuang air panas sambil diaduk hingga merata.",
                "Didihkan saus tersebut dipanci kecil.",
                "Masukan kecap manis, aduk hingga merata dan kental.",
                "Sajikan dengan cilok."};
        cilok.setLangkah(langkah14);

        Resep kueLapisLegit = new Resep();
        kueLapisLegit.setAuthor("novalkris");
        kueLapisLegit.setNama("Kue Lapis Legit");
        kueLapisLegit.setKategori("Kue");
        kueLapisLegit.setImage(R.drawable.kuelapislegit);
        String[] bahan15 = {
                "20 butir kuning telur",
                "300 gram gula halus",
                "350 gram butter",
                "60 gram tepung terigu protein rending",
                "20 gram susu bubuk",
                "1 sdt bubuk spekuk",
                "3 sdm susu kental manis"};
        kueLapisLegit.setBahan(bahan15);
        String[] langkah15 = {
                "Kocok butter dan gula halus sampai mengembang dengan kecepatan tinggi, kurang lebih 10 menit.",
                "Turunkan kecepatan, kemudian masukan kuning telur satu persatu. Kocok sampai rata kemudian masukan susu kental manis, kocok sampai rata.",
                "Ayak terigu, susu bubuk dan bumbu spekuk, masukan secara bertahap kedalam kocokan telur, aduk balik dengan spatula sampai rata.",
                "Masukan 1 sendok sayuur adonan kedalam Loyang 20 x 20 x 7 cm yang sudah dialasi dengan kertas roti. Ratakan. Oven dengan api bawah suhu 180°C hingga kecoklatan, keluarkan dari oven lalu tekan-tekan permukaannya dengan punggung sendok atau alat penekan kue lapis.",
                "Tuang 1 sendok sayur lagi adonan, ratakan, oven dengan api atas sampai kecoklatan, tekan-tekan lagi permukaannya. Ulangi langkah tersebut hingga adonan habis.",
                "Setelah adonan terakhir, panggang dengan api bawah selama 15 menit sampai matang.",
                "Setelah matang, keluarkan dari oven, dinginkan baru dipotong-potong.",};
        kueLapisLegit.setLangkah(langkah15);

        Resep sayurSop = new Resep();
        sayurSop.setAuthor("novalkris");
        sayurSop.setNama("Sayur Sop");
        sayurSop.setKategori("Sayur");
        sayurSop.setImage(R.drawable.sayursop);
        String[] bahan16 = {
                "200 gr kubis/daun kol, buang tulang daun yang keras, iris-iris kasar",
                "200 gr/2 buah wortel, kupas, iris bulat atau menyerong tipis",
                "200 gr/2 buah kentang, kupas dan iris bentuk dadu 2 cm",
                "2 buah daun bawang, potong sepanjang 1 cm",
                "3 batang daun seledri, iris kasar",
                "1 buah tomat, iris agak tebal (jika suka)",
                "1 sdt bubuk kaldu rasa ayam atau sayuran",
                "1,5 lt air",
                "1 sdt gula pasir atau sesuai selera",
                "Bumbu ulek :",
                "1 siung/3 gr bawang putih",
                "2 buah/10 gr bawang merah, tumbuk kasar",
                "1/2 sdt/20 butir merica butiran atau 1/2 sdt merica bubuk",
                "2 sdt garam atau sesuai selera",
                "Taburan (opsional) :",
                "Bawang merah Goreng"};
        sayurSop.setBahan(bahan16);
        String[] langkah16 = {
                "Kupas dan cuci bersih semua bahan sayuran, potong-potong dan sisihkan.",
                "Ulek merica butir, bawang putih dan sedikit garam hingga halus.",
                "Tambahkan bawang merah, tumbuk kasar dengan ulekan.",
                "Panaskan 1 sdm minyak goreng di dalam panci dengan suhu kecil, tumis bumbu halus dengan api kecil sampai benar2 matang dan berbau harum.",
                "(Tips : Jangan terlalu banyak pakai minyak nanti sop akan berminyak berlebihan. Pastikan bumbu ditumis sampai benar2 matang supaya sop tidak langu dan cemplang).",
                "Tuang 1,5 liter air dan masak dengan suhu sedang sampai air mendidih.",
                "(Catatan : Pada awal perebusan terlihat bumbu mengambang seperti busa, jangan panik. Teruskan memasak nanti lama-lama bumbu matang lalu tenggelam dan kuah kembali bening dan bau langu juga hilang).",
                "Masukkan bahan sayur yang lebih keras dulu yaitu wortel dan kentang, masak sampai sayuran hampir matang.",
                "Tambahkan bubuk kaldu, garam dan gula pasir.",
                "Masukkan kubis/daun kol dan masak sebentar hingga layu tapi tidak kematangan.",
                "Cicipi dan koreksi rasa, tambahkan garam atau gula pasir jika perlu.",
                "Terakhir masukkan irisan daun bawang, seledri dan tomat (jika dipakai). Masak sebentar sampai layu.",
                "Angkat segera dari api. Sajikan panas dengan taburan bawang merah goreng, nasi putih dan sambal kecap.",};
        sayurSop.setLangkah(langkah16);

        Resep esKacangMerah = new Resep();
        esKacangMerah.setAuthor("arsya");
        esKacangMerah.setNama("Es Kacang Merah");
        esKacangMerah.setKategori("Minuman");
        esKacangMerah.setImage(R.drawable.eskacangmerah);
        String[] bahan17 = {
                "1 cup kacang merah kering, rendam semaleman hingga mengembang",
                "500 ml air",
                "1 sdm gula pasir",
                "1 balok kecil gula merah",
                "3 ruas jari kayu manis",
                "1/4 sdt garam",
                "Bahan saus santan :",
                "250 ml santan",
                "1/4 sdt garam",
                "1 sdt tepung maizena, larutkan dengana sedikit air",
                "2 lembar daun pandan, ikat",
                "Bahan pelengkap :",
                "Es batu atau serut, secukupnya",
                "Susu kental manis, secukupnya",
                "Sirup merah atau cocopandan, secukupnya"};
        esKacangMerah.setBahan(bahan17);
        String[] langkah17 = {
                "Tiriskan dan cuci bersih kacang hasil rendaman.",
                "Masukkan kacang ke dalam panci dan tambahkan kayu manis. Masak hingga kacang merah lunak.",
                "Masukkan gula merah, gula pasir, dan garam. Aduk rata dan masak kembali hingga kuah mengental.",
                "Tuangkan larutan maizena. Aduk rata lalu masak hingga kuah mendidih dan mengental. Matikan api dan biarkan dingin.",
                "Saus santan: campurkan semua bahan saus santan, masak dengan api kecil sembari diaduk, angkat dan dinginkan.",
                "Penyajian: Siapkan gelas lalu masukkan 2 sdm kacang merah. Tuang sesuai selera: saus santan, es serut, sirup cocopandan, dan susu kental manis.",
                "Siap disajikan."};
        esKacangMerah.setLangkah(langkah17);

        Resep terangBulan = new Resep();
        terangBulan.setAuthor("luthfiakhakim");
        terangBulan.setNama("Terang bulan");
        terangBulan.setKategori("Jajanan");
        terangBulan.setImage(R.drawable.terangbulan);
        String[] bahan18 = {
                "250 gram tepung terigu",
                "15 gram susu bubuk",
                "1/4 sdt baking powder",
                "50 gram gula halus",
                "1/4 sdt garam",
                "300 ml air",
                "1/4 sdt fermipan",
                "1/4 sdt vanilla extract",
                "3 butir telur",
                "30 gram gula pasir",
                "3/4 sdt sode kue",
                "25 gram margarin (cair)",
                "Topping :",
                "Keju, secukupnya",
                "Susu kental, secukupnya",
                "Margarin, secukupnya"};
        terangBulan.setBahan(bahan18);
        String[] langkah18 = {
                "Campur terigu, susu bubuk, baking powder, gula halus, vanili bubuk, garam, aduk rata.",
                "Tuang air sedikit demi sedikit sambil diaduk rata, tambahkan ragi instan diamkan 30 menit.",
                "Setelah 30 menit, siapkan kocokan telur, gula pasir, kemudian campur ke campuran terigu tadi, aduk rata, kemudian masukan soda kue, dan margarin cair, aduk rata.",
                "Panaskan teflon/bakaran terang bulan dengan api sedang, setelah panas lalu tuang adonan 1/2 bagian dari adonan, apabila sudah mulai berlobang lobang taburi gula pasir, tutup dan biarkan terang bulan masak dan kering.",
                "Angkat terang bulan, lalu olesi dengan margarin dan beri parutan keju dan juga susu kental manis. Lipat jadi dua, potong dan sajikan."};
        terangBulan.setLangkah(langkah18);

        Resep cumiBakarKecap = new Resep();
        cumiBakarKecap.setAuthor("louisbay");
        cumiBakarKecap.setNama("Cumi Bakar Kecap");
        cumiBakarKecap.setKategori("Seafood");
        cumiBakarKecap.setImage(R.drawable.cumibakarkecap);
        String[] bahan19 = {
                "600 gram cumi ukurann besar",
                "2 sdm air jeruk nipis",
                "4 sdm kecap manis",
                "1 sdt air asam jawa, kental",
                "1 sdm margarin",
                "Bumbu Halus :",
                "5 siung bawang putih",
                "3 butir bawang merah",
                "2 sdt ketumbar, sangrai",
                "2 cm kunyit, bakar",
                "1 cm jahe",
                "Sambal Kecap :",
                "5 sdm kecap manis",
                "4 buah cabe rawit merah, iris tipis",
                "4 buah cabe rawit hijau, iris tipis",
                "2 butir bawang merah, iris tipis",
                "1/2 sdt air jeruk nipis"};
        cumiBakarKecap.setBahan(bahan19);
        String[] langkah19 = {
                "Bersihkan cumi-cumi dan buang tintanya. Cuci bersih. Kerat-kerat sepanjang badan cumi, tapi jangan sampai terputus. Lumuri cumi dengan air jeruk nipis lalu diamkan selama 15 menit. Bilas kembali sampai bersih.",
                "Campur bumbu halus dengan kecap dan air asam jawa, aduk hingga rata.",
                "Campurkan dan lumuri seluruh badan cumi dengan campuran bumbu, sambil sedikit diremas-remas. Diamkan di kulkas selama 30 menit agar bumbunya meresap.",
                "Panaskan pan atau wajan datar. Olesi dengan margarin. Taruh cumi dan bakar hingga matang sambil sesekali dioles sisa bumbu dan dibalik. Angkat.",
                "Siap disajikan bersama sambal kecap."};
        cumiBakarKecap.setLangkah(langkah19);

        Resep tumisKangkung = new Resep();
        tumisKangkung.setAuthor("kyurise");
        tumisKangkung.setNama("Tumis Kangkung");
        tumisKangkung.setKategori("Sayur");
        tumisKangkung.setImage(R.drawable.tumiskangkung);
        String[] bahan20 = {
                "250 gr kangkung",
                "5 buah cabe rawit merah, iris tipis",
                "5 buah bawang merah, iris tipis",
                "1 cm lengkuas, geprek, iris tipis",
                "Garam secukupnya"};
        tumisKangkung.setBahan(bahan20);
        String[] langkah20 = {
                "Siangi kangkung, petiki sesuai selera, cuci bersih dan tiriskan.",
                "Tumis bawang merah, bawang putih, cabe rawit dan lengkuas sampai matang dan berbau harum.",
                "Masukkan kangkung, masukkan irisan batang lebih dulu, aduk sebentar sampai layu.",
                "Kemudian masukkan bagian daun, aduk sebentar.",
                "Tambahkan garam sesuai selera. Angkat segera dari api."};
        tumisKangkung.setLangkah(langkah20);

        Resep kueJahe = new Resep();
        kueJahe.setAuthor("luthfiakhakim");
        kueJahe.setNama("Kue Jahe");
        kueJahe.setKategori("Kue");
        kueJahe.setImage(R.drawable.kuejahe);
        String[] bahan21 = {
                "350 gram tepung terigu",
                "100 gram mentega",
                "30 gram susu bubuk",
                "100 ml gula palem",
                "100 ml situp gula aren",
                "1/4 sdt garam",
                "1 butir telur",
                "1/2 sdt baking soda",
                "Rempah :",
                "2 sdt jahe bubuk",
                "1 sdt kayu manis",
                "1/2 sdt pala bubuk",
                "1/2 sdt cengkeh bubuk",
                "Hiasan :",
                "50 gram coklat putih, lelehkan",
                "Plastik segitiga, secukupnya",
                "Cetakan cookies"};
        kueJahe.setBahan(bahan21);
        String[] langkah21 = {
                "Dalam wadah masukan mentega dan gula palem, mixer. Tambahkan syrup gula aren sambil mixer tetap berjalan, lalu telur, rempah-rempah dan garam. Mixer hingga mengembang pucat.",
                "Ayak tepung terigu,baking soda dan susu bubuk, kemudian masukan kedalam adonan mentega, mixer hingga rata saja.",
                "Letakkan dalam selembar bungkus plastik besar atau bungkus dengan plastik wrap. Jika plastik tidak cukup besar, bagi jadi dua adonan. Kemudian simpan dalam freezer selama minimal 1-3 jam. Lebih mudah memipihkan dan mencetak adonan kue yang sudah dingin.",
                "Keluarkan adonan dari kulkas dan buka plastiknya. Taburi alas kerja dengan tepung, serta tangan dan rolling pin. Kemudian ratakan dengan rolling pin hingga setebal 0,5 cm.",
                "Cetak cookies sesuai bentuk yang disukai.",
                "Panaskan oven, setelah itu panggang cookies suhu 160°C selama 25 menit.",
                "Keluarkan cookies dari oven lalu dinginkan suhu ruang. Biarkan cookies mendingin selama 5 menit di atas loyang. Lalu pindahkan ke rak pendingin hingga benar-benar dingin.",
                "Setelah benar-benar dingin, hiasi dengan coklat putih leleh yang telah dimasukan kedalam plastik segitiga.",};
        kueJahe.setLangkah(langkah21);

        Resep esTimunSerut = new Resep();
        esTimunSerut.setAuthor("louisbay");
        esTimunSerut.setNama("Es Timun Serut");
        esTimunSerut.setKategori("Minuman");
        esTimunSerut.setImage(R.drawable.estimunserut);
        String[] bahan22 = {
                "2 buah timun",
                "250 ml sirup melon",
                "1 buah jeruk nipis, peras airnya",
                "800 ml air matang",
                "Es batu, secukupnya"};
        esTimunSerut.setBahan(bahan22);
        String[] langkah22 = {
                "Cuci bersih timun lalu belah dan buang bagian bijinya. Kemudian serut kasar buahnya.",
                "Campur timun serut dengan air jeruk nipis, sirup melon, dan air matang. Aduk rata.",
                "Tuang minuman dalam gelas saji lalu beri es batu secukupnya.",
                "Siap disajikan."};
        esTimunSerut.setLangkah(langkah22);

        Resep telurGulung = new Resep();
        telurGulung.setAuthor("arsya");
        telurGulung.setNama("Telur Gulung");
        telurGulung.setKategori("Jajanan");
        telurGulung.setImage(R.drawable.telurgulung);
        String[] bahan23 = {
                "3 butir telur",
                "50 ml air",
                "1/2 sdm tepung tapioca",
                "Potong sosis jadi 2-6 buah",
                "1/4 sdt garam",
                "1/4 sdt merica bubuk",
                "1/4 sdt kaldu bubuk",
                "10 buah tusuk sate",
                "Pelengkap :",
                "Saus sambal, secukupnya",
                "Mayonnaise, secukupnya"};
        telurGulung.setBahan(bahan23);
        String[] langkah23 = {
                "Tusuk sosis yang sudah dipotong-potong dengan tusuk sate. Kemudian, goreng sebentar saja, sisihkan.",
                "Dalam mangkuk, campur air dengan tepung tapioka aduk rata. Tambahkan telur, air, garam, merica bubuk dan kaldu bubuk. Kocok rata dengan menggunakan garpu.",
                "Siapkan wajan kecil, beri minyak agak banyak panaskan dengan api sedang. Tunggu sampai minyak benar-benar panas.",
                "Masukkan satu sendok sayur adonan telur ke dalam minyak panas, menuangnya dari atas supaya telur menyebar.",
                "Ambil 1 buah sosis, langsung gulung sampai sosis tertutup telur rapikan gulungan telur ditepi wajan. Proses ini harus dilakukan dengan cepat, kalau kelamaan telur tidak akan menempel.",
                "Tiriskan telur gulung yang sudah digoreng diatas tisu, supaya tidak begitu berminyak. Goreng semua sampai adonan habis.",
                "Sajikan dengan saus sambal dan mayonaise."};
        telurGulung.setLangkah(langkah23);

        Resep udangAsamManis = new Resep();
        udangAsamManis.setAuthor("novalkris");
        udangAsamManis.setNama("Udang Asam Manis");
        udangAsamManis.setKategori("Seafood");
        udangAsamManis.setImage(R.drawable.udangasamanis);
        String[] bahan24 = {
                "250 gram udang",
                "1 buah jeruk nipis, ambil air perasannya",
                "1 sdt garam",
                "1/2 buah bawang bombay cincang",
                "1 batang daun bawang, iris halus",
                "3 sdm butter / margarin, untuk menumis",
                "Bumbu :",
                "2 sdm saus tomat",
                "1 sdm saus sambal",
                "2 sdm kecap manis",
                "1 sdm perasan air jeruk nipis",
                "1 sdt garam",
                "1 sdm gula",
                "1 sdt kaldu bubuk"};
        udangAsamManis.setBahan(bahan24);
        String[] langkah24 = {
                "Bersihkan udang, buang kepalanya lalu kerat punggungnya dan keluarkan kotorannya. Cuci udang di bawah air mengalir. Taruh udang dalam wadah, kucuri dengan air perasan jeruk nipis, dan taburi dengan garam. Sisihkan.",
                "Dalam wadah, campur semua bumbu lalu aduk hingga rata. Sisihkan.",
                "Panaskan 1 sdm butter lalu tumis udang hingga setengah matang. Angkat dan sisihkan.",
                "Panaskan 2 sdm butter. Tumis bawang bombay hingga harum.",
                "Masukkan campuran bumbu. Masak hingga bumbu mengental. Koreksi rasanya.",
                "Masukkan udang dan daun bawang. Aduk-aduk dan masak sebentar. Angkat.",
                "Siap disajikan."};
        udangAsamManis.setLangkah(langkah24);

        Resep tumisKerang = new Resep();
        tumisKerang.setAuthor("kyurise");
        tumisKerang.setNama("Tumis Kerang Saus Tiram");
        tumisKerang.setKategori("Seafood");
        tumisKerang.setImage(R.drawable.tumiskerang);
        String[] bahan25 = {
                "Merebus Kerang :",
                "500 gram kerang hijau (dengan cangkang), cuci bersih",
                "2 siung bawang putih, memarkan",
                "2 cm jahe, memarkan",
                "2 lembar daun salam",
                "1/2 sdt garam",
                "1 liter air untuk merebus",
                "Bumbu Halus :",
                "5 buah cabe merah",
                "5 siung bawang putih",
                "3 siung bawang putih",
                "2 cm jahe",
                "1 buah tomat merah",
                "Lainnya :",
                "1 buah bawnag bombay, iris panjang",
                "10 buah cabe hijau, iris serong",
                "2 sdn saus tiram",
                "1/2 sdt garam",
                "1/2 sdt gula",
                "1/4 sdt kaldu bubuk",
                "1/4 sdt merica",
                "4 sdm minyak untuk menumis",};
        tumisKerang.setBahan(bahan25);
        String[] langkah25 = {
                "Didihkan air, masukkan kerang dan semua bahan-bahan rebusan. Aduk sebentar, masak hingga cangkangnya terbuka. Tiriskan. Sisihkan.",
                "Panaskan minyak. Tumis bawang bombay dan cabe rawit hingga harum.",
                "Masukkan bumbu halus, aduk rata.",
                "Masukkan semua bahan-bahan lainnya. Aduk rata dan masak sebentar. Koreksi rasanya.",
                "Masukkan kerang, aduk hingga kerang terlumuri dengan bumbu, terutama bagian dagingnya. Matikan api.",
                "Siap disajikan."};
        tumisKerang.setLangkah(langkah25);

        Resep kleponUbiUngu = new Resep();
        kleponUbiUngu.setAuthor("kyurise");
        kleponUbiUngu.setNama("Klepon Ubi Ungu");
        kleponUbiUngu.setKategori("Jajanan");
        kleponUbiUngu.setImage(R.drawable.kleponubiungu);
        String[] bahan26 = {
                "150 gram tepung ketan putih",
                "200 gram ubi ungu",
                "1/2 sdt garam halus",
                "175 ml air (sampai adonan bisa dipulung)",
                "Sedikit air untuk merebus",
                "1 lembar daun pandan",
                "Isi :",
                "75 gram gula merah (secukupnya) potong kecil untuk isian",
                "Bahan taburan (kukus selama 15) :",
                "1/2 butir kelapa, parut bagian putihnya",
                "1/4 sdt garam halus",
                "1 lembar daun pandan"};
        kleponUbiUngu.setBahan(bahan26);
        String[] langkah26 = {
                "Kupas kulit ubi ungu, cuci bersih, potong jadi dua bagian, kukus hingga matang.",
                "Haluskan selagi panas dg bantuan garpu atau ulekan.",
                "Campur tepung ketan dan garam lalu aduk rata.",
                "Tambahkan ubi ungu. Uleni hingga tercampur.",
                "Tambahkan air sedikit sedikit sampai adonan dapat dipulung.",
                "Didihkan air dan daun pandan dalam panci.",
                "Sementara itu ambil sedikit adonan sekitar @20gr, pipihkan adonan, beri isi gula merah dan bulatkan.",
                "Langsung masukkan ke air mendidih. Kalau mengapung segera diangkat, tiriskan dan gulingkan ke parutan kelapa. Siap disajikan."};
        kleponUbiUngu.setLangkah(langkah26);

        Resep greenteaCheeseCream = new Resep();
        greenteaCheeseCream.setAuthor("novalkris");
        greenteaCheeseCream.setNama("Greentea Cheese Cream");
        greenteaCheeseCream.setKategori("Minuman");
        greenteaCheeseCream.setImage(R.drawable.greentea);
        String[] bahan27 = {
                "3 sdm greentea",
                "2 bungkus SKM putih",
                "1/2 batang cheese cream",
                "1/4 cup kecil whip cream cair",
                "Susu cair full cream, secukupnya",
                "Gula, secukupnya",
                "Es batu, secukupnya"};
        greenteaCheeseCream.setBahan(bahan27);
        String[] langkah27 = {
                "Rebus green thaitea dalam ± 3 gelas air, tunggu sampai mendidih. Saring, buang ampasnya.",
                "Dilain wadah masukan cheese cream 1/2 batang, tambahkan gula ± 4 sdm aduk rata.",
                "Masukan whip cream sampai mengental, lalu tambahkan susu cair full cream (usahakan jangan sampai tekstur terlalu cair)",
                "Tuang thaitea ke gelas cup ±1/2 gelas tambahkan skm secukupnya. Beri es batu, dan masukkan adonan cheese cream."};
        greenteaCheeseCream.setLangkah(langkah27);

        Resep kueBalok = new Resep();
        kueBalok.setAuthor("louisbay");
        kueBalok.setNama("Kue Balok");
        kueBalok.setKategori("Jajanan");
        kueBalok.setImage(R.drawable.kuebalok);
        String[] bahan28 = {
                "150 gram terigu protein sedang",
                "20 gram coklat bubuk",
                "1 sdt baking powder",
                "3 butir telur",
                "50 gram gula",
                "100 dark cooking chocolate",
                "80 gram margarin",
                "100 ml susu cair"};
        kueBalok.setBahan(bahan28);
        String[] langkah28 = {
                "Dalam wadah, campur semua bahan kering, terigu,coklat bubuk,baking powder. sisihkan.",
                "Potong kecil-kecil dark cooking chocolate, campur dengan margarin. panaskan sebentar dengan api kecil sampai sebagian coklat mencair matikan api,aduk terus coklt dan margarin sampai meleleh sempurna.",
                "Kocok telur dan gula menggunakan mixer atau whisk sampai gula larut, masukkan coklat resrdan margarin yang sudah dilelehkan,kocok sampai rata.",
                "Masukkan campuran tepung, coklat bubuk dan baking powder secara bertahap dengan cara diayak. Aduk balik dengan menggunakan spatula.",
                "Masukkan susu cair, aduk sampai rata.",
                "Masukkan adonan ke dalam cetakan kue balok, oven dengan suhu 180ºC selama 10 menit.",
                "Kue balok siap disajikan."};
        kueBalok.setLangkah(langkah28);

        Resep supIkanPatin = new Resep();
        supIkanPatin.setAuthor("arsya");
        supIkanPatin.setNama("Sup Ikan Patin Bening");
        supIkanPatin.setKategori("Seafood");
        supIkanPatin.setImage(R.drawable.supikanpatin);
        String[] bahan29 = {
                "4 potong ikan patin",
                "1 buah jeruk nipis, ambil airnya",
                "1/2 sdt garam",
                "1/4 sdt merica",
                "Minyak untuk menumis, secukupnya",
                "5 siung bawang merah iris",
                "2 siung bawang merah putih iris",
                "50 gram bawang bombay, potong besar-besar",
                "2 cm jahe, iris",
                "3 cm lengkuas, memarkan",
                "1 batang serai, memarkan",
                "4 lembar daun salam",
                "1 sdt saus tiram",
                "600 ml air",
                "50 gram wortel, potong panjang",
                "50 gra, bunga kol, potong-potong",
                "1/2 sdt garam",
                "1/2 sdt gula",
                "1/4 sdt merica",
                "4 buah tomat hijau, belah empat",
                "1 bauh cabe merah, belah dua, bunga bijinya lalu potong serong",
                "5 buah cabe rawit merah",
                "5 buah cabe rawit  hijau",
                "1 batang daun bawang, potong 1 cm",
                "1 ikat daun seledri, ikat",
                "1 ikat daun kemangi"};
        supIkanPatin.setBahan(bahan29);
        String[] langkah29 = {
                "Dalam wadah, lumuri ikan patin dengan air jeruk nipis, garam, dan merica. Diamkan selama kurang lebih 15-20 menit.",
                "Panaskan minyak, tumis bawang merah, bawang putih, bawang bombay, jahe, lengkuas, serai, dan daun salam, hingga wangi.",
                "Masukkan saus tiram, aduk sebentar.",
                "Tuang air lalu aduk dan masak hingga mendidih.",
                "Masukkan ikan beserta air jeruk bekas lumurannya.",
                "Masukkan wortel dan bunga kol. Masak hingga sayurannya agak matang dan empuk.",
                "Masukkan garam, gula, dan merica. Koreksi rasanya.",
                "Terakhir masukkan semua bahan-bahan yang tersisa. Aduk perlahan lalu masak sampai tomat agak layu. Angkat.",
                "Siap disajikan."};
        supIkanPatin.setLangkah(langkah29);

        Resep ayamKecap = new Resep();
        ayamKecap.setAuthor("luthfiakhakim");
        ayamKecap.setNama("Ayam Kecap");
        ayamKecap.setKategori("Olahan Daging");
        ayamKecap.setImage(R.drawable.ayamkecap);
        String[] bahan30 = {
                "500 gr filet dada ayam, potong tipis memanjang",
                "5 sdm kecap manis",
                "1 cm jahe, iris tipis, geprak",
                "2 batang daun bawang, iris menyerong 1 cm",
                "1-2 buah tomat merah, potong-potong",
                "1/2 sdt merica bubuk",
                "100 ml air",
                "garam sesuai selera",
                "mentega atau minyak goreng secukupnya untuk menggoreng dan menumis",
                "Bumbu Iris atau Cincang :",
                "2 buah cabe merah, iris kasar (buang biji jika tak suka pedas)",
                "1 buah bawang bombay, iris melintang kasar",
                "2 siung bawang putih, geprak, cincang halus"};
        ayamKecap.setBahan(bahan30);
        String[] langkah30 = {
                "Panaskan 3 sdm mentega atau minyak goreng dengan suhu sedang, goreng filet ayam setengah matang saja/tidak terlalu kering. Tiriskan.",
                "Siapkan bumbu iris dan cincang. Sisihkan.",
                "Panaskan kembali 2 sdm mentega atau minyak goreng, tumis jahe dan bawang putih terlebih dulu hingga harum.",
                "Masukkan bawang bombay dan cabe, tumis sambil diaduk-aduk hingga bumbu layu dan matang. (Tambahkan sedikit garam segera jika bau pedas menyengat supaya anda tidak tersedak).",
                "Masukkan filet ayam goreng. Tambahkan kecap manis, air dan merica bubuk.",
                "Tambahkan garam sesuai selera, cicipi hingga rasanya pas. Masak dengan api sedang hingga kuah mengental.",
                "Terakhir tambahkan tomat dan irisan daun bawang. Masak sebentar hingga sayuran layu. Angkat segera dari api."};
        ayamKecap.setLangkah(langkah30);

        Resep capCay = new Resep();
        capCay.setAuthor("kyurise");
        capCay.setNama("Cap Cay");
        capCay.setKategori("Sayur");
        capCay.setImage(R.drawable.capcay);
        String[] bahan31 = {
                "300 gr fillet ayam, potong2 kotak",
                "200 gr bunga kol, potong2",
                "100 gr kacang kapri manis, siangi",
                "2 bh wortel, kupas, iris menyerong tipis",
                "2 bh daun bawang, potong menyerong",
                "1 1/2 sdt bubuk kaldu ayam",
                "1/2 sdt merica bubuk",
                "750 ml air panas",
                "2 sdt garam atau sesuai selera",
                "1 sdt gula pasir atau sesuai selera",
                "2 sdm tepung maizena, larutkan dgn 3 sdm air",
                "Bumbu cincang halus :",
                "1 cm jahe, kupas, cincang halus",
                "2 siung bawang putih, geprek, cincang halus",
                "Taburan (Opsional) :",
                "Bawang merah goreng"};
        capCay.setBahan(bahan31);
        String[] langkah31 = {
                "Panaskan 1 sdm margarin dalam wajan cekung, tumis bumbu cincang sampai berbau harum.",
                "Masukkan ayam, tumis sambil diaduk-aduk hingga ayam berubah warna.",
                "Tambahkan air panas, garam, gula, merica bubuk dan kaldu bubuk.",
                "Masak dengan api sedang sampai air mendidih.",
                "Masukkan wortel, kacang kapri dan bunga kol. Masak sampai sayuran matang.",
                "Berikutnya masukkan irisan daun bawang. Masak sebentar hingga daun bawang layu.",
                "Cicipi, jika perlu tambahkan garam dan gula sesuai selera.",
                "Terakhir masukkan larutan tepung maizena, aduk hingga kuah agak mengental. Angkat dari api.",
                "Hidangkan panas dengan taburan bawang merah goreng."};
        capCay.setLangkah(langkah31);

        Resep lavaCake = new Resep();
        lavaCake.setAuthor("novalkris");
        lavaCake.setNama("Chocolate Lava Cake");
        lavaCake.setKategori("Kue");
        lavaCake.setImage(R.drawable.lavacake);
        String[] bahan32 = {
                "150 gram coklat masak atau dark cooking chocolate",
                "125 gram gula pasir",
                "75 gram terigu protein sedang",
                "75 gram butter unsalted",
                "4 butir telur",
                "1/4 sdt garam",
                "1/2 sdt baking powder"};
        lavaCake.setBahan(bahan32);
        String[] langkah32 = {
                "Lelehkan dark cooking chocolate dan butter dengan cara di tim. Sisihkan dan dinginkan.",
                "Nyalahakan oven dengan suhu 160°C.",
                "Campur dalam wadah, telur dan gula. Kocok dengan whisker (pengocok) sampai gula tercampur rata.",
                "Tambahkan terigu, garam, dan baking powder sambil diayak. Aduk hingga rata.",
                "Terakhir masukan lelehan coklat. Aduk kembali hingga rata.",
                "Siapkan cetakan muffin atau mangkok alumunium foil. Olesi dengan margarin dan taburi tipis dengan terigu. Tuang adonan ke dalam cetakan.",
                "Masukan ke dalam Loyang dan panggang selama 5 menit."};
        lavaCake.setLangkah(langkah32);

        Resep ikanBandengGulai = new Resep();
        ikanBandengGulai.setAuthor("louisbay");
        ikanBandengGulai.setNama("Ikan Bandeng Gulai");
        ikanBandengGulai.setKategori("Seafood");
        ikanBandengGulai.setImage(R.drawable.bandengulai);
        String[] bahan33 = {
                "1 ekor ikan bandeng ukuran bsar @500 gram",
                "1 sdm air jeruk nipis",
                "400 ml santan encer",
                "3/4 sdt garam",
                "1/2 sdt kaldu bubuk",
                "1/2 sdt gula pasir",
                "3 sdm minyak goring",
                "Bumbu Halus :",
                "6 siung bawang merah",
                "3 siung bawang putih",
                "2 buah cabe keriting",
                "3 butir kemiri",
                "1 cm jahe",
                "3cm kunyit",
                "Bumbu Pelengkap :",
                "2 cm Lengkuah, memarkan",
                "1 batang serai, memarkan",
                "3 lembar daun jeruk, buang tulangnya"};
        ikanBandengGulai.setBahan(bahan33);
        String[] langkah33 = {
                "Bersihkan ikan bandeng, buang sisik, insang, dan isi perutnya. Patahkan tulang ekornya, kemudian potong menjadi 4 bagian, cuci bersih.",
                "Lumuri ikan dengan air jeruk nipis, biarkan beberapa saat, lalu bilas sampai bersih, sisihkan.",
                "Panaskan minyak, tumis bumbu halus dan bumbu pelengkap sampai harum. Masukkan santan encer, masak sambil terus diaduk sampai mendidih.",
                "Masukkan ikan bandeng, beri garam, gula, dan kaldu bubuk. Masak dengan api kecil sampai ikan matang.",
                "Masukkan santan kental, masak sampai mendidih sambil diaduk perlahan. Koreksi rasanya, jika sudah pas angkat. Siap disajikan."};
        ikanBandengGulai.setLangkah(langkah33);

        Resep corndog = new Resep();
        corndog.setAuthor("arsya");
        corndog.setNama("Corndog");
        corndog.setKategori("Jajanan");
        corndog.setImage(R.drawable.corndog);
        String[] bahan34 = {
                "150 gram tepung terigu",
                "100 gram jagung pipil, haluskan",
                "8 buah sosis",
                "1 butir telur",
                "80 ml susu tawar cair",
                "1/2 sdt garam",
                "2 sdt gula pasir",
                "2 sdt baking powder",
                "Tusuk sate, secukupnya",
                "Minyak goring, secukupnya"};
        corndog.setBahan(bahan34);
        String[] langkah34 = {
                "Tusuk sosis dengan tusuk sate. Sisihkan.",
                "Dalam wadah, campurkan tepung terigu, jagung halus, telur, susu cair, garam, gula, dan baking powder. Aduk hingga rata dan jadi adonan kental.",
                "Pindahkan adonan dalam wadah yang tinggi dan ramping, seperti gelas atau botol. Sisihkan.",
                "Panaskan banyak minyak dengan api sedang-kecil.",
                "Ambil 1 tusuk sosis lalu celupkan dalam gelas berisi adonan, putar perlahan sampai seluruh permukaan sosis tertutup adonan dengan tebal.",
                "Goreng corndog dalam minyak panas hingga matang dan berwarna kecoklatan. Angkat dan tiriskan.",
                "Siap disajikan dengan saus tomat/sambal dan mustard."};
        corndog.setLangkah(langkah34);

        Resep tehJaheLemon = new Resep();
        tehJaheLemon.setAuthor("luthfiakhakim");
        tehJaheLemon.setNama("Teh Jahe Lemon");
        tehJaheLemon.setKategori("Minuman");
        tehJaheLemon.setImage(R.drawable.tehjahelemon);
        String[] bahan35 = {
                "25 gram teh tubruk hitam",
                "500 ml air",
                "5 sentimeter jahe bakar yang dimemarkan",
                "1 buah lemon iris bulat bulat",
                "Bahan pelengkap :",
                "Irisan buah lemon",
                "50 gram gula batu"};
        tehJaheLemon.setBahan(bahan35);
        String[] langkah35 = {
                "Siapkan panci berisi air, panaskan air dan masukkan jahe. Kemudian masak di atas api kecil hingga harum, matikan api kompor. Baca juga: Resep Kopi Arab dengan Campuran Rempah untuk Tambah Stamina.",
                "Tambahkan teh tubruk, seduh sampai air berubah warna. Tambahkan irisan lemon dan diamkan 20 menit hingga sari lemonnya keluar.",
                "Jika sudah 20 menit, keluarkan irisan lemon. Panaskan kembali air teh jahe. Teh Jahe siap disajikan. Tambahkan gula jika suka."};
        tehJaheLemon.setLangkah(langkah35);

        Resep muffinCoklat = new Resep();
        muffinCoklat.setAuthor("louisbay");
        muffinCoklat.setNama("Muffin Coklat");
        muffinCoklat.setKategori("Kue");
        muffinCoklat.setImage(R.drawable.muffincoklat);
        String[] bahan36 = {
                "85 gram tepung terigu",
                "15 gram coklat bubuk",
                "1/4 sdt baking soda",
                "1/2 sdt baking powder",
                "1/8 sdt garam halus",
                "60 gram unsalted butter, diamkan suhu ruang",
                "65 gram gula kastor",
                "1/2 sdt vanilla extract",
                "1 butir telur, kocok lepas",
                "80 ml susu cair",
                "30 gram chocochips",
                "Chocochips untuk taburan, secukupnya"};
        muffinCoklat.setBahan(bahan36);
        String[] langkah36 = {
                "Panaskan oven dengan suhu 180°C.",
                "Di dalam wadah, ayak rata tepung terigu, cokelat bubuk, baking soda, baking powder, dan garam. Sisihkan.",
                "Aduk butter dengan hand whisk (pengocok tangan) hingga lembut.",
                "Tambahkan gula kastor, aduk rata.",
                "Masukkan telur dan vanilla extract, aduk hingga tercampur rata.",
                "Masukkan susu cair, aduk rata.",
                "Tuang campuran tepung sambil diaduk perlahan menggunakan hand whisk. Jangan terlalu banyak mengaduk, jika masih ada tepung yang bergerindil biarkan saja, agar muffin tidak bantet. Terakhir masukkan chocochips, aduk asal rata.",
                "Tuang adonan kedalam cup muffin sampai 3/4 tinggi cetakan, tabur dengan chocochips.",
                "Oven hingga matang selama 30 menit atau sesuaikan dengan oven masing-masing. Sajikan."};
        muffinCoklat.setLangkah(langkah36);

        Resep sayurBayam = new Resep();
        sayurBayam.setAuthor("arsya");
        sayurBayam.setNama("Sayur Bayam");
        sayurBayam.setKategori("Sayur");
        sayurBayam.setImage(R.drawable.sayurbayam);
        String[] bahan37 = {
                "1 ikat bayam yang sudah dbersihkan",
                "1 buah jagung manis, potong-potong",
                "2 siung bawang merah, iris",
                "3 cm kunci, memarkan",
                "1 buah tomat merah, belah memanjang",
                "1 sdt garam",
                "1 sdt gula pasir",
                "700 ml air"};
        sayurBayam.setBahan(bahan37);
        String[] langkah37 = {
                "Didihkan air.",
                "Masukan bawang merah dan kunci. Rebus sampai wangi.",
                "Masukan jagung, masak hingga jagung empuk.",
                "Tambahkan tomat, garam, dan gula pasir. Biarkan mendidih.",
                "Masukan bayam lalu masak sampai matang.",
                "Siap disajikan."};
        sayurBayam.setLangkah(langkah37);

        Resep sateSapi = new Resep();
        sateSapi.setAuthor("novalkris");
        sateSapi.setNama("Sate Sapi");
        sateSapi.setKategori("Olahan Daging");
        sateSapi.setImage(R.drawable.satesapi);
        String[] bahan38 = {
                "300 gram daging sapi",
                "50 gram lemak sapi",
                "3 sdm kecap manis",
                "1 sdt gula merah",
                "1 sdm air asam jawa",
                "1/2 sdt garam",
                "Tusuk sate secukupnya",
                "Bumbu halus :",
                "4 butir  bawang merah",
                "3 siung bawang putih",
                "1 sdt ketumbar",
                "1 cm lengkuas",
                "1 cm kencur",
                "Sambel kecap :",
                "10 sdm kecap manis",
                "4 butir bawang merah, iris",
                "1 buah tomat, potong dadu kecil",
                "50 gram cabe rawit, iris"};
        sateSapi.setBahan(bahan38);
        String[] langkah38 = {
                "Dalam wadah, campur semua bahan sambel kecap hingga rata, sisihkan.",
                "Potong daging dan lemak sapi dengan ukuran kecil.",
                "Campur daging dengan bumbu halus, kecap manis, gula merah, air asam jawa, dan garam. Aduk rata kemudian diamkan selama 30 menit agar bumbu meresap.",
                "Ambil tusuk sate. Tusukkan daging dan lemak secara berseling. Susun 4-5 potongan per tusuknya.",
                "Bakar sate hingga matang sambil sesekali dioles dengan sisa bumbu rendaman. Angkat.",
                "Siap disajikan bersama sambal kecapnya."};
        sateSapi.setLangkah(langkah38);

        Resep tumisBrokoli = new Resep();
        tumisBrokoli.setAuthor("luthfiakhakim");
        tumisBrokoli.setNama("Tumis Brokoli");
        tumisBrokoli.setKategori("Sayur");
        tumisBrokoli.setImage(R.drawable.tumisbrokoli);
        String[] bahan39 = {
                "4 buah bonggol brokoli potong",
                "5 butir bawang putih iris",
                "1,5 sdm saus tiram",
                "1 sdm kecap manis",
                "1 sdt merica",
                "Air secukupnya"};
        tumisBrokoli.setBahan(bahan39);
        String[] langkah39 = {
                "Tumis bawang putih hingga harum.",
                "Masukkan brokoli, aduk, tunggu sedikit layu.",
                "Tambahkan air secukupnya.",
                "Masukkan saus tiram dan kecap, aduk rata. Tes rasa dan sajikan."};
        tumisBrokoli.setLangkah(langkah39);

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
        arrayResep.add(rendangDaging);
        arrayResep.add(esCendolDawet);
        arrayResep.add(oporAyam);
        arrayResep.add(cilok);
        arrayResep.add(kueLapisLegit);
        arrayResep.add(sayurSop);
        arrayResep.add(esKacangMerah);
        arrayResep.add(terangBulan);
        arrayResep.add(cumiBakarKecap);
        arrayResep.add(tumisKangkung);
        arrayResep.add(kueJahe);
        arrayResep.add(esTimunSerut);
        arrayResep.add(telurGulung);
        arrayResep.add(udangAsamManis);
        arrayResep.add(tumisKerang);
        arrayResep.add(kleponUbiUngu);
        arrayResep.add(greenteaCheeseCream);
        arrayResep.add(kueBalok);
        arrayResep.add(supIkanPatin);
        arrayResep.add(ayamKecap);
        arrayResep.add(capCay);
        arrayResep.add(lavaCake);
        arrayResep.add(ikanBandengGulai);
        arrayResep.add(corndog);
        arrayResep.add(tehJaheLemon);
        arrayResep.add(muffinCoklat);
        arrayResep.add(sayurBayam);
        arrayResep.add(sateSapi);
        arrayResep.add(tumisBrokoli);
        arrayResep.add(baksoSapiRica);


    }

}