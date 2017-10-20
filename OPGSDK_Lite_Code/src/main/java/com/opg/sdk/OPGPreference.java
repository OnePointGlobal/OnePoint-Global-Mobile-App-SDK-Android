package com.opg.sdk;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opg.sdk.models.OPGGeofenceSurvey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.opg.sdk.OPGSDKConstant.EMPTY_STRING;

///////////////////////////////////////////////////////////////////////////////
//
//Copyright (c) 2016 OnePoint Global Ltd. All rights reserved.
//
//This code is licensed under the OnePoint Global License.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//THE SOFTWARE.
//
///////////////////////////////////////////////////////////////////////////////

/**
 *
 * OPGPreference class.
 */
public class OPGPreference
{

    // private static OPGPreference opgPreference = null;
    private final static String PREFS_NAME = "OnePointPreference";
    private final static String OPG_GEOFENCE_PREF = "OnePointGeofencePerference";
    private final static String PREF_GEOFENCE_KEY = "OPGGeofenceKey";
    private final static String PREFERENCES_API_URL = "ApiUrl";
    private final static String PREFERENCES_INTERVIEW_URL = "InterviewUrl";
    private final static String PREFERENCES_USERNAME = "Username";
    private final static String PREFERENCES_SHARED_KEY = "SharedKey";
    private final static String PREFERENCES_APP_VERSION = "AppVersion";
    private final static String PREFERENCES_UNIQUE_ID = "UniqueID";
    private final static String PREFERENCES_DOWNLOAD_URL = "DownloadUrl";
    private final static String FCM_DEVICE_TOKEN = "FcmDeviceToken";
    private final static String PREFS_MYSURVEY_APP_ADMIN_NAME ="MySurveysAppAdminName";
    private final static String PREFS_MYSURVEY_APP_SHARED_KEY ="MySurveysAppSharedKey";

    private final static String PREFS_IS_GEOFENCING_TRIGGERED = "isGeoFencingTriggered";
    private final static String PREFS_GEOFENCING_SURVEYS      = "geoFencingTriggered";
    private final static String LOGIN_TYPE = "loginType";
    private final static String FACEBOOK_TOKEN = "FacebookToken";
    private final static String GOOGLE_TOKEN = "GoogleToken";
    private final static String APP_LOGIN_USERNAME = "appLoginUser";
    private final static String APP_LOGIN_PASSWORD = "appLoginPassword";
    private final static String IS_APP_DEBUGABLE = "appDebuggable";




    private OPGPreference()
    {
        // Default Private Constructor
    }

    /*
     * public static OPGPreference getOPGPreference() { if (opgPreference ==
     * null) { synchronized (OPGPreference.class) { if (opgPreference == null) {
     * opgPreference = new OPGPreference(); } } } return opgPreference; }
     */

    // for storing String Value
    private static synchronized void setProperty(final Context context, final String name, final String value)
    {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().putString(name, value).apply();
    }

    private static synchronized String getProperty(final Context context, final String name, final String defaultValue)
    {
        try
        {
            final String value = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(name, null);
            if (value == null)
            {
                setProperty(context, name, defaultValue);
                return defaultValue;
            }
            else
            {
                return value;
            }
        }
        catch (final Exception e)
        {
            setProperty(context, name, defaultValue);
            return defaultValue;
        }
    }

    // for storing boolean value
    private static synchronized void setProperty(final Context context, final String name, final boolean value)
    {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().putBoolean(name, value).apply();
    }

    @SuppressWarnings("unused")
    private static synchronized boolean getProperty(final Context context, final String name, final boolean defaultValue)
    {
        try
        {
            return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getBoolean(name, defaultValue);
        }
        catch (final ClassCastException e)
        {
            setProperty(context, name, defaultValue);
            return defaultValue;
        }
    }

    // for storing int value
    private static synchronized void setProperty(final Context context, final String name, final int value)
    {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().putInt(name, value).apply();
    }

    @SuppressWarnings("unused")
    private static synchronized int getProperty(final Context context, final String name, final int defaultValue)
    {
        try
        {
            int value = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getInt(name, -100);
            if (value == -100)
            {
                setProperty(context, name, defaultValue);
                return defaultValue;
            }
            else
            {
                return value;
            }
        }
        catch (final ClassCastException e)
        {
            setProperty(context, name, defaultValue);
            return defaultValue;
        }
    }

    /******/

