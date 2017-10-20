package com.opg.sdk.geofence;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.allatori.annotations.DoNotRename;

/**
 * Created by Padmavathi on 20/12/2016.
 */
@DoNotRename
public class OPGWakefulReceiver extends WakefulBroadcastReceiver {
    @Override@DoNotRename
    public void onReceive(Context context, Intent intent) {
        // Start the service, keeping the device awake while the service is
        // launching. This is the Intent to deliver to the service.
        Intent service = new Intent(context, OPGGeofenceTransitionsIntentService.class);
        startWakefulService(context, service);
    }
}
