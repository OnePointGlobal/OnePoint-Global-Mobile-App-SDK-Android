package com.opg.sdk.geofence;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.allatori.annotations.DoNotRename;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.gson.Gson;
import com.opg.sdk.BuildConfig;
import com.opg.sdk.OPGPreference;
import com.opg.sdk.OPGR;
import com.opg.sdk.OPGSDKConstant;
import com.opg.sdk.R;
import com.opg.sdk.models.OPGGeofenceSurvey;

import java.util.ArrayList;
import java.util.List;

import static com.opg.sdk.OPGSDKConstant.COLON;
import static com.opg.sdk.OPGSDKConstant.COMA;
import static com.opg.sdk.OPGSDKConstant.GEOFENCE_TRANSITION_DWELL_KEY;
import static com.opg.sdk.OPGSDKConstant.GEOFENCE_TRANSITION_ENTERED_KEY;
import static com.opg.sdk.OPGSDKConstant.GEOFENCE_TRANSITION_EXITED_KEY;
import static com.opg.sdk.OPGSDKConstant.GEOFENCE_TRANSITION_INVALID_TYPE_KEY;
import static com.opg.sdk.OPGSDKConstant.HYPHEN;
import static com.opg.sdk.OPGSDKConstant.STRING;
import static com.opg.sdk.OPGSDKConstant.UNKNOWN_GEOFENCE_TRANSITION_KEY;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
@DoNotRename
public class OPGGeofenceTransitionsIntentService extends IntentService {

    protected static final String TAG = "GeofenceTransitionsIS";
    private com.opg.sdk.OPGSDK opgsdk;
    @DoNotRename
    public String BROADCAST_GEOFENCE_TRANSITION_ENTER  = ".transition.enter";
    @DoNotRename
    public String BROADCAST_GEOFENCE_TRANSITION_EXIT   = ".transition.exit";
    @DoNotRename
    public String BROADCAST_GEOFENCE_TRANSITION_DWELL  = ".transition.dwell";

    private Context context;

    public OPGGeofenceTransitionsIntentService() {
        super(OPGGeofenceTransitionsIntentService.class.getName());
    }

    @Override@DoNotRename
    public void onCreate() {
        super.onCreate();
        opgsdk = new com.opg.sdk.OPGSDK();
        context = this;
    }

