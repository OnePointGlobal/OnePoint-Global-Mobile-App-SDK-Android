package OnePoint.CordovaPlugin;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.allatori.annotations.DoNotRename;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.opg.sdk.OPGR;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import static com.opg.sdk.OPGSDKConstant.ACCESS_COARSE_LOCATION_PERMISSION;
import static com.opg.sdk.OPGSDKConstant.ACCESS_FINE_LOCATION_PERMISSION;
import static com.opg.sdk.OPGSDKConstant.APP_DETAILS_SETTINGS;
import static com.opg.sdk.OPGSDKConstant.DENY;
import static com.opg.sdk.OPGSDKConstant.GPS_ENABLE_GPS_ALERT_KEY;
import static com.opg.sdk.OPGSDKConstant.LOCATION_PERMISSION_MSG;
import static com.opg.sdk.OPGSDKConstant.PACKAGE;
import static com.opg.sdk.OPGSDKConstant.RUNTIME_PERMISSION;
import static com.opg.sdk.OPGSDKConstant.SETTINGS;
import static com.opg.sdk.OPGSDKConstant.STRING;

//import OnePoint.Logging.LogManager;

//import OnePoint.CordovaPlugin.GeoFencingPlugin.GeoFencingTask;

@DoNotRename
public class EnableGPSPlugin extends RootPlugin
		implements ConnectionCallbacks, OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
	private CallbackContext callback;
	String username, password, version;
	Date signInDate;
	Context context;
	Activity activity;
	private AlertDialog gpsDialog;
	private final int ENABLE_GPS_FROM_SETTINGS = 1;

	final private static int REQUEST_CODE_ASK_LOCATION_PERMISSIONS = 125;


	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
	public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
	protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
	protected final static String LOCATION_KEY = "location-key";
	protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

	protected final static String GETTING_LOCATIOON = "Getting Location..";
	protected final static String UNABLE_GET_LOCATION = "Unable to get location, try again.";
	protected final static String OK = "OK";
	protected final static String CANCEL = "Cancel";
	protected GoogleApiClient mGoogleApiClient;
	protected LocationRequest mLocationRequest;
	protected Location mCurrentLocation;
	protected String mLatitudeLabel;
	protected String mLongitudeLabel;
	protected String mLastUpdateTimeLabel;
	protected Boolean mRequestingLocationUpdates;
	protected String mLastUpdateTime;
	private ProgressDialog pDialog;

	@Override@DoNotRename
	public boolean execute(String action, final CordovaArgs args, final CallbackContext callbackContext) {
		this.context = this.cordova.getActivity();
		this.activity = this.cordova.getActivity();
		init(context, callbackContext);
		this.callback = callbackContext;
		buildGoogleApiClient();
		mGoogleApiClient.connect();

		try {
			if (action.equalsIgnoreCase(ACTION_ENABLE_GPS)) {
				getLocation();
			} else {
				callback.error(getReplyJsonString(102));
			}
		} catch (Exception e) {
			e.printStackTrace();
			//LogManager.getLogger(getClass()).error(e.getMessage());
			callback.error(UNABLE_GET_LOCATION);
//			return false;
		}
		return true;
	}

	private boolean checkPermission() {
		if(ActivityCompat.checkSelfPermission(this.context, ACCESS_FINE_LOCATION_PERMISSION) == 0 || ActivityCompat.checkSelfPermission(this.context, ACCESS_COARSE_LOCATION_PERMISSION) == 0) {
			return true;
		}else{
			if(!ActivityCompat.shouldShowRequestPermissionRationale(cordova.getActivity(), ACCESS_FINE_LOCATION_PERMISSION)
					&& !ActivityCompat.shouldShowRequestPermissionRationale(cordova.getActivity(), ACCESS_COARSE_LOCATION_PERMISSION)){
				showAlertDialog();
			}else {
				ActivityCompat.requestPermissions(cordova.getActivity(),
						new String[]{ACCESS_COARSE_LOCATION_PERMISSION, ACCESS_FINE_LOCATION_PERMISSION},
						REQUEST_CODE_ASK_LOCATION_PERMISSIONS);
			}
			return false;
		}
	}

	private void getLocation(){
		try {
			if(Build.VERSION.SDK_INT < 23 || (Build.VERSION.SDK_INT >=23 && checkPermission())) {
				if (isGPSEnabled()) {
					pDialog = new ProgressDialog(context);
					pDialog.setMessage(GETTING_LOCATIOON);
					pDialog.setIndeterminate(false);
					pDialog.setCancelable(true);
					pDialog.show();
					final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							startLocationUpdates();
						}
					}, 1000);
				} else {
					enableGPS();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			//LogManager.getLogger(getClass()).error(e.getMessage());
			callback.error(UNABLE_GET_LOCATION);
//			return false;
		}
	}

	private void showAlertDialog() {
		final android.support.v7.app.AlertDialog.Builder builder;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			builder = new android.support.v7.app.AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
		} else {
			builder = new android.support.v7.app.AlertDialog.Builder(context);
		}
		builder.setTitle(OPGR.getString(context,STRING,RUNTIME_PERMISSION))
				.setMessage(OPGR.getString(context,STRING,LOCATION_PERMISSION_MSG))
				.setPositiveButton(OPGR.getString(context,STRING,SETTINGS), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						goToSettingPage();
					}
				})
				.setNegativeButton(OPGR.getString(context,STRING,DENY), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.setIcon(android.R.drawable.ic_dialog_alert)
				.show();
	}

	private void goToSettingPage(){
		Intent intent = new Intent();
		intent.setAction(APP_DETAILS_SETTINGS);
		Uri uri = Uri.fromParts(PACKAGE, cordova.getActivity().getPackageName(), null);
		intent.setData(uri);
		cordova.getActivity().startActivity(intent);
	}


	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(context).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
		createLocationRequest();
	}

	protected void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}


	private void enableGPS() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						enableGPSfromSettings();
						break;
					case DialogInterface.BUTTON_NEGATIVE:
						PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, getReplyJsonString(105));
						pluginResult.setKeepCallback(true);
						callback.error(UNABLE_GET_LOCATION);
						// callback.success(getReplyJsonString(105));
						break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(OPGR.getString(context,STRING,GPS_ENABLE_GPS_ALERT_KEY))
				.setPositiveButton(OK, dialogClickListener).setNegativeButton(CANCEL, dialogClickListener);
		if (gpsDialog == null)
			gpsDialog = builder.create();
		if (!gpsDialog.isShowing()) {
			gpsDialog.show();
		}

	}

	public void enableGPSfromSettings() {

		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		this.cordova.startActivityForResult(this, intent, ENABLE_GPS_FROM_SETTINGS);
	}

	@Override
	public void onLocationChanged(Location arg0) {
		mCurrentLocation = arg0;
		sendCallback();


	}
	protected final static String LATITUDE = "latitude";
	protected final static String LONGITUDE = "longitude";
	private void sendCallback() {
		try {

			JSONObject currentLocationObject = new JSONObject();
			currentLocationObject.put(LATITUDE, mCurrentLocation.getLatitude());
			currentLocationObject.put(LONGITUDE, mCurrentLocation.getLongitude());
			callback.success(currentLocationObject);
			stopLocationUpdates();
			pDialog.dismiss();


		} catch (Exception e) {
			pDialog.dismiss();
//			stopLocationUpdates();

//			mGoogleApiClient.disconnect();
			e.printStackTrace();
			//LogManager.getLogger(getClass()).error(e.getMessage());
			callback.error(e.getLocalizedMessage());
		}

	}

	protected void stopLocationUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
		mGoogleApiClient.disconnect();
	}

	@Override@DoNotRename
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override@DoNotRename
	public void onConnected(Bundle arg0) {
		if(ActivityCompat.checkSelfPermission(this.context, ACCESS_FINE_LOCATION_PERMISSION) == 0 || ActivityCompat.checkSelfPermission(this.context, ACCESS_COARSE_LOCATION_PERMISSION) == 0) {
			this.mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(this.mGoogleApiClient);
		}
	}

	@Override@DoNotRename
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();

	}

	@Override@DoNotRename
	public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
		super.onRequestPermissionResult(requestCode, permissions, grantResults);
		switch (requestCode){
			case REQUEST_CODE_ASK_LOCATION_PERMISSIONS:
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					if(ActivityCompat.checkSelfPermission(this.context, ACCESS_FINE_LOCATION_PERMISSION) == 0 || ActivityCompat.checkSelfPermission(this.context, ACCESS_COARSE_LOCATION_PERMISSION) == 0) {
						mGoogleApiClient.connect();
						getLocation();
					}
				}
				return;
		}
	}

	@Override@DoNotRename
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			switch (requestCode) {
				case ENABLE_GPS_FROM_SETTINGS:
					mGoogleApiClient.connect();
					getLocation();
					break;

			}
		}
	}

	protected void startLocationUpdates() {

		if (ActivityCompat.checkSelfPermission(this.context, ACCESS_FINE_LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, ACCESS_COARSE_LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
	}

}
