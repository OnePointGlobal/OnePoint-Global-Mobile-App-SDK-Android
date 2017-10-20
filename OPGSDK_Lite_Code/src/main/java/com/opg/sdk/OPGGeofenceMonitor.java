package com.opg.sdk;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.opg.sdk.exceptions.OPGException;
import com.opg.sdk.geofence.OPGGeofenceErrorMessages;
import com.opg.sdk.geofence.OPGGeofenceTransitionsIntentService;
import com.opg.sdk.geofence.OPGGeofenceTriggerEvents;
import com.opg.sdk.models.OPGGeofenceStatus;
import com.opg.sdk.models.OPGGeofenceSurvey;

import java.util.ArrayList;
import java.util.List;

import static com.opg.sdk.OPGSDKConstant.EXCEPTION_GPS_ENABLED;
import static com.opg.sdk.OPGSDKConstant.EXCEPTION_NETWORK_ENABLED;
import static com.opg.sdk.OPGSDKConstant.GEOFENCE_MONITORING_ADDED;
import static com.opg.sdk.OPGSDKConstant.GEOFENCE_MONITORING_REMOVED;
import static com.opg.sdk.OPGSDKConstant.HYPHEN;
import static com.opg.sdk.OPGSDKConstant.INVALIDE_LOCATION_PERMISSION;

/**
 * Created by Dinesh-opg on 10/12/2017.
 * This class is used to perform the geofecning opertations like start geofencing and stop geofencing.
 * This also maintains the reference for the opgGeofenceTriggerEvents which is the callback object
 * for all the geofecning transistions. This is singleton class.
 */
class OPGGeofenceMonitor {

    private static OPGGeofenceMonitor opgGeofenceMonitor;
    private PendingIntent mGeofencePendingIntent;
    private OPGGeofenceTriggerEvents opgGeofenceTriggerEvents;

    public static OPGGeofenceMonitor getInstance(){
        if(opgGeofenceMonitor == null){
            opgGeofenceMonitor = new OPGGeofenceMonitor();
        }
        return opgGeofenceMonitor;
    }

    private OPGGeofenceMonitor(){

    }

    private List<OPGGeofenceSurvey> getOpgGeofenceSurveyList(Context context) {
        return OPGPreference.getOPGGeofenceSurveys(context);
    }

    private void setOpgGeofenceSurveyList(Context context ,List<OPGGeofenceSurvey> opgGeofenceSurveyList) {
        OPGPreference.clearOPGGeofenceSurveys(context);
        OPGPreference.saveOPGGeofenceSurveys(context,opgGeofenceSurveyList);
    }

    protected OPGGeofenceTriggerEvents getOpgGeofenceTriggerEvents() {
        return opgGeofenceTriggerEvents;
    }

    protected void setOpgGeofenceTriggerEvents(OPGGeofenceTriggerEvents opgGeofenceTriggerEvents) {
        this.opgGeofenceTriggerEvents = opgGeofenceTriggerEvents;
    }

