package location.mx.localizacion;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class Gps_Activity extends Service {

    private static final String TAG = "#TAG - ";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;

   private class Location_Service implements android.location.LocationListener{

       Location mLastLocation;

       public Location_Service(String provider){
           this.mLastLocation = new Location(provider);
       }

       @Override
       public void onLocationChanged(Location location) {
           mLastLocation.set(location);
           actualizaMejorLocalizador(location);
       }

       @Override
       public void onStatusChanged(String provider, int status, Bundle extras) {
           activarProveedoresService();
       }

       @Override
       public void onProviderEnabled(String provider) {
           activarProveedoresService();
       }

       @Override
       public void onProviderDisabled(String provider) {
           activarProveedoresService();
       }
   }

    Location_Service[] location_services = new Location_Service[]{
      new Location_Service(LocationManager.GPS_PROVIDER),
      new Location_Service(LocationManager.NETWORK_PROVIDER)
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeLocationManager();
        activarProveedoresService();
        ultimaLocalizacion();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        /**
         * START_STICKY:           Crea de nuevo el servicio después de haber sido destruido por el sistema. En este caso llamará a onStartCommand() referenciando un intent nulo.
         * START_REDELIVER_INTENT: Crea de nuevo el servicio si el sistema lo destruyó. A diferencia de START_STICKY, esta vez sí se retoma el último intent que recibió el servicio.
         * START_NOT_STICKY:       Indica que el servicio no debe recrearse al ser destruido sin importar que haya quedado un trabajo pendiente.
         */

        Criteria criteria = new Criteria();
        criteria.setCostAllowed(false);
        criteria.setAltitudeRequired(false);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        mLocationManager.getBestProvider(criteria, true);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initializeLocationManager() {
        if(mLocationManager == null){
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void activarProveedoresService() {

        try{

            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, location_services[0]);
                }
                if(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, location_services[1]);
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }

    }

    void ultimaLocalizacion(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                actualizaMejorLocalizador(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            } else if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                actualizaMejorLocalizador(mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            }
        }
    }

    private static Location actualizaMejorLocalizador(Location localiz) {
        Log.i(TAG, "Latitud: " + localiz.getLatitude() + " Longitud: " + localiz.getLongitude());

        return localiz;
    }

}