    public static void setInterviewURL(String interviewURL, Context context)
    {
	/*
	 * SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
	 * 0); SharedPreferences.Editor editor = settings.edit();
	 * editor.putString(PREFERENCES_INTERVIEW_URL, interviewURL);
	 * editor.apply();
	 */
        setProperty(context, PREFERENCES_INTERVIEW_URL, interviewURL);
    }

    public static String getInterviewURL(Context context)
    {
        // SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
        // 0);
        String interviewURL = getProperty(context, PREFERENCES_INTERVIEW_URL, null);// settings.getString(PREFERENCES_INTERVIEW_URL,
        // null);
        return interviewURL;
    }

    public static void setApiURL(String apiUrl, Context context)
    {
	/*
	 * SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
	 * 0); SharedPreferences.Editor editor = settings.edit();
	 * editor.putString(PREFERENCES_API_URL, apiUrl); editor.apply();
	 */
        setProperty(context, PREFERENCES_API_URL, apiUrl);
    }

    public static String getApiURL(Context context)
    {
        // SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
        // 0);
        String apiURL = getProperty(context, PREFERENCES_API_URL, null);// settings.getString(PREFERENCES_API_URL,
        // null);
        return apiURL;
    }

    public static void setUsername(String username, Context context)
    {
	/*
	 * SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
	 * 0); SharedPreferences.Editor editor = settings.edit();
	 * editor.putString(PREFERENCES_USERNAME, username); editor.apply();
	 */
        setProperty(context, PREFERENCES_USERNAME, username);
    }

    public static String getUsername(Context context)
    {
        // SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
        // 0);
        String username = getProperty(context, PREFERENCES_USERNAME, null);// settings.getString(PREFERENCES_USERNAME,
        // null);
        return username;
    }

    public static void setSharedKey(String sharedKey, Context context)
    {
	/*
	 * SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
	 * 0); SharedPreferences.Editor editor = settings.edit();
	 * editor.putString(PREFERENCES_SHARED_KEY, username); editor.apply();
	 */
        setProperty(context, PREFERENCES_SHARED_KEY, sharedKey);
    }

    public static String getSharedKey(Context context)
    {
        // SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
        // 0);
        String sharedKey = getProperty(context, PREFERENCES_SHARED_KEY, null);// settings.getString(PREFERENCES_SHARED_KEY,
        // null);
        return sharedKey;
    }

    public static void setAppVersion(String appVersion, Context context)
    {
        setProperty(context, PREFERENCES_APP_VERSION, appVersion);
    }

    public static String getAppVersion(Context context)
    {
        String appVersion = getProperty(context, PREFERENCES_APP_VERSION, null);
        return appVersion;
    }

    public static void setUniqueID(String uniqueID, Context context)
    {
        setProperty(context, PREFERENCES_UNIQUE_ID, uniqueID);
    }

    public static String getUniqueID(Context context)
    {
        String uniqueID = getProperty(context, PREFERENCES_UNIQUE_ID, null);
        return uniqueID;
    }

    public static void setDownloadURL(String downloadUrl, Context context)
    {
        setProperty(context, PREFERENCES_DOWNLOAD_URL, downloadUrl);
    }

    public static String getDownloadURL(Context context)
    {
        String downloadUrl = getProperty(context, PREFERENCES_DOWNLOAD_URL, null);
        return downloadUrl;
    }
    public static void clearOPGPreference(final Context context)
    {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(OPG_GEOFENCE_PREF, Context.MODE_PRIVATE).edit().clear().apply();
    }

    public static void setMySurevyAppAdminName(Context context,String name)
    {
        setProperty(context, PREFS_MYSURVEY_APP_ADMIN_NAME, name);
    }

    public static String getMySurevyAppAdminName(Context context)
    {
        return getProperty(context, PREFS_MYSURVEY_APP_ADMIN_NAME, null);
    }

    public static void setMySurevyAppSharedKey(Context context,String name)
    {
        setProperty(context, PREFS_MYSURVEY_APP_SHARED_KEY, name);
    }

    public static String getMySurevyAppSharedKey(Context context)
    {
        return getProperty(context, PREFS_MYSURVEY_APP_SHARED_KEY, null);
    }

    /**
     * Sets the status of the geofencing triggering or not
     * @param context
     * @param isGeoFencingTriggered
     */
    public static void setIsGeofencingTriggered(Context context,Boolean isGeoFencingTriggered){
        setProperty(context,PREFS_IS_GEOFENCING_TRIGGERED,isGeoFencingTriggered);
    }

