package com.opg.sdk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.allatori.annotations.DoNotRename;

import org.apache.cordova.OPGBaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import OnePoint.CordovaPlugin.MediaPlugin;

import static com.opg.sdk.OPGSDKConstant.ACTIVATE_STORAGE_MSG;
import static com.opg.sdk.OPGSDKConstant.ALLOW;
import static com.opg.sdk.OPGSDKConstant.APP_DETAILS_SETTINGS;
import static com.opg.sdk.OPGSDKConstant.DENY;
import static com.opg.sdk.OPGSDKConstant.PACKAGE;
import static com.opg.sdk.OPGSDKConstant.READ_EXTRENAL_STORAGE_PERMISSION;
import static com.opg.sdk.OPGSDKConstant.RUNTIME_PERMISSION;
import static com.opg.sdk.OPGSDKConstant.SETTINGS;
import static com.opg.sdk.OPGSDKConstant.STORAGE_PERMISSION_MSG;
import static com.opg.sdk.OPGSDKConstant.STRING;
import static com.opg.sdk.OPGSDKConstant.WRITE_EXTRENAL_STORAGE_PERMISSION;


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
 * This activity is the base activity for displaying the survey.
 * User activity need to extend this activity to display the survey.
 */
@SuppressLint({"SetJavaScriptEnabled", "NewApi"})
@DoNotRename
public class OPGActivity extends OPGBaseActivity{

