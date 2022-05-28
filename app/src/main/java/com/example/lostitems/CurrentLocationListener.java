package com.example.lostitems;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.List;

public interface CurrentLocationListener extends GpsStatus.Listener, LocationListener {
    public void onLocationChanged(Location location);
    public void onProviderDisabled(String provider);
    public void onProviderEnabled(String provider);
    public void onStatusChanged(String provider, int status, Bundle extras);
    public void onGpsStatusChanged(int event);
}
