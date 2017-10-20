/*
 * Copyright (c) http://www.onepointsurveys.com/ All rights reserved.
 * Use is subject to license terms.
 * Author : OnePoint Developers
 */

package OnePoint.Common;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import static com.opg.sdk.OPGSDKConstant.BACK_SLASH;
import static com.opg.sdk.OPGSDKConstant.CLOSE_SQUARE_BRACKET;
import static com.opg.sdk.OPGSDKConstant.COLON;
import static com.opg.sdk.OPGSDKConstant.COMA;

public class OPGSharedPreference {

	@SuppressWarnings("unused")
	private static final String TAG = "OPGSharedPreference";
	private static final String PREFS_NAME = "OPGSharedPreference";
	private static final String PREFS_NAME_OFFLINE_SURVEYS = "OPGSharedPreferenceOfflineSurveys";
	private static final String PREFERENCES_EULA = "eula";
	private static final String PREFERENCES_USERNAME = "username";
	private static final String PREFERENCES_PASSWORD = "password";
	private static final String PREFERENCES_VERSION = "version";
	private static final String PREFERENCES_SESSION = "sessionId";
	private static final String PREFERENCES_URL = "url";
	private static final String PREFERENCES_INTERVIEW_URL = "InterviewUrl";
	private static final String PREFERENCES_ISLOGGED = "isLogged";
	private static final String PREFERENCES_LAST_UPDATE = "last_update";
	private static final String PREFERENCES_MARITAL_STATUS = "marital_status";
	private static final String PREFERENCES_LANGUAGE = "language";
	private static final String PREFERENCES_INITIAL_LOADING = "initial_loading";
	private static final String PREFS_KEY_CAMERA_FILE_COUNT = "PrefsKeyCameraFileCount";
	private static final String PREFERENCES_APPDOWNLOADURL = "appDownloadURL";
	private static final String PREFERENCES_SURVEY_IDS = "surveyID";
	private static final String PREFERENCES_ENABLE_SURVEYIDS = "enablesurveyIDs";
	private static final String PREFERENCES_POPUP_SURVEYIDS = "popUpsurveyIDs";
	private static final String PREFERENCES_PANEL_ID = "panelID";
	private static final String PREFERENCES_MEDIA_COUNT = "mediaCount";
	private static final String PREFERENCES_LOCATION_UPDATE = "locationStatus";

	private static final String PREFS_PANELLIST_DATA = "PanellistDataPlugin";
	private static final String PREFS_PANELS = "PanelsPlugin";
	private static final String PREFS_SURVEYS = "SurveysPlugin";
	private static final String PREFS_SCRIPTS = "ScriptPlugin";
	private static final String PREFS_CHECK_FOR_UPDATES = "CheckForUpdates";
	private static final String PREFS_CACHE_ASSETS = "CacheAssets";
	private static final String PREFS_LOCAL_MEDIA_ID = "CacheAssets";
	private static final String PREFS_UPLOAD_RESULTS = "UploadResults";
	private static final String PREFS_DEVICE_TOKEN = "DeviceToken";
	private static final String PREFS_OFFLINE_MEDIA_PATH = "offlineMediaPath";
	private static final String PREFS_MEDIA_FILE = "mediaFileFormat";
	private static final String PREFS_OFFLINE_SURVEY_LIST = "OfflineSurveyResultsList";

	private OPGSharedPreference() {
	}

