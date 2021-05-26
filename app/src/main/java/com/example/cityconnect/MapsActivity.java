package com.example.cityconnect;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Dialog.DialogListener {

    private GoogleMap mMap;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 9876;
    public static FirebaseFirestore db;
    
    //Δεδομένα που θα χρειαστούμε για αποστολή αναφοράς στην βάση
    private String title, description, name, surname, id, email, status, address, postalcode, city;
    private LatLng latLng;
    private Date date;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //αρχικοποίηση σύνδεσης με βάση
        db = FirebaseFirestore.getInstance();
        //συγκέντρωση στοιχείων χρήστη για τυχόν αποστολή αναφοράς στη βάση
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            name = signInAccount.getGivenName();
            surname = signInAccount.getFamilyName();
            email = signInAccount.getEmail();
            id = signInAccount.getId();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //open pop up dialog when report_button is pressed
        findViewById(R.id.report_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog();
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });
        //go to Profile when button is pressed
        findViewById(R.id.profile_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        //checks permissions for location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override   //Αφού κλείσει επιτυχώς το pop up dialog
    public void takeInfo(final String t, final String d) {
        //συγκέντρωση στοιχείων αναφοράς
        title = t;
        description = d;
        date = Calendar.getInstance().getTime();
        status = "Pending";

        getLocation();
    }

    public void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location Problem", Toast.LENGTH_LONG).show();
            return;
        }
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        double lat = myLocation.getLatitude();
        double lng = myLocation.getLongitude();
        latLng = new LatLng(lat, lng);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            postalcode = addresses.get(0).getPostalCode();
            city = addresses.get(0).getLocality();
            address = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
            postalcode = "Unknown";
            address = "Unknown";
        }

        addToDatabase();
    }

    private void addToDatabase1() {
        Test t = new Test("aaaa","bbbb");
        db.collection("test").add(t);
    }

    private void addToDatabase() {
        Reports report = new Reports(title, description, name, surname, id, email, status, address, postalcode, latLng.toString(), date.toString(), city);
        //Εισαγωγή στην βάση
        db.collection("reports").add(report);
    }
}
