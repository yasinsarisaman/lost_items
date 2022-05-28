package com.example.lostitems;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserDeviceRecyclerAdapter extends RecyclerView.Adapter<UserDeviceRecyclerAdapter.DeviceViewHolder> {
    private ArrayList<UserDevice> userDevices;

    public UserDeviceRecyclerAdapter(ArrayList<UserDevice> userDevices) {
        this.userDevices = userDevices;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_single_item,parent,false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        holder.uniqueIdTV.setText(userDevices.get(position).getUniqueID());
        holder.deviceNameTV.setText(userDevices.get(position).getDeviceName());
        holder.deviceLogoIMG.setImageResource(userDevices.get(position).getDeviceLogo());
        holder.deviceLonTV.setText(userDevices.get(position).getDeviceLon());
        holder.deviceLatTV.setText(userDevices.get(position).getDeviceLat());
    }

    @Override
    public int getItemCount() {
        return userDevices.size();
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder
        {
            TextView deviceNameTV,uniqueIdTV,deviceLatTV,deviceLonTV;
            ImageView deviceLogoIMG;
        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceLatTV = itemView.findViewById(R.id.deviceLat);
            deviceLonTV = itemView.findViewById(R.id.deviceLong);
            deviceLogoIMG = itemView.findViewById(R.id.deviceLogo);
            deviceNameTV = itemView.findViewById(R.id.deviceName);
            uniqueIdTV = itemView.findViewById(R.id.deviceTitle);
        }
    }
}
