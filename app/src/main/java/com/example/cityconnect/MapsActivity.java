package com.example.cityconnect;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Dialog.DialogListener {

    private GoogleMap mMap;
    //public static FirebaseFirestore db;

    //Δεδομένα που θα χρειαστούμε για την βάση
    private String title, description;
    private LatLng latLng;
    private String name, surname, id, email;
    private String status;
    private Date date;
    //city (από location)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        findViewById(R.id.report_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog();
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        findViewById(R.id.profile_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override   //Αφού κλείσει επιτυχώς το pop up dialog
    public void takeInfo(final String t, final String d) {
        title = t;
        description = d;
        getOtherData();
    }

    private void getOtherData() {
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount!=null){
            name = signInAccount.getGivenName();
            surname = signInAccount.getFamilyName();
            email = signInAccount.getEmail();
            id = signInAccount.getId();
        }
        status = "Pending";
        date = Calendar.getInstance().getTime();
        getLocation();
    }

    public void getLocation(){
        //Δήλωση locationManager για την διαχείριση τοποθεσίας
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Έλεγχος για την άδεια εντοπισμού συσκευής
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; }
        //Λήψη τοποθεσίας μέσω δεδομένων κινητής τηλεφωνίας
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //Λήψη γεωγραφικού πλάτους και μήκους
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    //Δημιουργία LatLng για εμφάνιση στον χάρτη
                    latLng = new LatLng(latitude, longitude);
                    //Αποδέσμευση ακροατή αλλαγής τοποθεσίας για να μην εκτελείται συνεχώς το onLocationChanged
                    locationManager.removeUpdates(this);
                    //Πλέον έχουμε όλες τις πληροφορίες για εισαγωγή στη βάση
                    //addToDatabase();
                    mMap.addMarker(new MarkerOptions().position(latLng).title(title).icon(BitmapDescriptorFactory
                            .defaultMarker()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) { }
                @Override
                public void onProviderEnabled(String provider) { }
                @Override
                public void onProviderDisabled(String provider) { }
            });
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("App Exit")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        //finish every activity
                        finishAffinity();
                    }
                }).create().show();
    }
}