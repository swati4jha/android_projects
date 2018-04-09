package com.example.mihai.group24_inclass12;

import android.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.HashMap;
import java.util.Iterator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap mMap;
    LocationManager mLocationManager;
    LocationListener mLocListener;
    boolean permissionGranted;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    HashMap<Double, Double> locations = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //UiSettings.setZoomControlsEnabled(true);
        mapFragment.getView().setClickable(true);


        permissionGranted = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in UNCC Charlotte Library and move the camera
        LatLng ch = new LatLng(35.307019, -80.732411);
        mMap.addMarker(new MarkerOptions().position(ch).title("Marker in Charlotte"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ch));

        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        /*final Polyline polyline = googleMap.addPolyline(new PolylineOptions().clickable(true).width(5)
                .color(Color.RED));*/
        final PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("GPS Not Enabled").setMessage("Would you like to turn on the GPS?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            } else {
                mLocListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.d("demo", location.getLatitude() + " " + location.getLongitude());
                        locations.put(location.getLatitude(), location.getLongitude());
                        Log.d("demo", locations.toString());
                        //polyline.remove();
                        //Iterator it = locations.entrySet().iterator();
                        //PolylineOptions options = new PolylineOptions();
                        //Polyline polyline = googleMap.addPolyline(new PolylineOptions().clickable(true));
                        //while(it.hasNext()) {
                        //    options.add(it.next());
                        //}
                        options.add(new LatLng(location.getLatitude(), location.getLongitude()));
                        /*for(Double key: locations.keySet()){
                            options.add(new LatLng(key, locations.get(key)));
                        }*/
                        //polyline =googleMap.addPolyline(options.clickable(true));


                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        Log.d("demo", "changed");
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        Log.d("demo", "enabled");
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Log.d("demo", "disabled");
                    }
                };
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 100, mLocListener);
                mMap.addPolyline(options);

            }
        }

    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}