	public static void storeDeviceToken(Activity activity, String username) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putString(PREFS_DEVICE_TOKEN, username);
		editor.apply();
	}


	public static String readDeviceToken(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		String userName = settings.getString(PREFS_DEVICE_TOKEN, "");
		return userName;
	}


	public static void storeOfflineMediaPath(Activity activity, String offlineMediaPath) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putString(PREFS_OFFLINE_MEDIA_PATH, offlineMediaPath);
		editor.apply();
	}


	public static String readOfflineMediaPath(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		String offlineMediaPath = settings.getString(PREFS_OFFLINE_MEDIA_PATH, "");
		return offlineMediaPath;
	}
	public static void setMediaCount(Activity activity, String mediaCount) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putString(PREFERENCES_MEDIA_COUNT, mediaCount);
		editor.apply();
	}

	public static String getMediaCount(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		String mediaCount = settings.getString(PREFERENCES_MEDIA_COUNT, "");
		return mediaCount;
	}


	public static void setLocationStatus(Activity activity, String status) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putString(PREFERENCES_LOCATION_UPDATE, status);
		editor.apply();
	}

	public static String getLocationStatus(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		String status = settings.getString(PREFERENCES_LOCATION_UPDATE, "");
		return status;
	}

	public static void storePanelID(Activity activity, String panel) {

		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putString(PREFERENCES_PANEL_ID, panel);
		editor.apply();
	}

	public static void storeEnableSurveyIDs(Activity activity, String enable) {

		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putString(PREFERENCES_ENABLE_SURVEYIDS, enable);
		editor.apply();
	}

	public static String readEnableSurveyIDs(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		String enable = settings.getString(PREFERENCES_ENABLE_SURVEYIDS, "");
		return enable;
	}

	public static void storePopUpSurveyIDs(Activity activity, String popup) {

		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putString(PREFERENCES_POPUP_SURVEYIDS, popup);
		editor.apply();
	}

	public static String readPopUpSurveyIDs(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		String popup = settings.getString(PREFERENCES_POPUP_SURVEYIDS, "");
		return popup;
	}



	public static String readPanelID(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		String panelid = settings.getString(PREFERENCES_PANEL_ID, "");
		return panelid;
	}


	public static void storeSurveyID(Activity activity, String surveyIDs) {

		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putString(PREFERENCES_SURVEY_IDS, surveyIDs);
		editor.apply();
	}

	public static String readSurveyID(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		String name = settings.getString(PREFERENCES_SURVEY_IDS, "");
		// String[] values =name.split(",");
		//
		// List<String> list = Arrays.asList(values);
		// Set<String> set = new HashSet<String>(list);
		// String Surveynames[] = new String[set.size()];
		// set.toArray(Surveynames);
		return name;
	}

	public static void storeCurrentSurvey(Activity activity, String surveyList) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME_OFFLINE_SURVEYS, 0);
		Editor editor = settings.edit();
		editor.putString(PREFS_OFFLINE_SURVEY_LIST, surveyList);
		editor.apply();

	}

	public static void storeCurrentSurvey(Activity activity, String surveyid, String surveyname) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME_OFFLINE_SURVEYS, 0);
		Editor editor = settings.edit();
		String temp = settings.getString(PREFS_OFFLINE_SURVEY_LIST, "");
		String mjsonobj = "{\"id\":" + "\"" + surveyid + "\"" + "," + "\"Name\":" + "\"" + surveyname + "\"" + "}";
		boolean value;

		if (temp.equals("")) {
			value = false;
		} else {
			value = compareString(temp, surveyid);
		}

		if (value) {

		} else {
			if (temp.equals("")) {
				temp = mjsonobj;
			} else {
				temp = temp + "," + mjsonobj;
			}

		}
		editor.putString(PREFS_OFFLINE_SURVEY_LIST, temp);
		editor.apply();
	}

	public static void clearOfflineSurveyList(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME_OFFLINE_SURVEYS, 0);
		Editor editor = settings.edit();
		editor.clear();
		editor.apply();
	}

	public static boolean compareString(String temp, String id) {

		String[] strAry = temp.split(COMA);

		for (int i = 0; i < strAry.length; i++) {

			String[] strArr2 = strAry[i].split(COLON);
			String xyz = strArr2[1].replaceAll(BACK_SLASH, "").replaceAll(CLOSE_SQUARE_BRACKET, "");
			if (xyz.equals(id)) {
				return true;
			}

		}

		return false;

	}

	public static String readCurrentSurvey(Activity activity) {

		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME_OFFLINE_SURVEYS, 0);
		String name = settings.getString(PREFS_OFFLINE_SURVEY_LIST, "");
		System.out.println(name);

		return name;

	}

	public static void setMediaFileFormat(Activity activity, String mediaformat) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putString(PREFS_MEDIA_FILE, mediaformat);
		editor.apply();

	}

	public static String getMediaFileFormat(Activity activity) {

		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		String name = settings.getString(PREFS_MEDIA_FILE, "");
		return name;

	}

	public static void storeAppDownloadURL(String downloadUrl, Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putString(PREFERENCES_APPDOWNLOADURL, downloadUrl);
		editor.apply();
	}

	public static String getAppDownloadURL(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		String sessionId = settings.getString(PREFERENCES_APPDOWNLOADURL, null);
		return sessionId;
	}

	public static void storeUserName(Activity activity, String username) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putString(PREFERENCES_USERNAME, username);
		editor.apply();
	}

	public static String readUserName(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		String userName = settings.getString(PREFERENCES_USERNAME, "");
		return userName;
	}

	public static void storePassword(Activity activity, String password) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putString(PREFERENCES_PASSWORD, password);
		editor.apply();
	}

	public static String readPassword(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		String result = settings.getString(PREFERENCES_PASSWORD, "");
		return result;
	}

	public static void storeAppVersion(Activity activity, String password) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putString(PREFERENCES_VERSION, password);
		editor.apply();
	}

	public static String readAppVersion(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		String result = settings.getString(PREFERENCES_VERSION, "");
		return result;
	}

	public static String getSessionId(Activity activity) {
		System.out.println(activity);
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		String sessionId = settings.getString(PREFERENCES_SESSION, null);
		return sessionId;
	}

	public static void storeSessionId(String sessionId, Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putString(PREFERENCES_SESSION, sessionId);
		editor.apply();
	}

	public static void storeURL(String sessionId, Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putString(PREFERENCES_URL, sessionId);
		editor.apply();
	}

	public static String getURL(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		String sessionId = settings.getString(PREFERENCES_URL, null);
		return sessionId;
	}

	public static void storeInterviewURL(String interviewURL, Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putString(PREFERENCES_INTERVIEW_URL, interviewURL);
		editor.apply();
	}

	public static String getInterviewURL(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		String interviewURL = settings.getString(PREFERENCES_INTERVIEW_URL, null);
		return interviewURL;
	}

	public static int getMaritalStatus(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		int status = settings.getInt(PREFERENCES_MARITAL_STATUS, 11);
		return status;
	}

	public static void storeMaritalStatus(int status, Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putInt(PREFERENCES_MARITAL_STATUS, status);
		editor.apply();
	}

	public static int getLanguage(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		int status = settings.getInt(PREFERENCES_LANGUAGE, 11);
		return status;
	}

	public static void storeLanguage(int status, Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putInt(PREFERENCES_LANGUAGE, status);
		editor.apply();
	}

	public static boolean readIsLoggedIn(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		boolean result = settings.getBoolean(PREFERENCES_ISLOGGED, false);
		return result;
	}

	public static void storeIsLoggedIn(Activity activity, boolean isLogged) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putBoolean(PREFERENCES_ISLOGGED, isLogged);
		editor.apply();
	}

	public static void storeLastUpdate(Activity activity, String lastUpdate) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putString(PREFERENCES_LAST_UPDATE, lastUpdate);
		editor.apply();
	}

	public static String readLastUpdate(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		String result = settings.getString(PREFERENCES_LAST_UPDATE, "");
		return result;
	}

	public static void setEULAState(boolean status, Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putBoolean(PREFERENCES_EULA, status);
		editor.apply();
	}

	public static boolean getEULAState(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		boolean status = settings.getBoolean(PREFERENCES_EULA, false);
		return status;
	}

	public static boolean readIsLoaded(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		boolean result = settings.getBoolean(PREFERENCES_INITIAL_LOADING, false);
		return result;
	}

	public static void storeIsLoaded(Activity activity, boolean isLoaded) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putBoolean(PREFERENCES_INITIAL_LOADING, isLoaded);
		editor.apply();
	}

	public static void incrementCameraFileCount(Activity activity) {
		SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, 0);
		int count = prefs.getInt(PREFS_KEY_CAMERA_FILE_COUNT, 0) + 1;
		Editor editor = prefs.edit();
		editor.putInt(PREFS_KEY_CAMERA_FILE_COUNT, count);
		editor.apply();
	}

	public static int getCameraFileCount(Activity activity) {
		SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, 0);
		return prefs.getInt(PREFS_KEY_CAMERA_FILE_COUNT, 0);
	}

	/**
	 * Amit Added clear Preference method to clear the whole preferences while
	 * Logging out
	 */

	public static void clearPrefernces(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.clear();
		editor.apply();
	}

	public static void setPanellistDataPlugin(boolean status, Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putBoolean(PREFS_PANELLIST_DATA, status);
		editor.apply();
	}

	public static boolean getPanellistDataPlugin(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		boolean status = settings.getBoolean(PREFS_PANELLIST_DATA, false);
		return status;
	}

	public static void setPanelsPlugin(boolean status, Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putBoolean(PREFS_PANELS, status);
		editor.apply();
	}

	public static boolean getPanelsPlugin(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		boolean status = settings.getBoolean(PREFS_PANELS, false);
		return status;
	}

	public static void setSurveysPlugin(boolean status, Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putBoolean(PREFS_SURVEYS, status);
		editor.apply();
	}

	public static boolean getSurveysPlugin(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		boolean status = settings.getBoolean(PREFS_SURVEYS, false);
		return status;
	}

	public static void setScriptPlugin(boolean status, Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putBoolean(PREFS_SCRIPTS, status);
		editor.apply();
	}

	public static boolean getScriptPlugin(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		boolean status = settings.getBoolean(PREFS_SCRIPTS, false);
		return status;
	}

	public static void setCheckForUpdatesPlugin(boolean status, Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putBoolean(PREFS_CHECK_FOR_UPDATES, status);
		editor.apply();
	}

	public static boolean getCheckForUpdatesPlugin(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		boolean status = settings.getBoolean(PREFS_CHECK_FOR_UPDATES, false);
		return status;
	}

	public static void setUploadResultsPlugin(boolean status, Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putBoolean(PREFS_UPLOAD_RESULTS, status);
		editor.apply();
	}

	public static boolean getUploadResultsPlugin(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		boolean status = settings.getBoolean(PREFS_UPLOAD_RESULTS, false);
		return status;
	}

	public static void setCacheAssets(boolean status, Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putBoolean(PREFS_CACHE_ASSETS, status);
		editor.apply();
	}

	public static boolean getCacheAssets(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		boolean status = settings.getBoolean(PREFS_CACHE_ASSETS, false);
		return status;
	}

	public static void incrementLocalMediaID(Activity activity) {
		SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, 0);
		int count = prefs.getInt(PREFS_LOCAL_MEDIA_ID, 0) + 1;
		Editor editor = prefs.edit();
		editor.putInt(PREFS_LOCAL_MEDIA_ID, count);
		editor.apply();
	}

	public static int getLocalMediaID(Activity activity) {
		SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, 0);
		return prefs.getInt(PREFS_LOCAL_MEDIA_ID, 0);
	}

	public static void setMedia(String mediaID, String path, Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putString(mediaID, path);
		editor.apply();
	}

	public static String getMedia(String mediaID, Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		return settings.getString(mediaID, "");
	}
}
