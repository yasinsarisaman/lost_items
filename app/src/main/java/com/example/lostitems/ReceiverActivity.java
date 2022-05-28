package com.example.lostitems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Debug;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ReceiverActivity extends AppCompatActivity implements CurrentLocationListener {

    private static final int PERMISSION_LOCATION = 1001;

    androidx.appcompat.widget.Toolbar receiverActivityToolbar;
    private ArrayList<UserDevice> userDevices;
    private RecyclerView recyclerView;
    private UserDeviceRecyclerAdapter userDeviceRecyclerAdapter;
    private TextView latTextView,longTextView;

    //Firebase initializing
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRoot = database.getReference().child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

    ProgressDialog waitDialog;
    CountDownTimer countDownTimer;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        init();
        checkPermissionsAndShareLocation();
        waitDialog.show();
        countDownTimer = new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long l) {
                waitDialog.setMessage("Please Wait For Retrieving Data...");
                getDevicesFromDB();
            }

            @Override
            public void onFinish() {
                waitDialog.dismiss();
                userDeviceRecyclerAdapter.notifyDataSetChanged();
            }
        }.start();

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        dbRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDeviceRecyclerAdapter.notifyDataSetChanged();
                Log.d("Firebase"," Değişti");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void viewSettings()
    {
        userDevices = new ArrayList<>();
        recyclerView = findViewById(R.id.userDeviceListRV);
        userDeviceRecyclerAdapter = new UserDeviceRecyclerAdapter(userDevices);
        recyclerView.setAdapter(userDeviceRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void checkPermissionsAndShareLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ReceiverActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
            } else {
                showLocation();
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_LOCATION)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                showLocation();
            }
            else
            {
                Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
                finish();
            }

        }

    }

    @SuppressLint("MissingPermission")
    private void showLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //check gps is enabled
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            //loading locations
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
        }
        else
        {
            Toast.makeText(this,"Please enable GPS",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    private void init()
    {
        latTextView = findViewById(R.id.latitudeText);
        longTextView = findViewById(R.id.longitudeText);
        receiverActivityToolbar = findViewById(R.id.ReceiverActivityToolbar);
        if(receiverActivityToolbar != null)
        {
            setSupportActionBar(receiverActivityToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        viewSettings();
        waitDialog = new ProgressDialog(ReceiverActivity.this);
        waitDialog.setMessage("Please Wait, Getting Data From Database...");
        waitDialog.setCancelable(false);
        waitDialog.setProgress(0);
        waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        waitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
    }

    private void getDevicesFromDB() {
        UserDevice userDevice = new UserDevice();
        userDevice.setDeviceLogo(R.drawable.default_device);
        dbRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDevice.setDeviceName(snapshot.child("User Device").child("Device Name").getValue().toString());
                userDevice.setUniqueID(snapshot.getKey());
                userDevice.setDeviceLat(snapshot.child("User Device").child("User Location").child("Latitude").getValue().toString());
                userDevice.setDeviceLon(snapshot.child("User Device").child("User Location").child("Longitude").getValue().toString());
                userDevices.add(new UserDevice(userDevice.getUniqueID(),userDevice.getDeviceName(),userDevice.getDeviceLogo(),userDevice.getDeviceLat(),userDevice.getDeviceLon()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onLocationChanged(Location location) {
        //Log.d("Konum Değişti!", "Yeni Konum: "+ location.getLatitude() + " " + location.getLongitude());
        latTextView.setText(String.valueOf(location.getLatitude()));
        longTextView.setText(String.valueOf(location.getLongitude()));
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onGpsStatusChanged(int event) {

    }
}