    final private static int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    final private static int REQUEST_CODE_APP_SETTINGS = 123;
    private Context context;
    private String surveyUrl;
    private OPGGetSurveyURL opgSurveyUrl;
    private OPGSurveyInterface opgViewInterface;
    private String surveyReference;
    private long panellistId=0,panelId=0;
    private HashMap<String, String> queryParams = null;
    private List<String> listPermissionsNeeded;
    private String[] permissions= new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.GET_ACCOUNTS
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.init();
        context = this;
        listPermissionsNeeded = new ArrayList<>();
    }

    /***
     * This is method is called once the survey starts loading
     */
    @Override
    protected void onQuestionStartLoading() {
        // TODO Auto-generated method stub
        if(opgViewInterface != null) {
            opgViewInterface.didSurveyStartLoad();
        }
    }

    /***
     * This is method is called once the survey stops loading
     */
    @Override
    protected void onQuestionStopLoading() {
        // TODO Auto-generated method stub
        if(opgViewInterface != null) {
            opgViewInterface.didSurveyFinishLoad();
        }
    }

    /***
     * This is method is called once the survey is completed
     */
    @Override
    protected void onSurveyCompleted() {
        // TODO Auto-generated method stub
        if(opgViewInterface != null) {
            opgViewInterface.didSurveyFinishLoad();
            opgViewInterface.didSurveyCompleted();
        }
    }

    /**
     * This method loads the URL to run the survey.
     *
     * @param opgViewInterface
     * @param surveyReference  This parameter is used to construct the URL which is used to load the survey.
     */
    @DoNotRename
    public void loadOnlineSurvey(OPGSurveyInterface opgViewInterface, String surveyReference) {
        MediaPlugin.isOfflineMedia = false;
        this.opgViewInterface = opgViewInterface;
        this.surveyReference = surveyReference;
        loadSurveyLink();
    }

    /**
     * This method loads the URL to run the survey.
     *
     * @param opgViewInterface
     * @param surveyReference
     * @param queryParams
     */
    @DoNotRename
    public void loadOnlineSurvey(OPGSurveyInterface opgViewInterface, String surveyReference, HashMap<String, String> queryParams) {
        MediaPlugin.isOfflineMedia = false;
        this.opgViewInterface = opgViewInterface;
        this.surveyReference = surveyReference;
        this.queryParams = queryParams;
        loadSurveyLink();
    }

    /**
     * This method loads the URL to run the survey.
     * @param opgViewInterface
     * @param surveyReference
     * @param panelID
     * @param panellistID
     */
    @DoNotRename
    public void loadOnlineSurvey(OPGSurveyInterface opgViewInterface, String surveyReference, long panelID, long panellistID) {
        MediaPlugin.isOfflineMedia = false;
        this.opgViewInterface = opgViewInterface;
        this.surveyReference = surveyReference;
        this.panelId = panelID;
        this.panellistId = panellistID;
        loadSurveyLink();
    }
    /**
     * This method loads the URL to run the survey .
     * @param opgViewInterface
     * @param surveyReference
     * @param panelID
     * @param panellistID
     * @param queryParams
     */
    @DoNotRename
    public void loadOnlineSurvey(OPGSurveyInterface opgViewInterface, String surveyReference, long panelID, long panellistID, HashMap<String, String> queryParams) {
        MediaPlugin.isOfflineMedia = false;
        this.opgViewInterface = opgViewInterface;
        this.surveyReference = surveyReference;
        this.queryParams = queryParams;
        this.panelId = panelID;
        this.panellistId = panellistID;
        loadSurveyLink();
    }


    public boolean checkPermission() {
        if(Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(OPGActivity.this, READ_EXTRENAL_STORAGE_PERMISSION) +
                        ContextCompat.checkSelfPermission(OPGActivity.this, WRITE_EXTRENAL_STORAGE_PERMISSION)
                        == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this,permissions,REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                //showAlertDialog();
            }
            return false;
        } else {
            return true;
        }
    }

    private void showAlertDialog() {
        String postiveButtonTitle = OPGR.getString(OPGActivity.this,STRING,ALLOW);

        if (!showPermissionDialog()){
            postiveButtonTitle = OPGR.getString(OPGActivity.this,STRING,SETTINGS);
        }
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(OPGActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(OPGActivity.this);
        }
        builder.setTitle(OPGR.getString(OPGActivity.this,STRING,RUNTIME_PERMISSION))
                .setMessage(OPGR.getString(OPGActivity.this,STRING,STORAGE_PERMISSION_MSG))
                .setPositiveButton(postiveButtonTitle, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        validatePermissions();
                    }
                })
                .setNegativeButton(OPGR.getString(OPGActivity.this,STRING,DENY), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private boolean showPermissionDialog(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(OPGActivity.this, READ_EXTRENAL_STORAGE_PERMISSION)
                    && ActivityCompat.shouldShowRequestPermissionRationale(OPGActivity.this, WRITE_EXTRENAL_STORAGE_PERMISSION)) {
            return true;
        }else {
            return false;
        }
    }

    private void validatePermissions() {
        if (showPermissionDialog()) {
            ActivityCompat.requestPermissions(OPGActivity.this,
                    permissions,
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else if (!showPermissionDialog()) {
            goToSettingPage();
            Toast.makeText(context,OPGR.getString(OPGActivity.this,STRING,ACTIVATE_STORAGE_MSG),Toast.LENGTH_SHORT).show();
        }
    }

    private void goToSettingPage(){
        Intent intent = new Intent();
        intent.setAction(APP_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts(PACKAGE, getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent,REQUEST_CODE_APP_SETTINGS);
    }

    /*@Override@DoNotRename
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                if(ContextCompat.checkSelfPermission(context,READ_EXTRENAL_STORAGE_PERMISSION) + ContextCompat.checkSelfPermission(context,WRITE_EXTRENAL_STORAGE_PERMISSION) == PackageManager.PERMISSION_GRANTED)
                {}
                   // startRuntime();
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }*/

    private void loadSurveyLink(){
            opgSurveyUrl = new OPGGetSurveyURL();
            surveyUrl = opgSurveyUrl.takeSurveyUrl(context, surveyReference, panelId, panellistId, queryParams);
            loadUrl(surveyUrl);
        if(!OPGPreference.isSurveyLoadingFirstTime(context))
        {
            OPGPreference.setIsSurveyLoadingFirstTime(context,true);
            checkPermission();
        }
    }
}
