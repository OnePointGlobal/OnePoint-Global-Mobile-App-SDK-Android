/**
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) Matt Kane 2010
 * Copyright (c) 2011, IBM Corporation
 * Copyright (c) 2013, Maciej Nux Jaros
 */
package com.phonegap.plugins.barcodescanner;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.allatori.annotations.DoNotRename;
import com.opg.sdk.BuildConfig;
import com.opg.sdk.OPGR;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.OPGBaseActivity;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.opg.sdk.OPGSDKConstant.APP_DETAILS_SETTINGS;
import static com.opg.sdk.OPGSDKConstant.CAMERA_PERMISSION;
import static com.opg.sdk.OPGSDKConstant.CAMERA_PERMISSION_MSG;
import static com.opg.sdk.OPGSDKConstant.DENY;
import static com.opg.sdk.OPGSDKConstant.PACKAGE;
import static com.opg.sdk.OPGSDKConstant.RUNTIME_PERMISSION;
import static com.opg.sdk.OPGSDKConstant.SETTINGS;
import static com.opg.sdk.OPGSDKConstant.STRING;

/**
 * This calls out to the ZXing barcode reader and returns the result.
 *
 * @sa https://github.com/apache/cordova-android/blob/master/framework/src/org/apache/cordova/CordovaPlugin.java
 */
public class BarcodeScanner extends CordovaPlugin {
    public static final int REQUEST_CODE = 0x0ba7c0de;

    private static final String SCAN = "scan";
    private static final String ENCODE = "encode";
    private static final String CANCELLED = "cancelled";
    private static final String FORMAT = "format";
    private static final String TEXT = "text";
    private static final String DATA = "data";
    private static final String TYPE = "type";
    //private static final String SCAN_INTENT = "com.phonegap.plugins.barcodescanner.SCAN";
    private static final String SCAN_INTENT = "com.google.zxing.client.android.SCAN";

    private static final String ENCODE_DATA = "ENCODE_DATA";
    private static final String ENCODE_TYPE = "ENCODE_TYPE";
    private static final String ENCODE_INTENT = "com.phonegap.plugins.barcodescanner.ENCODE";
    private static final String TEXT_TYPE = "TEXT_TYPE";
    private static final String EMAIL_TYPE = "EMAIL_TYPE";
    private static final String PHONE_TYPE = "PHONE_TYPE";
    private static final String SMS_TYPE = "SMS_TYPE";
    private static final String CATEGORY_DEFAULT = Intent.CATEGORY_DEFAULT;
    private static final String LOG_TAG = "BarcodeScanner";
    private static final String SCAN_RESULT= "SCAN_RESULT";
    private static final String SCAN_RESULT_FORMAT = "SCAN_RESULT_FORMAT";
    private static final int REQUEST_CODE_ASK_CAMERA_PERMISSIONS = 129 ;

    private String [] permissions = { CAMERA_PERMISSION };


    private CallbackContext callbackContext;

    /**
     * Constructor.
     */
    public BarcodeScanner() {
    }

    /**
     * Executes the request.
     *
     * This method is called from the WebView thread. To do a non-trivial amount of work, use:
     *     cordova.getThreadPool().execute(runnable);
     *
     * To run on the UI thread, use:
     *     cordova.getActivity().runOnUiThread(runnable);
     *
     * @param action          The action to execute.
     * @param args            The exec() arguments.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return                Whether the action was valid.
     *
     * @sa https://github.com/apache/cordova-android/blob/master/framework/src/org/apache/cordova/CordovaPlugin.java
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        this.callbackContext = callbackContext;

        if (action.equals(ENCODE)) {
            JSONObject obj = args.optJSONObject(0);
            if (obj != null) {
                String type = obj.optString(TYPE);
                String data = obj.optString(DATA);

                // If the type is null then force the type to text
                if (type == null) {
                    type = TEXT_TYPE;
                }

                if (data == null) {
                    callbackContext.error("User did not specify data to encode");
                    return true;
                }

                encode(type, data);
            } else {
                callbackContext.error("User did not specify data to encode");
                return true;
            }
        } else if (action.equals(SCAN)) {
            //android permission auto add
            if(checkPermission()) {
                scan();
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * Starts an intent to scan and decode a barcode.
     */
    public void scan() {
        Intent intentScan = new Intent(SCAN_INTENT);
        intentScan.addCategory(CATEGORY_DEFAULT);
        // avoid calling other phonegap apps
        intentScan.setPackage(this.cordova.getActivity().getApplicationContext().getPackageName());
        if(this.cordova.getActivity() instanceof OPGBaseActivity) {
            ((OPGBaseActivity) this.cordova.getActivity()).enableBackButton = false;
        }

        this.cordova.startActivityForResult(this, intentScan, REQUEST_CODE);

    }

