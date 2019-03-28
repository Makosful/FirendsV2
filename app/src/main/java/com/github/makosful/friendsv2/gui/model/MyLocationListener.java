package com.github.makosful.friendsv2.gui.model;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocationListener implements LocationListener
{
    @Override
    public void onLocationChanged(Location location)
    {
        // Not used
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle)
    {
        // Not used
    }

    @Override
    public void onProviderEnabled(String s)
    {
        // Not used
    }

    @Override
    public void onProviderDisabled(String s)
    {
        // Not used
    }
}
