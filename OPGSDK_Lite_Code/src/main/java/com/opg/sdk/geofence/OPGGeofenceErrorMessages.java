/**
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.opg.sdk.geofence;

import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.location.GeofenceStatusCodes;
import com.opg.sdk.OPGR;
import com.opg.sdk.R;

import static com.opg.sdk.OPGSDKConstant.GEOFENCE_NOT_AVAILABLE_KEY;
import static com.opg.sdk.OPGSDKConstant.GEOFENCE_TOO_MANY_GEOFENCES_KEY;
import static com.opg.sdk.OPGSDKConstant.GEOFENCE_TOO_MANY_PENDING_INTENTS_KEY;
import static com.opg.sdk.OPGSDKConstant.MSG_SDK_LITE_KEY;
import static com.opg.sdk.OPGSDKConstant.STRING;
import static com.opg.sdk.OPGSDKConstant.UNKNOWN_GEOFENCE_ERROR_KEY;

/**
 * Geofence error codes mapped to error messages.
 */
public class OPGGeofenceErrorMessages {
    /**
     * Prevents instantiation.
     */

    public static String GOOGLEAPICLIENT_NULL         = "GoogleApiClient is null.";
    public static String GOOGLEAPICLIENT_NOT_CONNECT  = "GoogleApiClient is not connected.";
    public static String OPGGFTRIGGEREVENTS_NULL      = "OPGGeofenceTriggerEvents is null.";
    public static String LOCATIONSERVICES_NOTENABLED  = "Location services is not enabled.It should be enabled.";
    public static String GEOFENCES_SIZE               = "Geofences list should not be null or empty.";
    public static String GEOFENCES_SIZE_EXCEED        = "Geofences list size should be less than or equal to 100.";




    private OPGGeofenceErrorMessages() {}

    /**
     * Returns the error string for a geofencing error code.
     */
    public static String getErrorString(Context context, int errorCode) {
        //Resources mResources = context.getResources();
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return  OPGR.getString(context,STRING,GEOFENCE_NOT_AVAILABLE_KEY);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return  OPGR.getString(context,STRING,GEOFENCE_TOO_MANY_GEOFENCES_KEY);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return  OPGR.getString(context,STRING,GEOFENCE_TOO_MANY_PENDING_INTENTS_KEY);
            default:
                return  OPGR.getString(context,STRING,UNKNOWN_GEOFENCE_ERROR_KEY);

        }
    }
}
