package OnePoint.CordovaPlugin;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.opg.sdk.OPGSDKConstant;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

//import com.opg.main.Utils;

/*import com.opg.main.OPGApplication;
import com.opg.main.OPGDBHelper;*/

//import OnePoint.Logging.LogManager;

public class RootPlugin extends CordovaPlugin {
public static final String DOLLAR_SIGN = "$";
	public static final String ACTION_NETWORK = "_network";
	public static final String ACTION_DATABASE = "_database";
	public static final String ACTION_LOGIN = "login";
	public static final String ACTION_PANELS = "panels";
	public static final String ACTION_FETCH_PROFILE = "fetchProfile";
	public static final String ACTION_EDIT_PROFILE = "editProfile";
	public static final String ACTION_CHANGE_PASSWORD = "changePassword";
	public static final String ACTION_FORGOT_PASSWORD = "forgotPassword";
	public static final String ACTION_ENABLE_GPS = "enableGPS";
	public static final String ACTION_SURVEY = "survey";
	public static final String ACTION_MEDIA_UPLOAD = "mediaUpload";

/*	public void setDatabase(String name) {
		OPGDBHelper.setDatabaseName(name);
		//LogManager.getLogger(getClass()).error(name + " SETUP STARETED!");
		OPGDBHelper.getInstance().getWritableDatabase();
		//LogManager.getLogger(getClass()).error(name + " SETUP ENDED!");
	}*/
	public static final String ACTION_MEDIA_DOWNLOAD = "mediaDownload";
	public static final String ACTION_PICK_MEDIA = "local";
	public static final String ACTION_LOCAL_STORAGE_GET = "get";
	public static final String ACTION_LOCAL_STORAGE_SET = "set";
	public static final String ACTION_TAKE_SURVEY = "takeSurvey";
	public static final String ACTION_UPDATE_TAKE_SURVEY = "updateTakeSurvey";
	public static final String ACTION_LOGOUT = "logout";
	public static final String ACTION_CHECK_FOR_UPDATES = "checkForUpdatesPlugin";
	public static final String ACTION_KEYBOARD_SHOW = "show";
	public static final String ACTION_KEYBOARD_HIDE = "hide";
	public static final String ACTION_FETCH_ABSELUTE_PATH = "_getPath";
	public static final String ACTION_APPUPDATE = "appUpdate";
	public static final String ACTION_OPEN_IN_BROWSER = "openWindow";
	public static final String ACTION_UPLOAD_OFFLINE_SURVEY_RESULTS = "uploadOfflineResults";
	public static final String ACTION_LOAD_NOTIFICATIONS = "loadNotifications";
	public static final String ACTION_DELETE_NOTIFICATIONS = "deleteNotification";
	public static final String ACTION_UPDATE_NOTIFICATIONS = "updateNotification";
	public static final String ACTION_LOAD_GEOLOCATIONS = "loadGeoLocations";
	public static final String ACTION_SEND_LOCAL_NOTIFICATIONS = "sendLocalNotifications";
	public static final String ACTION_MEDIA_PICK_MAGE_FROM_CAMERA = "pickImageFromCamera";
	public static final String ACTION_MEDIA_PICK_IMAGE_FROM_GALLERY = "pickImageFromGallery";
	public static final String ACTION_MEDIA_PICK_AUDIO_FROM_GALLERY = "pickAudioFromGallery";
	public static final String ACTION_MEDIA_START_RECORDING_AUDIO = "startRecordingAudio";
	public static final String ACTION_MEDIA_STOP_RECORDING_AUDIO = "stopRecordingAudio";
	public static final String ACTION_MEDIA_START_PLAYING_RECORDED_AUDIO = "startPlayingRecordedAudio";
	public static final String ACTION_MEDIA_STOP_PALYING_RECORDED_AUDIO = "stopPlayingRecordedAudio";
	public static final String ACTION_MEDIA_PICK_VIDEO_FROM_CAMERA = "pickVideoFromCamera";
	public static final String ACTION_MEDIA_PICK_VIDEO_FROM_GALLERY = "pickVideoFromGallery";
	public static final String ACTION_MEDIA_PLAY_VIDEO_FROM_SELECTEDPATH = "playVideoSelectedPath";
	public static final String ACTION_MEDIA_SHOW_IMAGE_FROM_PATH = "showImageFromPath";
	private static final String CODE = "code";
	private static final String MESSAGE = "message";
	private static CallbackContext callback;
	/* REPLY MESSAGE STRINGS */
	private final String SUCCESS = "Success";
	private final String INVALID_REQUEST = "Invalid Request";
	private final String INVALID_ACTION = "Invalid Action Name";
	private final String NO_INTERNET = "No Internet Connection";
	private final String INVALID_USR_PWD = "Wrong Username/Password";
	private final String ERROR = "Error";
	private final String UNKNOWN_ERROR = "Unknown Error";

