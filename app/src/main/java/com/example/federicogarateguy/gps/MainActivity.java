package com.example.federicogarateguy.gps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.jar.JarFile;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LocationManager lm;
    private ListView lv;
    private List<String> lista =  new ArrayList<>();
    private final static int FINELOCATIONCODE = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.listview);
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lista));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINELOCATIONCODE);
            }
        } else {
            requestLocation();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v("location", location.toString());
        Date d = new Date(location.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/y HH:mm:ss");
        lista.add(location.getProvider() + " | " + location.getLatitude() + " | " + location.getLongitude() + " | " + sdf.format(d));
        ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
        lv.setSelection(lista.size() - 1);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FINELOCATIONCODE) {
            int i = 0;
            while (i < permissions.length && !permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                i++;
            }
            if (i < permissions.length) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocation();
                }
            }
        }
    }

    private void requestLocation() {
        try {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        } catch (SecurityException se) {
            se.printStackTrace();
        }
    }
}