    /**
     *This method takes the googleApiClient and checks whether the googleapiclient is null or is it connected.And then the list of geofence objects is created from the list of
     * OPGGeofenceSurvey.
     * @param mContext
     * @param googleApiClient
     * @param opgGeofencesList
     * @throws OPGException
     */
    protected void startGeofencingMonitor(final Context mContext, GoogleApiClient googleApiClient, List<OPGGeofenceSurvey> opgGeofencesList,
                                          final OPGGeofenceTriggerEvents opgGeofenceTriggerEvents) throws OPGException {
        verifyGoogleApiClient(googleApiClient);
        List<OPGGeofenceSurvey> opgGeofenceSurveyList = new ArrayList<>();
        if(opgGeofenceTriggerEvents == null){
            throw new OPGException(OPGGeofenceErrorMessages.OPGGFTRIGGEREVENTS_NULL);
        }

        if(!locationServicesEnabled(mContext)){
            throw new OPGException(OPGGeofenceErrorMessages.LOCATIONSERVICES_NOTENABLED);
        }

        /*if(opgGeofencesList == null || opgGeofencesList.isEmpty() || opgGeofencesList.size()>100){*/
        if(opgGeofencesList == null || opgGeofencesList.isEmpty())
            throw new OPGException(OPGGeofenceErrorMessages.GEOFENCES_SIZE);
           /* else
                throw new OPGException(OPGGeofenceErrorMessages.GEOFENCES_SIZE_EXCEED);
        }*/

        try {
            this.opgGeofenceTriggerEvents = opgGeofenceTriggerEvents;
            opgGeofenceSurveyList
                    = (opgGeofencesList.size()>100) ? opgGeofencesList.subList(0,100) : opgGeofencesList;
            this.setOpgGeofenceSurveyList(mContext,opgGeofenceSurveyList);
            if(OPGPreference.getIsGeofencingTriggered(mContext)){
                stopGeofencingMonitor(mContext,googleApiClient,opgGeofenceTriggerEvents);
            }
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    // The GeofenceRequest object.
                    getGeofencingRequest(convertOPGObjectToGeofence(mContext)),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent(mContext)
            ).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    {
                        OPGGeofenceStatus opgGeofenceStatus = new OPGGeofenceStatus();
                        if(status.isSuccess()){
                            OPGPreference.setIsGeofencingTriggered(mContext,true);
                            opgGeofenceStatus.setSuccess(true);
                            opgGeofenceStatus.setMessage(GEOFENCE_MONITORING_ADDED);
                        } else {
                            opgGeofenceStatus.setSuccess(false);
                            opgGeofenceStatus.setMessage(OPGGeofenceErrorMessages.getErrorString(mContext,status.getStatusCode()));
                        }
                        opgGeofenceStatus.setMonitoring(OPGPreference.getIsGeofencingTriggered(mContext));
                        if(opgGeofenceTriggerEvents!=null)
                        { opgGeofenceTriggerEvents.onResult(opgGeofenceStatus); }
                    }
                }
            }); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(mContext,securityException);
            throw  securityException;
        }
    }

    /**
     * Converts OPGGeofenceSurvey to Geofence Objects..
     * @return
     */
    private List<Geofence> convertOPGObjectToGeofence(Context context) {
        List<OPGGeofenceSurvey> opgGeofenceSurveyList = getOpgGeofenceSurveyList(context);
        List<Geofence> geofenceList = new ArrayList<>();
        if(opgGeofenceSurveyList!=null && !opgGeofenceSurveyList.isEmpty()){
            StringBuilder stringBuilder = new StringBuilder();

            for (OPGGeofenceSurvey entry : opgGeofenceSurveyList) {
                stringBuilder.setLength(0);
                stringBuilder.append(entry.getSurveyReference()).append(HYPHEN).append(entry.getLatitude()).append(HYPHEN).append(entry.getLongitude());
                geofenceList.add(new Geofence.Builder()
                        // Set the request ID of the geofence. This is a string to identify this
                        // geofence.and the max length should be 100 characters
                        .setRequestId(stringBuilder.toString())
                        // Set the circular region of this geofence.
                        .setCircularRegion(
                                entry.getLatitude(),
                                entry.getLongitude(),
                                entry.getRange()
                        )

                        // Set the expiration duration of the geofence. This geofence gets automatically
                        // removed after this period of time.
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)

                        // Set the transition types of interest. Alerts are only generated for these
                        // transition. We track entry and exit transitions in this sample.
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                Geofence.GEOFENCE_TRANSITION_EXIT)

                        // Create the geofence.
                        .build());
            }
        }

        return geofenceList;
    }


    /**
     *
     * @param mContext
     * @param googleApiClient
     * @throws OPGException
     */
    protected void stopGeofencingMonitor(final Context mContext, GoogleApiClient googleApiClient, final OPGGeofenceTriggerEvents opgGeofenceTriggerEvents) throws OPGException {
        verifyGoogleApiClient(googleApiClient);
        try {
            this.opgGeofenceTriggerEvents = opgGeofenceTriggerEvents;
            // Remove geofences.
            LocationServices.GeofencingApi.removeGeofences(
                    googleApiClient,
                    // This is the same pending intent that was used in addGeofences().
                    getGeofencePendingIntent(mContext)
            ).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    OPGGeofenceStatus opgGeofenceStatus = new OPGGeofenceStatus();
                    if(status.isSuccess()){
                        OPGPreference.setIsGeofencingTriggered(mContext,false);
                        opgGeofenceStatus.setSuccess(true);
                        opgGeofenceStatus.setMessage(GEOFENCE_MONITORING_REMOVED);
                    } else {
                        opgGeofenceStatus.setSuccess(false);
                        opgGeofenceStatus.setMessage(OPGGeofenceErrorMessages.getErrorString(mContext,status.getStatusCode()));
                    }
                    opgGeofenceStatus.setMonitoring(OPGPreference.getIsGeofencingTriggered(mContext));

                    if(opgGeofenceTriggerEvents!=null)
                    {  opgGeofenceTriggerEvents.onResult(opgGeofenceStatus); }
                }
            }); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(mContext,securityException);
        }
    }


    /**
     * This method used to create a geofencing request....
     * @param geofenceList
     * @return
     */
    private GeofencingRequest getGeofencingRequest(List<Geofence> geofenceList) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    /**
     * This method is to verify the GoogleApiClient is null or is it connected or not...
     * @param googleApiClient
     * @throws OPGException
     */
    private void verifyGoogleApiClient(GoogleApiClient googleApiClient) throws OPGException {
        if (googleApiClient != null || !googleApiClient.isConnected()) {
            if(googleApiClient == null)
                throw new OPGException(OPGGeofenceErrorMessages.GOOGLEAPICLIENT_NULL);
            else if(!googleApiClient.isConnected())
                throw new OPGException(OPGGeofenceErrorMessages.GOOGLEAPICLIENT_NOT_CONNECT);
        }
    }


    private void logSecurityException(Context mContext , SecurityException securityException) {
        Log.e(mContext.getClass().getName(),INVALIDE_LOCATION_PERMISSION , securityException);
    }

    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    private PendingIntent getGeofencePendingIntent(Context mContext) {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(mContext, OPGGeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Verifies wheather the location services are enabled or not.....
     * @param mContext
     * @return
     */
    private boolean locationServicesEnabled(Context mContext) {
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean net_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Log.e(mContext.getPackageName(),EXCEPTION_GPS_ENABLED);
        }

        try {
            net_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Log.e(mContext.getPackageName(),EXCEPTION_NETWORK_ENABLED);
        }
        return gps_enabled || net_enabled;
    }
}