	public static void notifyNetworkStatus(int code, String message) {
		if (callback != null) {
			JSONObject jobj = new JSONObject();
			try {
				jobj.put(CODE, code);
				jobj.put(MESSAGE, message);
				callback.error( DOLLAR_SIGN + jobj.toString());
			} catch (JSONException e) {
				e.printStackTrace();
				//LogManager.getLogger("RootPlugin").error(e.getMessage());
			}
		} else {
			//Log.d("RootPlugin", "NO CALLBACK REGISTERED FOR NETWORK STATUS!");
		}
	}

	public void init(Context con, CallbackContext callbackContext) {
		//LogManager.Configure("VitaccessLog.xml");
		//OPGDBHelper.mContext = con;
		RootPlugin.callback = callbackContext;
		//int array_id = con.getResources().getIdentifier("databases_array", "array", con.getPackageName());
		/*String[] databases_array = con.getResources().getStringArray(array_id);
		if (!OPGSharedPreference.readIsLoaded((Activity) OPGApplication.getActivityContext())) {
			for (String dbName : databases_array) {
				setDatabase(dbName);
			}
			OPGSharedPreference.storeIsLoaded((Activity) OPGApplication.getActivityContext(), true);
		}*/
	}

	protected void sendResult(final String result) {

		PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, result);
		pluginResult.setKeepCallback(true);
		callback.sendPluginResult(pluginResult);

	}

	public String getReplyJsonString(int code) {
		JSONObject jobj = new JSONObject();
		try {
			jobj.put(CODE, code);
			switch (code) {
			case 100:
				jobj.put(MESSAGE, SUCCESS);
				break;
			case 101:
				jobj.put(MESSAGE, NO_INTERNET);
				break;
			case 102:
				jobj.put(MESSAGE, INVALID_REQUEST);
				break;
			case 103:
				jobj.put(MESSAGE, INVALID_USR_PWD);
				break;
			case 105:
				jobj.put(MESSAGE, ERROR);
				break;
			case 106:
				jobj.put(MESSAGE, INVALID_ACTION);
				break;
			default:
				jobj.put(MESSAGE, UNKNOWN_ERROR);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//LogManager.getLogger(getClass()).error(e.getMessage());
		}
		return jobj.toString();
	}

	public boolean isOnline() {

		ConnectivityManager cm = (ConnectivityManager) cordova.getActivity().getSystemService(OPGSDKConstant.CONNECTIVITY_SERVICE_STR);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public boolean isGPSEnabled() {
		LocationManager locationManager = (LocationManager) cordova.getActivity()
				.getSystemService(Context.LOCATION_SERVICE);

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			return true;
		} else {
			return false;
		}
	}

	public String readPanelListCachePathFromDevice() {
		/*try {

			File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
					+ Utils.PATH + File.separator + "OPGData" + File.separator + "opgPanelListIdData.txt");
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow;
			}
			String data = (aBuffer);
			myReader.close();

			return data;

		} catch (Exception e) {
			return null;
		}*/
		return null;
	}
}
