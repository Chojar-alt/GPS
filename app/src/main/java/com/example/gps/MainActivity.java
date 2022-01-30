package com.example.gps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationListenerCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView textViewLatitude, textViewLongitude;
    LocationManager locationManager;
    ListView listView;
    DatabaseHelper databaseHelper;
    ArrayList arrayList;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewLatitude = findViewById(R.id.latitude);
        textViewLongitude = findViewById(R.id.longitude);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listView = findViewById(R.id.list_view);

        databaseHelper = new DatabaseHelper(MainActivity.this);

        arrayList = databaseHelper.getAllText();

        arrayAdapter = new ArrayAdapter( MainActivity.this,
                android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(arrayAdapter);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        //czas w mili sekundach
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                textViewLongitude.setText(String.valueOf(location.getLongitude()));
                textViewLatitude.setText(String.valueOf(location.getLatitude()));
                String text = textViewLatitude.getText().toString();
                String textOne = textViewLongitude.getText().toString();
                if(!text.isEmpty()){
                    if (databaseHelper.addText(text) && databaseHelper.addText(textOne)){

                        textViewLatitude.setText("");
                        textViewLongitude.setText("");
                        arrayList.clear();
                        arrayList.addAll(databaseHelper.getAllText());
                        arrayAdapter.notifyDataSetChanged();
                        listView.invalidateViews();
                        listView.refreshDrawableState();
                    }
                }
            }
        });


    }
}