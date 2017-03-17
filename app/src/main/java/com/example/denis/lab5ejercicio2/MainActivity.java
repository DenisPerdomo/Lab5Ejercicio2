package com.example.denis.lab5ejercicio2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView txtGps;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Comprobamos los permisos de la app
        checkPermission();

        txtGps = (TextView) findViewById(R.id.txtGps);

        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizar local = new Localizar();
        local.setMainActivity(this);
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,(LocationListener) local);
    }

    public class Localizar implements LocationListener{

        MainActivity mainActivity;

        public void setMainActivity(MainActivity mainActivity){
            this.mainActivity = mainActivity;
        }
        @Override
        public  void onLocationChanged (Location loc){
            //Se ejecuta cuando cambia la localizacion
            String mensaje = "La ubicación es: "+"\n Latitud: " + loc.getLatitude() + "\n Longitud: "+loc.getLongitude();
            txtGps.setText(mensaje);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            String msg = "";
            switch (status){
                case LocationProvider.OUT_OF_SERVICE:
                    msg = "GPS Status: Fuera de servicio.";
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    msg = "GPS Status: Temporalmente no disponible..";
                    break;
                case LocationProvider.AVAILABLE:
                    msg = "GPS Status: Disponible";
                    break;
            }
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            txtGps.setText("EL usuario ha activado el GPS.");
        }

        @Override
        public void onProviderDisabled (String provider){
            txtGps.setText("EL usuario ha desactivado el GPS.");
        }
    }
    private void checkPermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            Toast.makeText(this, "This version is not Android 6 or later " + Build.VERSION.SDK_INT, Toast.LENGTH_LONG).show();

        } else {

            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);

                Toast.makeText(this, "Requesting permissions", Toast.LENGTH_LONG).show();

            }else if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED){
                return;
            }

        }

        return;
    }
}
