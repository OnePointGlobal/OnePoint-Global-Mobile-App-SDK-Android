package com.opg.sdk.geofence;

import android.location.Location;

import com.allatori.annotations.DoNotRename;
import com.opg.sdk.models.OPGGeofenceStatus;
import com.opg.sdk.models.OPGGeofenceSurvey;

import java.util.List;

/**
 * Created by Padmavathi on 20/12/2016.
 */
@DoNotRename
public interface OPGGeofenceTriggerEvents {

    /**
     * When you try to start or stop geofencing in opgsdk.
     * The result of that will be updated on this callback method .
     * Based on isSuccess variable you will get the outcome of your action and a message is present in the OPGGeofenceStatus
     * @param opgGeofenceStatus
     */
    @DoNotRename
    void onResult(OPGGeofenceStatus opgGeofenceStatus);

    /**
     *
     * @param geofenceList
     */
    /*@DoNotRename
    void geoFencedAreas(List<Geofence> geofenceList);*/

    /**
     * This callback method is triggered when the geofence enters in any of the added geofence areas and
     * it provides the location of the entered region and the avaliable OPGGeofenceSurveys in that region/location
     * @param regionEntered
     * @param opgGeofenceList
     */
    @DoNotRename
    void didEnterSurveyRegion(Location regionEntered, List<OPGGeofenceSurvey> opgGeofenceList);

    /**
     * This callback method is triggered when the geofence exits in any of the added geofence areas and
     * it provides the location of the exited region and the exited OPGGeofenceSurveys in that region/location
     * @param regionExited
     * @param opgGeofenceList
     */
    @DoNotRename
    void didExitSurveyRegion(Location regionExited, List<OPGGeofenceSurvey> opgGeofenceList);

    /**
     * This callback method is triggered when the when the geofence is added and if the device is already inside that geofence for some time.
     * it provides the location of that region and the OPGGeofenceSurveys in that region/location.
     * @param regionExited
     * @param opgGeofenceList
     */
    @DoNotRename
    void didDwellSurveyRegion(Location regionExited, List<OPGGeofenceSurvey> opgGeofenceList);


}
