package location.mx.localizacion;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Main_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_);

        ActivityCompat.requestPermissions(Main_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        if(ContextCompat.checkSelfPermission(Main_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            startService(new Intent(getBaseContext(), Gps_Activity.class));
        } else {
            ActivityCompat.requestPermissions(Main_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getBaseContext(), Gps_Activity.class));
    }

}
