package com.example.lostitems;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.lostitems.databinding.ActivityReceiverMapBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ReceiverMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityReceiverMapBinding binding;

    //FİREBASE COMPONENTS
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRoot = database.getReference().child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

    MarkerOptions markerOptions;
    LatLng tempLatLong = new LatLng(0,0);

    boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityReceiverMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dbRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!isFirstTime)
                {
                    Log.d("Firebase"," Değişti");
                    String currentLat = snapshot.child("User Device").child("User Location").child("Latitude").getValue().toString();
                    String currentLong = snapshot.child("User Device").child("User Location").child("Longitude").getValue().toString();
                    double finalLat = Double.parseDouble(currentLat);
                    double finalLong = Double.parseDouble(currentLong);
                    LatLng locMarker = new LatLng(finalLat, finalLong);
                    markerOptions.position(locMarker);
                    mMap.clear();
                    mMap.addMarker(markerOptions);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locMarker,15f));
                    Toast.makeText(ReceiverMapActivity.this,"New location: " + currentLat + " / " + currentLong, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ReceiverMapActivity.this,"Fetching Location...", Toast.LENGTH_SHORT).show();
                    isFirstTime = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng tempLatLng = new LatLng(39.9035557, 32.6226816);
        markerOptions = new MarkerOptions().position(tempLatLng).title("Your Sender is Here");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_2));
        mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tempLatLng,15f));
        // Add a marker in Sydney and move the camera
        /*LatLng marker = new LatLng(39.9035557, 32.6226816);
        mMap.addMarker(new MarkerOptions().position(marker).title("You are here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));*/
    }
}