package com.example.lostitems;

import static android.provider.Settings.Secure.ANDROID_ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

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
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

//TODO USER UID

public class SenderActivity extends AppCompatActivity implements CurrentLocationListener{

    private static final int PERMISSION_LOCATION = 1001;

    //UI Elements
    TextView lat,lon;
    Button refreshBtn;
    Toolbar senderToolbar;
    EditText deviceNameET, deviceIDET, accountInfoET;
    ProgressDialog waitDialog;
    CountDownTimer countDownTimer;

    //Local variables
    private double latitude,longitude;
    private static String deviceID;
    private boolean deviceDataSaved;

    //Firebase instance
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRoot = database.getReference().child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);

        init();
        startAllEvents();

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDeviceDataToDB();
            }
        });
    }

    private void startAllEvents() {

        waitDialog.show();
        countDownTimer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long l) {
                waitDialog.setMessage("Please Wait...");
                isThereDeviceData();
            }

            @Override
            public void onFinish() {
                waitDialog.dismiss();
                if(deviceDataSaved)
                {
                    checkPermissionsAndShareLocation();
                    setDefaultValues();
                }
                else
                {
                    Toast.makeText(SenderActivity.this,"Please set your device name before sharing location", Toast.LENGTH_SHORT).show();
                }
            }
        }.start();

    }
    private void isThereDeviceData() {

        dbRoot.child("User Device").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    deviceDataSaved = true;
                }
                else deviceDataSaved = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setDefaultValues() {

        dbRoot.child("User Device").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                deviceNameET.setText(snapshot.child("Device Name").getValue().toString());
                deviceIDET.setText(snapshot.child("Device ID").getValue().toString());
                //TODO account bilgileri set edilecek
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkPermissionsAndShareLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SenderActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
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
        senderToolbar = findViewById(R.id.SenderActivityToolbar);
        if(senderToolbar != null)
        {
            setSupportActionBar(senderToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        lat = (TextView) findViewById(R.id.latitudeText);
        lon = (TextView) findViewById(R.id.longitudeText);
        refreshBtn = (Button) findViewById(R.id.refreshBtn);
        deviceID = Settings.Secure.getString(getContentResolver(), ANDROID_ID);
        deviceNameET = (EditText) findViewById(R.id.deviceNameEditText);
        deviceIDET = (EditText) findViewById(R.id.deviceIDEditText);
        accountInfoET = (EditText) findViewById(R.id.accountInfo);

        waitDialog = new ProgressDialog(SenderActivity.this);
        waitDialog.setMessage("Please Wait");
        waitDialog.setCancelable(false);
        waitDialog.setProgress(0);
        waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        waitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        saveLocationDataToDB(latitude,longitude);
    }
    @Override
    public void onProviderDisabled(String provider) {
        //empty
    }
    @Override
    public void onProviderEnabled(String provider) {
        //empty
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //empty
    }
    @Override
    public void onGpsStatusChanged(int event) {
        //empty
    }
    private void saveLocationDataToDB(double latitude, double longitude) {
        lat.setText(String.valueOf(latitude));
        lon.setText(String.valueOf(longitude));

        String finalLatitude = String.valueOf(latitude);
        String finalLongitude = String.valueOf(longitude);

        HashMap<String, String> userLocationMap = new HashMap<>();

        userLocationMap.put("Latitude", finalLatitude);
        userLocationMap.put("Longitude", finalLongitude);

        dbRoot.child("User Device").child("User Location").setValue(userLocationMap);
    }

    private void saveDeviceDataToDB()
    {
        HashMap<String, String> userDeviceDataMap = new HashMap<>();
        String finalDeviceName = deviceNameET.getText().toString();
        userDeviceDataMap.put("Device ID", deviceID);
        userDeviceDataMap.put("Device Name", finalDeviceName);
        dbRoot.child("User Device").setValue(userDeviceDataMap);

        startAllEvents();
    }


}