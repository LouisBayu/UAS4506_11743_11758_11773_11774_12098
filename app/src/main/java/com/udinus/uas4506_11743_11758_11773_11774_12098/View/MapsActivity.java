package com.udinus.uas4506_11743_11758_11773_11774_12098.View;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.L;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    GoogleMap mGoogleMap;
    GoogleMap mMap;
    ProgressBar pbMap;
    Spinner spWisata;
    CardView cvWisata;
    String [] sWisata = {"mosque", "restaurant", "atm", "bank", "school"
            , "hospital", "laundry", "university", "post_office", "police"};
    String xWisata;
    ImageView btn_back;
    TextView tvAddress;
    LocationManager locationManager;
    ArrayList<LatLng>arrayList = new ArrayList<LatLng>();
    ArrayList<String> title;
    LatLng idm_menoreh = new LatLng(-7.009117618769958, 110.39381632129647);
    LatLng idm_sampangan1 = new LatLng(-7.009737124893538, 110.39618105890301);
    LatLng idm_sampangan2 = new LatLng(-7.010700722570222, 110.39141941237511);
    LatLng alfa_menoreh1 = new LatLng(-7.008616429644728, 110.39474269518585);
    LatLng alfa_menoreh2 = new LatLng(-7.011662323009147, 110.39060322402129);
    LatLng alfa_menoreh3 = new LatLng(-7.012799966369056, 110.39011789703363);
    LatLng alfa_menoreh4 = new LatLng(-7.013101072226714, 110.39049144121061);
    LatLng alfamidi = new LatLng(-7.0104783683315555, 110.39612441052743);
    LatLng pasar_sampangan = new LatLng(-7.017254410231279, 110.38927186635048);
    LatLng anjay = new LatLng(-7.011821323380349, 110.37711733239024);
    LatLng frozen_food = new LatLng(-7.009453284358874, 110.39326223740622);
    LatLng frozen_food2 = new LatLng(-7.01330810216075, 110.3904107948273);
    LatLng superindo = new LatLng(-7.004417067745987, 110.41327519781119);
    LatLng ada = new LatLng(-6.981828376260548, 110.40431243151394);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fMap);
        mapFragment.getMapAsync(this);
        arrayList.add(idm_menoreh);
        arrayList.add(idm_sampangan1);
        arrayList.add(idm_sampangan2);
        arrayList.add(alfa_menoreh1);
        arrayList.add(alfa_menoreh2);
        setID();
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.statusbarMaps));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spWisata.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object itemDB = parent.getItemAtPosition(pos);
                xWisata = itemDB.toString();
                SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fMap);
                fragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mGoogleMap = googleMap;
                        initMap();
                    }
                });
                startProg();
            }

            public void onNothingSelected(AdapterView<?> parent) { }
        });

    }

    private void setID(){
        pbMap = findViewById(R.id.pbMap);
        spWisata = findViewById(R.id.spWisata);
        cvWisata = findViewById(R.id.cvWisata);
        btn_back = findViewById(R.id.back_btn);
        tvAddress = findViewById(R.id.tvAddress);
        ArrayAdapter<String> adpWisata = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sWisata);
        adpWisata.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWisata.setAdapter(adpWisata);
    }

    private void stopProg() {
        pbMap.setVisibility(View.GONE);
    }

    private void startProg() {
        pbMap.setVisibility(View.VISIBLE);
        spWisata.setVisibility(View.GONE);
        cvWisata.setVisibility(View.GONE);
    }

    private void initMap() {
        onMapReady(mGoogleMap);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 115);
            return;
        }

        if (mGoogleMap != null) {
            startProg();
            mGoogleMap.setMyLocationEnabled(true);

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                onLocationChanged(location);
            } else
                stopProg();
            locationManager.requestLocationUpdates(provider, 20000, 0, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double mLatitude = location.getLatitude();
        double mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(18));

        String sb = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=" + mLatitude + "," + mLongitude +
                "&radius=20000" +
                "&types=" + xWisata +
                "&key=" + "AIzaSyCmHKydROp04bFYKfy1xIWSzGV9YDOpfLo";
        new MapsActivity.PlacesTask().execute(sb);
        stopProg();
        try{
            Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);

            tvAddress.setText(address);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        for (int i=0; i<arrayList.size(); i++){
            mGoogleMap.addMarker(new MarkerOptions().position(arrayList.get(i)).title("Marker"));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(18));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(i)));
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class PlacesTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = null;
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                stopProg();
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            new MapsActivity.ParserTask().execute(result);
        }
    }

    private String downloadUrl(String strUrl) {
        String data = "";
        InputStream iStream;
        HttpURLConnection urlConnection;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();
            iStream.close();
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    @SuppressLint("StaticFieldLeak")
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
        JSONObject jObject;
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {
            List<HashMap<String, String>> places = null;
            ParserPlace parserPlace = new ParserPlace();
            try {
                jObject = new JSONObject(jsonData[0]);
                places = parserPlace.parse(jObject);
            } catch (Exception e) {
                stopProg();
                e.printStackTrace();
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {
            mGoogleMap.clear();
            for (int i = 0; i < list.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> hmPlace = list.get(i);
                BitmapDescriptor pinDrop = BitmapDescriptorFactory.fromResource(R.drawable.ic_location);

                double lat = Double.parseDouble(hmPlace.get("lat"));
                double lng = Double.parseDouble(hmPlace.get("lng"));
                String nama = hmPlace.get("place_name");
                String namaJln = hmPlace.get("vicinity");
                LatLng latLng = new LatLng(lat, lng);

                markerOptions.icon(pinDrop);
                markerOptions.position(latLng);
                markerOptions.title(nama + " : " + namaJln);
                mGoogleMap.addMarker(markerOptions);
            }
            stopProg();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) { }
    @Override
    public void onProviderEnabled(String s) { }
    @Override
    public void onProviderDisabled(String s) { }

}