    /**
     * Handles incoming intents.
     * @param intent sent by Location Services. This Intent is provided to Location
     *               Services (inside a PendingIntent) when addGeofences() is called.
     */
    @Override@DoNotRename
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = OPGGeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());
            if(BuildConfig.DEBUG) {
                Log.e(TAG, errorMessage);
            }
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT || geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
            Intent broadcastIntent = new Intent();
            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            List<Geofence> triggeringGeofences            = (ArrayList<Geofence>) geofencingEvent.getTriggeringGeofences();

            List<OPGGeofenceSurvey> triggeredOPGGeofences = convertGeofenceToOPGObject(context,triggeringGeofences);
            if(BuildConfig.DEBUG){
                Log.d(TAG,"OPGSDKTriggerTransType"+geofenceTransition);
                Log.d(TAG,"OPGSDKTriggerGeofencesSize:"+triggeringGeofences.size());
                Log.d(TAG,"OPGSDKTriggerOriginalSize:"+opgsdk.getOPGGeofenceSurveys(context).size());
                Log.d(TAG,"OPGSDKTriggerConvertedSize:"+triggeredOPGGeofences.size());
            }

            if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
                if(opgsdk.getOpgGeofenceTriggerEvents() != null) {
                    opgsdk.getOpgGeofenceTriggerEvents().didEnterSurveyRegion(geofencingEvent.getTriggeringLocation(), convertGeofenceToOPGObject(context,geofencingEvent.getTriggeringGeofences()));
                }
                broadcastIntent.setAction(context.getPackageName()+BROADCAST_GEOFENCE_TRANSITION_ENTER);
            }

            if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
                if(opgsdk.getOpgGeofenceTriggerEvents() != null) {
                    opgsdk.getOpgGeofenceTriggerEvents().didExitSurveyRegion(geofencingEvent.getTriggeringLocation(), convertGeofenceToOPGObject(context,geofencingEvent.getTriggeringGeofences()));
                }
                broadcastIntent.setAction(context.getPackageName()+BROADCAST_GEOFENCE_TRANSITION_EXIT);
            }


            if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL){
                if(opgsdk.getOpgGeofenceTriggerEvents() != null) {
                    opgsdk.getOpgGeofenceTriggerEvents().didDwellSurveyRegion(geofencingEvent.getTriggeringLocation(), convertGeofenceToOPGObject(context,geofencingEvent.getTriggeringGeofences()));
                }
                broadcastIntent.setAction(context.getPackageName()+BROADCAST_GEOFENCE_TRANSITION_DWELL);
            }

            broadcastIntent.putExtra(OPGSDKConstant.LATITUDE,geofencingEvent.getTriggeringLocation().getLatitude());
            broadcastIntent.putExtra(OPGSDKConstant.LONGITUDE,geofencingEvent.getTriggeringLocation().getLongitude());

            broadcastIntent.putExtra(OPGSDKConstant.TRIGGERED_GEOFENCES,convertOPGObjectsToString(triggeredOPGGeofences));

            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeringGeofences
            );

            sendBroadcast(broadcastIntent);
            if(BuildConfig.DEBUG) {
                Log.i(TAG, geofenceTransitionDetails);
            }
        } else {
            // Log the error.
            if(BuildConfig.DEBUG) {
                Log.e(TAG, OPGR.getString(getApplicationContext(), STRING, GEOFENCE_TRANSITION_INVALID_TYPE_KEY));
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        OPGWakefulReceiver.completeWakefulIntent(intent);
    }


//    private ArrayList<String> getRequestIds(List<Geofence> geofenceList){
//        ArrayList<String> geofenceIds = new ArrayList<>();
//        for (Geofence geofence:geofenceList){
//            geofenceIds.add(geofence.getRequestId());
//        }
//        return geofenceIds;
//    }

    private String convertOPGObjectsToString(List<OPGGeofenceSurvey> opgGeofenceSurveys){
        Gson gson = new Gson();
        return gson.toJson(opgGeofenceSurveys);
    }


    private List<OPGGeofenceSurvey> convertGeofenceToOPGObject(Context context, List<Geofence> triggeredGeofences){
        List<OPGGeofenceSurvey> triggeredOPGGeofenceSurveys = new ArrayList<>();
        List<OPGGeofenceSurvey> savedOPGGeofenceSurveys = opgsdk.getOPGGeofenceSurveys(context);
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "SAVEDOPGGEofenceSurveys" + savedOPGGeofenceSurveys.size());
        }
        if(savedOPGGeofenceSurveys!=null && savedOPGGeofenceSurveys.size()>0 && triggeredGeofences.size() > 0){
            StringBuilder stringBuilder = new StringBuilder();
            for (OPGGeofenceSurvey opgGeofenceSurvey : opgsdk.getOPGGeofenceSurveys(context)){
                stringBuilder.setLength(0);
                stringBuilder.append(opgGeofenceSurvey.getSurveyReference()).append(HYPHEN).append(opgGeofenceSurvey.getLatitude()).append(HYPHEN).append(opgGeofenceSurvey.getLongitude());
                for (Geofence geofence: triggeredGeofences){
                    if(geofence.getRequestId().equalsIgnoreCase(stringBuilder.toString())){
                        triggeredOPGGeofenceSurveys.add(opgGeofenceSurvey);
                        break;
                    }
                }
            }
        }
        return triggeredOPGGeofenceSurveys;
    }
    /**
     * Gets transition details and returns them as a formatted string.
     *
     * @param context               The app context.
     * @param geofenceTransition    The ID of the geofence transition.
     * @param triggeringGeofences   The geofence(s) triggered.
     * @return                      The transition details formatted as String.
     */
    private String getGeofenceTransitionDetails(
            Context context,
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        ArrayList triggeringGeofencesIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(COMA,  triggeringGeofencesIdsList);

        return geofenceTransitionString +COLON + triggeringGeofencesIdsString;
    }

    /**
     * Maps geofence transition types to their human-readable equivalents.
     *
     * @param transitionType    A transition type constant defined in Geofence
     * @return                  A String indicating the type of transition
     */
    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return OPGR.getString(getApplicationContext(),STRING,GEOFENCE_TRANSITION_ENTERED_KEY);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return OPGR.getString(getApplicationContext(),STRING,GEOFENCE_TRANSITION_EXITED_KEY);
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                return OPGR.getString(getApplicationContext(),STRING,GEOFENCE_TRANSITION_DWELL_KEY);
            default:
                return OPGR.getString(getApplicationContext(),STRING,UNKNOWN_GEOFENCE_TRANSITION_KEY);
        }
    }

    /*public String GEOFENCE_TRANSITION_ENTERED  = "com.opg.sdk.transition.enter";
    public String GEOFENCE_TRANSITION_EXITED  = "com.opg.sdk.transition.exit";
    public String GEOFENCE_TRANSITION_DWELL  = "com.opg.sdk.transition.dwell";
    public String UNKNOWN_GEOFENCE_TRANSITION  = "com.opg.sdk.transition.unknown";*/

}