    /**
     * Gives the status of the geofencing triggering or not
     * @param context
     * @return
     */
    public static boolean getIsGeofencingTriggered(Context context){
        return getProperty(context,PREFS_IS_GEOFENCING_TRIGGERED,false);
    }

    /**
     * Sets the app is debuggable or not
     * @param context
     * @param is_app_debugable
     */
    public static void setIsAppDebugable(Context context,Boolean is_app_debugable){
        setProperty(context,IS_APP_DEBUGABLE,is_app_debugable);
    }

    /**
     * tells whether the app is debuggable or not
     * @param context
     * @return
     */
    public static boolean getIsAppDebugable(Context context){
        return getProperty(context,IS_APP_DEBUGABLE,false);
    }

    /**
     * Sets the List<OPGGeofenceSurvey> to the sharedpreferences
     * @param context
     * @param surveyList
     */
    public static void setGeofencingSurveys(Context context, List<OPGGeofenceSurvey> surveyList)
    {
        Gson gson = new Gson();
        String json = gson.toJson(surveyList);
        setProperty(context, PREFS_GEOFENCING_SURVEYS, json);
    }

    /**
     * Gets the geofencingsurveys List<OPGGeofenceSurvey> from the sharedpreferences
     * @param context
     * @return
     */
    public static List<OPGGeofenceSurvey> getGeofencingSurveys(Context context)
    {
        List<OPGGeofenceSurvey> resultGeofenceSurveys = new ArrayList<>();
        String jsonString = getProperty(context, PREFS_GEOFENCING_SURVEYS, null);
        if(jsonString != null){
            Gson gson = new Gson();
            Type listType = new TypeToken<List<OPGGeofenceSurvey>>()
            {
            }.getType();
            return gson.fromJson(jsonString, listType);
        }
        return resultGeofenceSurveys;
    }

    public static void setLoginType(Context context,int type)
    {
        setProperty(context, LOGIN_TYPE, type);
    }

    public static int getLoginType(Context context)
    {
        return getProperty(context, LOGIN_TYPE, -1);
    }
    public static void setFacebookToken(Context context,String  token)
    {
        setProperty(context, FACEBOOK_TOKEN, token);
    }

    public static String getFacebookToken(Context context)
    {
        return getProperty(context, FACEBOOK_TOKEN, EMPTY_STRING);

    }
    public static void setGoogleToken(Context context,String  token)
    {
        setProperty(context, GOOGLE_TOKEN, token);
    }

    public static String getGoogleToken(Context context)
    {
        return getProperty(context, GOOGLE_TOKEN, EMPTY_STRING);

    }

    public static void setAppLoginUsername(Context context,String  token)
    {
        setProperty(context, APP_LOGIN_USERNAME, token);
    }

    public static String getAppLoginUsername(Context context)
    {
        return getProperty(context, APP_LOGIN_USERNAME, EMPTY_STRING);

    }
    public static void setAppLoginPassword(Context context,String  token)
    {
        setProperty(context, APP_LOGIN_PASSWORD, token);
    }

    public static String getAppLoginPassword(Context context)
    {
        return getProperty(context, APP_LOGIN_PASSWORD, EMPTY_STRING);

    }

    public static void saveOPGGeofenceSurveys(Context context,List<OPGGeofenceSurvey> geofenceSurveys)
    {
        Gson gson = new Gson();
        JSONArray jsonArray = new JSONArray();
        for (OPGGeofenceSurvey opgGeofenceSurvey : geofenceSurveys){
            try {
                JSONObject jsonObject = new JSONObject(gson.toJson(opgGeofenceSurvey));
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        context.getSharedPreferences(OPG_GEOFENCE_PREF, Context.MODE_PRIVATE).edit().putString(PREF_GEOFENCE_KEY, jsonArray.toString()).apply();
    }

    public static List<OPGGeofenceSurvey> getOPGGeofenceSurveys(Context context)
    {
        Gson gson = new Gson();
        List<OPGGeofenceSurvey> opgGeofenceSurveyList = new ArrayList<>();
        String jsonString = context.getSharedPreferences(OPG_GEOFENCE_PREF,Context.MODE_PRIVATE).getString(PREF_GEOFENCE_KEY,"");
        if(jsonString.length() != 0){
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    opgGeofenceSurveyList.add(gson.fromJson(jsonObject.toString(),OPGGeofenceSurvey.class));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return opgGeofenceSurveyList;
    }

    public static boolean clearOPGGeofenceSurveys(Context context)
    {
        return context.getSharedPreferences(OPG_GEOFENCE_PREF,Context.MODE_PRIVATE).edit().clear().commit();
    }
}