    /**
     * Called when the barcode scanner intent completes.
     *
     * @param requestCode The request code originally supplied to startActivityForResult(),
     *                       allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param intent      An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override@DoNotRename
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && intent != null) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put(TEXT, intent.getStringExtra(SCAN_RESULT));
                    obj.put(FORMAT, intent.getStringExtra(SCAN_RESULT_FORMAT));
                    obj.put(CANCELLED, false);
                } catch (JSONException e) {
                    if (BuildConfig.DEBUG) {
                        Log.d(LOG_TAG, "This should never happen");
                    }
                }
                //this.success(new PluginResult(PluginResult.Status.OK, obj), this.callback);
                this.callbackContext.success(obj);
            } else if (resultCode == Activity.RESULT_CANCELED && intent != null) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put(TEXT, "");
                    obj.put(FORMAT, "");
                    obj.put(CANCELLED, true);
                } catch (JSONException e) {
                    if (BuildConfig.DEBUG) {
                        Log.d(LOG_TAG, "This should never happen");
                    }
                    try {
                        obj.put(TEXT, "");
                        obj.put(FORMAT, "");
                        obj.put(CANCELLED, true);
                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                        if (BuildConfig.DEBUG) {
                            Log.d(LOG_TAG, "This should never happen");
                        }
                    }
                }
                //this.success(new PluginResult(PluginResult.Status.OK, obj), this.callback);
                this.callbackContext.success(obj);
            } else {
                //this.error(new PluginResult(PluginResult.Status.ERROR), this.callback);
                this.callbackContext.error("Unexpected error");
            }
        }
    }

    /**
     * Initiates a barcode encode.
     *
     * @param type Endoiding type.
     * @param data The data to encode in the bar code.
     */
    public void encode(String type, String data) {
        Intent intentEncode = new Intent(ENCODE_INTENT);
        intentEncode.putExtra(ENCODE_TYPE, type);
        intentEncode.putExtra(ENCODE_DATA, data);
        // avoid calling other phonegap apps
        intentEncode.setPackage(this.cordova.getActivity().getApplicationContext().getPackageName());

        this.cordova.getActivity().startActivity(intentEncode);
    }

    /**
     * check application's permissions
     */
    private boolean checkPermission() {
        if(Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this.cordova.getActivity(), CAMERA_PERMISSION) == 0) {
                return true;
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(cordova.getActivity(),CAMERA_PERMISSION)) {
                ActivityCompat.requestPermissions(cordova.getActivity(),
                        permissions,
                        REQUEST_CODE_ASK_CAMERA_PERMISSIONS);
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(cordova.getActivity(), CAMERA_PERMISSION)) {
                showAlertDialog();
            }
            return false;
        }else{
            return true;
        }
    }

    /**
     * processes the result of permission request
     *
     * @param requestCode The code to get request action
     * @param permissions The collection of permissions
     * @param grantResults The result of grant
     */
    @DoNotRename
    public void onRequestPermissionResult(int requestCode, String[] permissions,
                                          int[] grantResults) throws JSONException
    {
        PluginResult result;
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                if (BuildConfig.DEBUG) {
                    Log.d(LOG_TAG, "Permission Denied!");
                }
                result = new PluginResult(PluginResult.Status.ILLEGAL_ACCESS_EXCEPTION);
                this.callbackContext.sendPluginResult(result);
                return;
            }
        }

        switch(requestCode)
        {
            case 0:
                scan();
                break;
        }
    }


    private void showAlertDialog() {
        final android.support.v7.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.support.v7.app.AlertDialog.Builder(this.cordova.getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new android.support.v7.app.AlertDialog.Builder(this.cordova.getActivity());
        }
        builder.setTitle(OPGR.getString(this.cordova.getActivity(),STRING,RUNTIME_PERMISSION))
                .setMessage(OPGR.getString(this.cordova.getActivity(),STRING,CAMERA_PERMISSION_MSG))
                .setPositiveButton(OPGR.getString(this.cordova.getActivity(),STRING,SETTINGS), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        goToSettingPage();
                    }
                })
                .setNegativeButton(OPGR.getString(this.cordova.getActivity(),STRING,DENY), new DialogInterface.OnClickListener() {
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

    /**
     * This plugin launches an external Activity when the camera is opened, so we
     * need to implement the save/restore API in case the Activity gets killed
     * by the OS while it's in the background.
     */
    public void onRestoreStateForActivityResult(Bundle state, CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }
}
