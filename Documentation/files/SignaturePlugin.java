package com.opg.my.surveys.plugins;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.allatori.annotations.DoNotRename;
import com.opg.my.surveys.R;
import com.opg.sdk.BuildConfig;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import OnePoint.CordovaPlugin.RootPlugin;
import OnePoint.CordovaPlugin.Utils.FileUtils;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Dinesh-opg on 11/3/2017.
 */

public class SignaturePlugin extends RootPlugin {

    public String READ_EXTRENAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    public String WRITE_EXTRENAL_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public String RUNTIME_PERMISSION= "Runtime Permission";



    private static final int REQUEST_CODE_ASK_STORAGE_PERMISSIONS = 155;
    private CallbackContext callback;
    Context context;
    int REQUEST_CODE = 122;
    private static final String SIGNATURE_INTENT = "com.opg.my.surveys.SIGNATURE";
    private static final String CATEGORY_DEFAULT = Intent.CATEGORY_DEFAULT;
    private String [] permissions = { READ_EXTRENAL_STORAGE_PERMISSION,WRITE_EXTRENAL_STORAGE_PERMISSION };


    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.context = this.cordova.getActivity();
        this.callback = callbackContext;
        try {
            if(cordova.hasPermission(READ_EXTRENAL_STORAGE_PERMISSION) && cordova.hasPermission(WRITE_EXTRENAL_STORAGE_PERMISSION)){
                getSignature();
            }else{
                getStoragePermission(REQUEST_CODE_ASK_STORAGE_PERMISSIONS);
            }
        } catch (Exception var6) {
            if(BuildConfig.DEBUG) {
                var6.printStackTrace();
                Log.e(this.getClass().getName(), var6.getMessage());
            }
            this.callback.error(var6.getLocalizedMessage());
            return false;
        }
        return true;
    }

    protected void getStoragePermission(int requestCode)
    {
        if(showStoragePermissionDialog()) {
            cordova.requestPermissions(this,requestCode,permissions);
        }else {
            showAlertDialog();
        }
    }
    /**
     * Starts an intent to get signature.
     */
    public void getSignature() {
        Intent intentScan = new Intent(SIGNATURE_INTENT);
        intentScan.addCategory(CATEGORY_DEFAULT);
        // avoid calling other phonegap apps
        intentScan.setPackage(this.cordova.getActivity().getApplicationContext().getPackageName());
        this.cordova.startActivityForResult(this, intentScan, REQUEST_CODE);
    }


    private void sendResponse(String path) {
        if(callback!=null){
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, FileUtils.getJSONPathObj(path));
            pluginResult.setKeepCallback(true);
            callback.sendPluginResult(pluginResult);
        } else
            Toast.makeText(this.cordova.getActivity(),"Failed to capture the signature request.Please try again.",Toast.LENGTH_LONG).show();
    }

   /* private JSONObject getErrorJsonObject(String message){
        JSONObject errJsonObj = new JSONObject();
        try{
            errJsonObj.put(MESSAGE,message);
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return errJsonObj;
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            if( data != null && resultCode == RESULT_OK){
                if(data.hasExtra("imagePath")){
                    sendResponse(data.getStringExtra("imagePath"));
                }
            }/*else if( data != null && resultCode == RESULT_CANCELED){
                Toast.makeText(this.cordova.getActivity(),"Cancelled the signature request",Toast.LENGTH_LONG).show();
                if(callback!= null)
                    callback.error(getErrorJsonObject("Cancelled the signature request"));
            }else {
                if(callback!= null)
                    this.callback.error("Unexpected error");
                else
                   Toast.makeText(this.cordova.getActivity(),"Cancelled the signature request",Toast.LENGTH_LONG).show();
            }*/
        }
    }

    private boolean showStoragePermissionDialog(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(cordova.getActivity(), READ_EXTRENAL_STORAGE_PERMISSION)
                && ActivityCompat.shouldShowRequestPermissionRationale(cordova.getActivity(), WRITE_EXTRENAL_STORAGE_PERMISSION)){
            return  true;
        }
        return false;
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
        for(int r:grantResults)
        {
            if(r == PackageManager.PERMISSION_DENIED)
            {
                this.callback.sendPluginResult(new PluginResult(PluginResult.Status.ILLEGAL_ACCESS_EXCEPTION));
                return;
            }
        }

        switch(requestCode)
        {
            case REQUEST_CODE_ASK_STORAGE_PERMISSIONS:
                getSignature();
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
        builder.setTitle(context.getString(R.string.runtime_permission))
                .setMessage(context.getString(R.string.storage_permission_msg))
                .setPositiveButton(context.getString(R.string.app_settings), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        goToSettingPage();
                    }
                })
                .setNegativeButton(context.getString(R.string.deny), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void goToSettingPage(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", cordova.getActivity().getPackageName(), null);
        intent.setData(uri);
        cordova.getActivity().startActivity(intent);
    }
}
