package com.sample.battery;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;

import com.sample.battery.alarm.AlarmHook;
import com.sample.battery.gps.GPSHook;
import com.sample.battery.wakelock.WakelockHook;

public class MainActivity extends Activity {

    private PowerManager.WakeLock wakeLock;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button hookAlarm = (Button) findViewById(R.id.hook_alarm);
        hookAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlarmHook(MainActivity.this).onInstall();
            }
        });


        final Button hookWakelock = (Button) findViewById(R.id.hook_wakelock);
        hookWakelock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WakelockHook(MainActivity.this).onInstall();
            }
        });

        final Button hookGPS = (Button) findViewById(R.id.hook_gps);
        hookGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GPSHook(MainActivity.this).onInstall();
            }
        });

        findViewById(R.id.wakelock_acquire).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                if (powerManager != null) {
                    wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getName());
                    wakeLock.setReferenceCounted(false);
                    wakeLock.acquire(60 * 1000);
                }
            }
        });

        findViewById(R.id.wakelock_release).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wakeLock != null) {
                    wakeLock.release();
                }
            }
        });

        findViewById(R.id.gps_request).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                } else {
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (locationManager != null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1, locationListener);
                    }
                }
            }
        });

        findViewById(R.id.gps_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationManager != null && locationListener != null) {
                    locationManager.removeUpdates(locationListener);
                }
            }
        });

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.e("===>", "onLocationChanged");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        findViewById(R.id.alarm_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(MainActivity.this, MyAlarmReceiver.class)
                        .setAction("intent_alarm");
                alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, alarmIntent);
            }
        });
        findViewById(R.id.alarm_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.cancel(alarmIntent);
            }
        });
    }

    static class MyAlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
