package com.github.makosful.friendsv2.gui.model;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.github.makosful.friendsv2.IMapCallBack;
import com.github.makosful.friendsv2.gui.controller.MapsActivity;

public class MyLocationListener implements LocationListener
{
    IMapCallBack m_view;

    public MyLocationListener(IMapCallBack view) {
        m_view = view;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(MapsActivity.TAG, "Notified on onLocationChanged");
        m_view.setCurrentLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        // Not used
    }

    @Override
    public void onProviderEnabled(String s) {
        // Not used
    }

    @Override
    public void onProviderDisabled(String s) {
        // Not used
    }
}
