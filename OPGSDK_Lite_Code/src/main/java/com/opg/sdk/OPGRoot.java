/**
 *
 */
package com.opg.sdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.opg.sdk.exceptions.InvalidMediaTypeException;
import com.opg.sdk.exceptions.OPGException;
import com.opg.sdk.geofence.OPGGeofenceTriggerEvents;
import com.opg.sdk.models.OPGAuthenticate;
import com.opg.sdk.models.OPGChangePassword;
import com.opg.sdk.models.OPGCountry;
import com.opg.sdk.models.OPGDownloadMedia;
import com.opg.sdk.models.OPGForgotPassword;
import com.opg.sdk.models.OPGGeofenceSurvey;
import com.opg.sdk.models.OPGPanellistPanel;
import com.opg.sdk.models.OPGPanellistProfile;
import com.opg.sdk.models.OPGSurvey;
import com.opg.sdk.models.OPGTheme;
import com.opg.sdk.models.OPGUpdatePanellistProfile;
import com.opg.sdk.restclient.OPGHttpUrlRequest;
import com.opg.sdk.restclient.OPGNetworkRequest;
import com.opg.sdk.restclient.OPGParseResult;
import com.opg.sdk.restclient.OPGRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import OnePoint.Common.Utils;

import static com.opg.sdk.OPGSDKConstant.DOT;
import static com.opg.sdk.OPGSDKConstant.DOWNLOAD_SUCCESSFUL;
import static com.opg.sdk.OPGSDKConstant.EMPTY_STRING;
import static com.opg.sdk.OPGSDKConstant.ERROR_FAILED_DOWNLOAD;
import static com.opg.sdk.OPGSDKConstant.ERROR_NULL_ADMIN_NAME;
import static com.opg.sdk.OPGSDKConstant.ERROR_NULL_APP_VERSION;
import static com.opg.sdk.OPGSDKConstant.ERROR_NULL_DEVICE_TOKEN;
import static com.opg.sdk.OPGSDKConstant.ERROR_NULL_MEDIA_TYPE;
import static com.opg.sdk.OPGSDKConstant.ERROR_NULL_SHARED_KEY;
import static com.opg.sdk.OPGSDKConstant.ERROR_NULL_UNIQUE_ID;
import static com.opg.sdk.OPGSDKConstant.NEW_LINE;
import static com.opg.sdk.OPGSDKConstant.OPG_MEDIA;
import static com.opg.sdk.OPGSDKConstant.SESSION_TIME_OUT_ERROR;
/**
 * @author Neeraj
 *
 */
class OPGRoot
{
    public OPGRoot() {

    }

    /**
     * @return
     */
    public static OPGRoot getInstance() {
        return new OPGRoot();
    }

    /**
     * Checks the given email is valid or not
     *
     * @param email
     * @return
     */
    protected static boolean validateEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public List<OPGGeofenceSurvey> getOpgGeofenceSurveyList(Context context) {
        return OPGPreference.getOPGGeofenceSurveys(context);
    }

    public void setOpgGeofenceSurveyList(Context context, List<OPGGeofenceSurvey> opgGeofenceSurveyList) {
        OPGPreference.clearOPGGeofenceSurveys(context);
        OPGPreference.saveOPGGeofenceSurveys(context, opgGeofenceSurveyList);
    }

    /**
     * It is mandatory to initialize the SDK at app launch. The initialize API
     * must be invoked on the UI Thread, preferably in the onCreate method of
     * the launcher activity.
     *
     * @param username
     *            The input value is the admin username.
     * @param sharedKey
     *            The input value is the admin sdk key.
     * @param context
     *            The context to use. Usually your android.app.Application or
     *            android.app.Activity object.
     * @return
     * @throws OPGException
     */


    protected void initialize(String username, String sharedKey, Context context) throws OPGException
    {
        /*live urls*/
//        String apiURL = "https://api.1pt.mobi/V3.1/Api/";
//        String interviewURL = "https://api.1pt.mobi/i/interview";
//        String mediaURL = "https://api.1pt.mobi/i/Media?";
        String apiURL = "http://apidev.1pt.mobi/V3.0/api/";
        String interviewURL = "http://apidev.1pt.mobi/i/interview";
        String mediaURL = "http://apidev.1pt.mobi/i/Media?";

        if (!validateString(username) || !validateString(sharedKey)) {
            System.err.println(OPGSDKConstant.FAILED_INITIALIZE);
            throw new OPGException(OPGSDKConstant.ERROR_MESSAGE_OPGROOT);
        }
        if (OPGPreference.getApiURL(context) == null || OPGPreference.getApiURL(context).trim().isEmpty()) {
            OPGPreference.setApiURL(apiURL, context);
        }
        if (OPGPreference.getInterviewURL(context) == null || OPGPreference.getInterviewURL(context).trim().isEmpty()) {
            OPGPreference.setInterviewURL(interviewURL, context);
        }
        if (OPGPreference.getDownloadURL(context) == null || OPGPreference.getDownloadURL(context).trim().isEmpty()) {
            OPGPreference.setDownloadURL(mediaURL, context);
        }
        OPGPreference.setUsername(username, context);
        OPGPreference.setSharedKey(sharedKey, context);
        System.out.println(OPGSDKConstant.SUCCESS_INITIALIZE);

    }

    /**
     * This method will set the App Version
     *
     * @param appVersion
     * @param context
     * @throws OPGException
     */
    protected void setAppVersion(String appVersion, Context context) throws OPGException {
        if (!validateString(appVersion)) {
            //System.err.println("Null or empty credential values for appversion");
            throw new OPGException(ERROR_NULL_APP_VERSION);
        }
        OPGPreference.setAppVersion(appVersion, context);
    }

    /**
     * To get the App Version
     *
     * @param context
     * @return
     */
    protected String getAppVersion(Context context) {
        return OPGPreference.getAppVersion(context);
    }

    /**
     * To get the UniqueID
     *
     * @param context
     * @return UniqueID
     */
    protected String getUniqueID(Context context) {
        return OPGPreference.getUniqueID(context);
    }

    /**
     * setUniqueID
     *
     * @param uniqueID
     * @param context
     * @throws OPGException
     */
    protected void setUniqueID(String uniqueID, Context context) throws OPGException {
        if (!validateString(uniqueID)) {
            //System.err.println("Null or empty credential values for uniqueid");
            throw new OPGException(ERROR_NULL_UNIQUE_ID);
        }
        OPGPreference.setUniqueID(uniqueID, context);
    }


    private boolean isValidSession(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has(OPGSDKConstant.HTTP_STATUS_CODE) && jsonObject.getInt(OPGSDKConstant.HTTP_STATUS_CODE) == 401) {
                return false;
            }
        } catch (JSONException e) {

        }
        return true;

    }

    /**
     * This method is to get list of surveys .
     *
     * @param context
     *            The context to use. Usually your android.app.Application or
     *            android.app.Activity object.
     * @return ArrayList of OnePointSurvey objects
     */
    protected ArrayList<OPGSurvey> getUserSurveyList(Context context) throws OPGException {
        ArrayList<OPGSurvey> surveyList;
        try {
            String uniqueID = OPGPreference.getUniqueID(context);
            String sharedkey_auth = OPGPreference.getSharedKey(context);
            String username_auth = OPGPreference.getUsername(context);
            if (validateString(uniqueID) && validateString(sharedkey_auth) && validateString(username_auth)) {

                OPGHttpUrlRequest httpUrlRequest = OPGNetworkRequest.createRequestParams(context, null, OPGSDKConstant.surveyAPIRoute, uniqueID);
                String surveyListResponse = OPGNetworkRequest.performRequest(httpUrlRequest, OPGSDKConstant.GET);
                if (!isValidSession(surveyListResponse)) {
                    if (refreshSession(context)) {
                        uniqueID = OPGPreference.getUniqueID(context);
                        httpUrlRequest = OPGNetworkRequest.createRequestParams(context, null, OPGSDKConstant.surveyAPIRoute, uniqueID);
                        surveyListResponse = OPGNetworkRequest.performRequest(httpUrlRequest, OPGSDKConstant.GET);
                    } else {
                        throw new OPGException(SESSION_TIME_OUT_ERROR);
                    }
                }
                surveyList = (ArrayList<OPGSurvey>) OPGParseResult.parseSurveyList(surveyListResponse);
            } else {
                StringBuilder builder = new StringBuilder();
                if (!validateString(uniqueID)) {
                    builder.append(ERROR_NULL_UNIQUE_ID).append(NEW_LINE);
                }
                if (!validateString(username_auth)) {
                    builder.append(ERROR_NULL_ADMIN_NAME).append(NEW_LINE);
                }
                if (!validateString(sharedkey_auth)) {
                    builder.append(ERROR_NULL_SHARED_KEY).append(NEW_LINE);
                }
                throw new OPGException(builder.toString());
            }
        } catch (Exception exception) {
            //Log.i(OPGRoot.class.getName(),exception.getMessage());
            throw new OPGException(exception.getMessage());
        }
        return surveyList;
    }

    /**
     * This method is to get list of surveys based on admin username and sharedkey.
     * @param context
     * @return
     * @throws OPGException
     */
    protected ArrayList<OPGSurvey> getSurveyList(Context context) throws OPGException {
        ArrayList<OPGSurvey> surveyList;
        try {

            String uniqueID = OPGPreference.getUniqueID(context);
            String sharedkey_auth = OPGPreference.getSharedKey(context);
            String username_auth = OPGPreference.getUsername(context);
            if (validateString(sharedkey_auth) && validateString(username_auth)) {

                OPGHttpUrlRequest httpUrlRequest = OPGNetworkRequest.createRequestParams(context, null, OPGSDKConstant.surveyAPIRoute, uniqueID);
                String surveyListResponse = OPGNetworkRequest.performRequest(httpUrlRequest, OPGSDKConstant.GET);
                if (!isValidSession(surveyListResponse)) {
                    if (refreshSession(context)) {
                        uniqueID = OPGPreference.getUniqueID(context);
                        httpUrlRequest = OPGNetworkRequest.createRequestParams(context, null, OPGSDKConstant.surveyAPIRoute, uniqueID);
                        surveyListResponse = OPGNetworkRequest.performRequest(httpUrlRequest, OPGSDKConstant.GET);
                    } else {
                        throw new OPGException(SESSION_TIME_OUT_ERROR);
                    }
                }
                surveyList = (ArrayList<OPGSurvey>) OPGParseResult.parseSurveyList(surveyListResponse);
            } else {
                StringBuilder builder = new StringBuilder();
                if (!validateString(username_auth)) {
                    builder.append(ERROR_NULL_ADMIN_NAME).append(NEW_LINE);
                }
                if (!validateString(sharedkey_auth)) {
                    builder.append(ERROR_NULL_SHARED_KEY).append(NEW_LINE);
                }
                throw new OPGException(builder.toString());
            }
        } catch (Exception exception) {
            //Log.i(OPGRoot.class.getName(),exception.getMessage());
            throw new OPGException(exception.getMessage());
        }
        return surveyList;
    }

    /**
     * This method returns the list of all the surveys for a particular panel ID.
     * @param context The Context
     * @param panelID The PanelID
     * @return The list of OPGSurvey Object
     * @throws OPGException
     */

    protected List<OPGSurvey> getSurveys(Context context, String panelID) throws OPGException {
        List<OPGSurvey> surveyList = null;
        try {

            String uniqueID = OPGPreference.getUniqueID(context);
            String sharedkey_auth = OPGPreference.getSharedKey(context);
            String username_auth = OPGPreference.getUsername(context);
            if (validateString(panelID) && validateString(uniqueID) && validateString(sharedkey_auth) && validateString(username_auth)) {
                JSONObject surveyEntity = OPGRequest.getSurveyListEntityForPanelID(uniqueID, panelID);
                OPGHttpUrlRequest httpUrlRequest = OPGNetworkRequest.createRequestParams(context, surveyEntity, OPGSDKConstant.surveyAPIRoute, uniqueID);
                String surveyListResponse = OPGNetworkRequest.performRequest(httpUrlRequest);
                if (!isValidSession(surveyListResponse)) {
                    if (refreshSession(context)) {
                        uniqueID = OPGPreference.getUniqueID(context);
                        httpUrlRequest = OPGNetworkRequest.createRequestParams(context, OPGRequest.getSurveyListEntityForPanelID(OPGPreference.getUniqueID(context), panelID), OPGSDKConstant.surveyAPIRoute, uniqueID);
                        surveyListResponse = OPGNetworkRequest.performRequest(httpUrlRequest);
                    } else {
                        throw new OPGException(SESSION_TIME_OUT_ERROR);
                    }
                }
                surveyList = OPGParseResult.parseSurveyList(surveyListResponse);
            } else {
                StringBuilder builder = new StringBuilder();
                if (!validateString(panelID)) {
                    builder.append(OPGSDKConstant.ERROR_NULL_PANEL_ID).append(NEW_LINE);
                }
                if (!validateString(uniqueID)) {
                    builder.append(ERROR_NULL_UNIQUE_ID).append(NEW_LINE);
                }
                if (!validateString(username_auth)) {
                    builder.append(ERROR_NULL_ADMIN_NAME).append(NEW_LINE);
                }
                if (!validateString(sharedkey_auth)) {
                    builder.append(ERROR_NULL_SHARED_KEY).append(NEW_LINE);
                }
                throw new OPGException(builder.toString());
            }
        } catch (Exception exception) {
            //Log.i(OPGRoot.class.getName(),exception.getMessage());
            throw new OPGException(exception.getMessage());
        }
        return surveyList;
    }


    /**
     *  This method returns the list of all the opggeofencesurveys for a particular latitude and longitude.
     * @param context
     * @param latitude
     * @param longitude
     * @return
     * @throws OPGException
     */
    protected List<OPGGeofenceSurvey> getGeofenceSurveys(Context context, float latitude, float longitude) throws OPGException {
        List<OPGGeofenceSurvey> geofenceSurveys = null;
        try {

            String uniqueID = OPGPreference.getUniqueID(context);
            String sharedkey_auth = OPGPreference.getSharedKey(context);
            String username_auth = OPGPreference.getUsername(context);
            if (validateString(String.valueOf(latitude)) && validateString(String.valueOf(longitude)) && validateString(uniqueID) && validateString(sharedkey_auth) && validateString(username_auth)) {
//                JSONObject geofenceEntity = OPGRequest.getGeofenceSurveyListEntity(uniqueID,latitude,longitude);
                Map<String, Object> queryString = new HashMap<>();
                queryString.put(OPGSDKConstant.LATITUDE, latitude);
                queryString.put(OPGSDKConstant.LONGITUDE, longitude);
                String url = OPGRequest.getQueryStringUrl(OPGSDKConstant.geofencingAPIRoute, queryString);

                OPGHttpUrlRequest httpUrlRequest = OPGNetworkRequest.createRequestParams(context, null, url, uniqueID);
                String geofenceSurveyListResponse = OPGNetworkRequest.performRequest(httpUrlRequest, OPGSDKConstant.GET);
                if (!isValidSession(geofenceSurveyListResponse)) {
                    if (refreshSession(context)) {
                        uniqueID = OPGPreference.getUniqueID(context);
                        httpUrlRequest = OPGNetworkRequest.createRequestParams(context, null, url, uniqueID);
                        geofenceSurveyListResponse = OPGNetworkRequest.performRequest(httpUrlRequest, OPGSDKConstant.GET);
                    } else {
                        throw new OPGException(SESSION_TIME_OUT_ERROR);
                    }
                }
                geofenceSurveys = OPGParseResult.parseGeofenceSurveyList(geofenceSurveyListResponse);
            } else {
                StringBuilder builder = new StringBuilder();
                if (!validateString(String.valueOf(latitude))) {
                    builder.append(OPGSDKConstant.ERROR_NULL_LATITUDE).append(NEW_LINE);
                }
                if (!validateString(String.valueOf(longitude))) {
                    builder.append(OPGSDKConstant.ERROR_NULL_LONGITUDE).append(NEW_LINE);
                }
                if (!validateString(uniqueID)) {
                    builder.append(ERROR_NULL_UNIQUE_ID).append(NEW_LINE);
                }
                if (!validateString(username_auth)) {
                    builder.append(ERROR_NULL_ADMIN_NAME).append(NEW_LINE);
                }
                if (!validateString(sharedkey_auth)) {
                    builder.append(ERROR_NULL_SHARED_KEY).append(NEW_LINE);
                }
                throw new OPGException(builder.toString());
            }
        } catch (Exception exception) {
            //Log.i(OPGRoot.class.getName(),exception.getMessage());
            throw new OPGException(exception.getMessage());
        }
        return geofenceSurveys;
    }


    /**
     *
     * @param userName
     * @param password
     * @return
     */
    protected OPGAuthenticate authenticate(String userName, String password, Context mContext) {
        OPGAuthenticate opgAuthenticate = null;
        String appVersion = OPGPreference.getAppVersion(mContext);
        String username_auth = OPGPreference.getUsername(mContext);
        String sharedkey_auth = OPGPreference.getSharedKey(mContext);
        if (validateString(username_auth) && validateString(sharedkey_auth) && validateString(userName) && validateString(password) && validateString(appVersion)) {
            try {
                OPGPreference.setAppLoginUsername(mContext, userName);
                OPGPreference.setAppLoginPassword(mContext, password);
                JSONObject authEntity = OPGRequest.getAuthenticateEntity(userName, password, OPGPreference.getAppVersion(mContext), Utils.convertToUTCFromDate(new Date()));
                OPGHttpUrlRequest opgReqObj = OPGNetworkRequest.createRequestParams(mContext, authEntity, OPGSDKConstant.authenticateAPIRoute, "");
                String authResponse = OPGNetworkRequest.performRequest(opgReqObj);
                opgAuthenticate = OPGParseResult.parseAuthenticate(mContext, authResponse);
            } catch (Exception e) {
                opgAuthenticate = new OPGAuthenticate();
                opgAuthenticate.setStatusMessage(e.toString());
                opgAuthenticate.setSuccess(false);
            }
        } else {
            opgAuthenticate = new OPGAuthenticate();
            opgAuthenticate.setSuccess(false);

            StringBuilder builder = new StringBuilder();
            if (!validateString(username_auth)) {
                builder.append(ERROR_NULL_ADMIN_NAME).append(NEW_LINE);
            }

            if (!validateString(sharedkey_auth)) {
                builder.append(ERROR_NULL_SHARED_KEY).append(NEW_LINE);
            }

            if (!validateString(userName)) {
                builder.append(OPGSDKConstant.ERROR_NULL_LOGIN_USERNAME).append(NEW_LINE);
            }

            if (!validateString(password)) {
                builder.append(OPGSDKConstant.ERROR_NULL_LOGIN_PASSWORD).append(NEW_LINE);
            }
            if (!validateString(appVersion)) {
                builder.append(ERROR_NULL_APP_VERSION).append(NEW_LINE);
            }
            opgAuthenticate.setStatusMessage(builder.toString());
        }
        return opgAuthenticate;
    }

    /**
     *
     * @param googleTokenID
     * @param mContext
     * @return
     */
    protected OPGAuthenticate authenticateWithGoogle(String googleTokenID, Context mContext) {
        OPGAuthenticate opgAuthenticate = null;
        String appVersion = OPGPreference.getAppVersion(mContext);
        String username_auth = OPGPreference.getUsername(mContext);
        String sharedkey_auth = OPGPreference.getSharedKey(mContext);
        if (validateString(username_auth) && validateString(sharedkey_auth) && validateString(googleTokenID) && validateString(appVersion)) {
            try {
                OPGPreference.setGoogleToken(mContext, googleTokenID);
                JSONObject authEntity = OPGRequest.getGoogleAuthEntity(googleTokenID, appVersion);
                OPGHttpUrlRequest opgReqObj = OPGNetworkRequest.createRequestParams(mContext, authEntity, OPGSDKConstant.googleAuthenticateAPIRoute, "");
                String authResponse = OPGNetworkRequest.performRequest(opgReqObj);
                opgAuthenticate = OPGParseResult.parseAuthenticate(mContext, authResponse);
            } catch (Exception e) {
                opgAuthenticate = new OPGAuthenticate();
                opgAuthenticate.setStatusMessage(e.toString());
                opgAuthenticate.setSuccess(false);
            }
        } else {
            opgAuthenticate = new OPGAuthenticate();
            opgAuthenticate.setSuccess(false);

            StringBuilder builder = new StringBuilder();
            if (!validateString(username_auth)) {
                builder.append(ERROR_NULL_ADMIN_NAME).append(NEW_LINE);
            }

            if (!validateString(sharedkey_auth)) {
                builder.append(ERROR_NULL_SHARED_KEY).append(NEW_LINE);
            }

            if (!validateString(googleTokenID)) {
                builder.append(OPGSDKConstant.ERROR_NULL_GOOGLE_TOKENID).append(NEW_LINE);
            }


            if (!validateString(appVersion)) {
                builder.append(ERROR_NULL_APP_VERSION).append(NEW_LINE);
            }
            opgAuthenticate.setStatusMessage(builder.toString());
        }
        return opgAuthenticate;
    }

    /**
     *
     * @param facebookTokenID
     * @param mContext
     * @return
     */
    protected OPGAuthenticate authenticateWithFacebook(String facebookTokenID, Context mContext) {
        OPGAuthenticate opgAuthenticate = null;
        String appVersion = OPGPreference.getAppVersion(mContext);
        String username_auth = OPGPreference.getUsername(mContext);
        String sharedkey_auth = OPGPreference.getSharedKey(mContext);
        if (validateString(username_auth) && validateString(sharedkey_auth) && validateString(facebookTokenID) && validateString(appVersion)) {
            try {
                OPGPreference.setFacebookToken(mContext, facebookTokenID);
                JSONObject authEntity = OPGRequest.getFacebookAuthEntity(facebookTokenID, appVersion);
                OPGHttpUrlRequest opgReqObj = OPGNetworkRequest.createRequestParams(mContext, authEntity, OPGSDKConstant.facebookAuthenticateAPIRoute, "");
                String authResponse = OPGNetworkRequest.performRequest(opgReqObj);
                opgAuthenticate = OPGParseResult.parseAuthenticate(mContext, authResponse);
            } catch (Exception e) {
                opgAuthenticate = new OPGAuthenticate();
                opgAuthenticate.setStatusMessage(e.toString());
                opgAuthenticate.setSuccess(false);
            }
        } else {
            opgAuthenticate = new OPGAuthenticate();
            opgAuthenticate.setSuccess(false);

            StringBuilder builder = new StringBuilder();
            if (!validateString(username_auth)) {
                builder.append(ERROR_NULL_ADMIN_NAME).append(NEW_LINE);
            }

            if (!validateString(sharedkey_auth)) {
                builder.append(ERROR_NULL_SHARED_KEY).append(NEW_LINE);
            }

            if (!validateString(facebookTokenID)) {
                builder.append(OPGSDKConstant.ERROR_NULL_FACEBOOK_TOKENID).append(NEW_LINE);
            }


            if (!validateString(appVersion)) {
                builder.append(ERROR_NULL_APP_VERSION).append(NEW_LINE);
            }
            opgAuthenticate.setStatusMessage(builder.toString());
        }
        return opgAuthenticate;
    }


    /**
     * To Change the panellist password
     *
     * @param context
     * @param currentPassword
     * @param newPassword
     * @return
     * @throws Exception
     */
    protected OPGChangePassword changePassword(Context context, String currentPassword, String newPassword) {
        OPGChangePassword opgChangePassword = new OPGChangePassword();
        String uniqueID = OPGPreference.getUniqueID(context);
        if (validateString(uniqueID) && validateString(currentPassword) && validateString(newPassword) && !currentPassword.equals(newPassword)) {
            try {
                JSONObject changePwdEntity = OPGRequest.getChangePasswordEntity(uniqueID, currentPassword, newPassword);
                OPGHttpUrlRequest opgReqObj = OPGNetworkRequest.createRequestParams(context, changePwdEntity, OPGSDKConstant.changePasswordAPIRoute, uniqueID);
                String changePwdResponse = OPGNetworkRequest.performRequest(opgReqObj);
                if (!isValidSession(changePwdResponse)) {
                    if (refreshSession(context)) {
                        uniqueID = OPGPreference.getUniqueID(context);
                        opgReqObj = OPGNetworkRequest.createRequestParams(context, OPGRequest.getChangePasswordEntity(OPGPreference.getUniqueID(context), currentPassword, newPassword), OPGSDKConstant.changePasswordAPIRoute, uniqueID);
                        changePwdResponse = OPGNetworkRequest.performRequest(opgReqObj);
                    } else {
                        opgChangePassword.setSuccess(false);
                        opgChangePassword.setStatusMessage(SESSION_TIME_OUT_ERROR);
                        return opgChangePassword;
                    }
                }
                opgChangePassword = OPGParseResult.parseChangePassword(changePwdResponse);
                if (opgChangePassword.isSuccess()) {
                    OPGPreference.setAppLoginPassword(context, newPassword);
                }
            } catch (Exception e) {
                opgChangePassword.setSuccess(false);
                opgChangePassword.setStatusMessage(e.toString());
            }
        } else {
            opgChangePassword = new OPGChangePassword();
            opgChangePassword.setSuccess(false);
            StringBuilder builder = new StringBuilder();
            if (!validateString(uniqueID)) {
                builder.append(ERROR_NULL_UNIQUE_ID).append(NEW_LINE);
            }

            if (!validateString(currentPassword)) {
                builder.append(OPGSDKConstant.ERROR_NULL_CURRENT_PASSWORD).append(NEW_LINE);
            }

            if (!validateString(newPassword)) {
                builder.append(OPGSDKConstant.ERROR_NULL_NEW_PASSWORD).append(NEW_LINE);
            } else if (currentPassword.equals(newPassword)) {
                builder.append(OPGSDKConstant.ERROR_PASSWORD_SHOULD_SAME).append(NEW_LINE);
            }
            opgChangePassword.setStatusMessage(builder.toString());
        }
        return opgChangePassword;
    }


    /**
     *
     * @param emailID
     * @return
     * @throws Exception
     */
    protected OPGForgotPassword forgotPassword(String emailID, Context context) {
        OPGForgotPassword forgotPassword = new OPGForgotPassword();
        String appVersion = OPGPreference.getAppVersion(context);
        String username_auth = OPGPreference.getUsername(context);
        String sharedkey_auth = OPGPreference.getSharedKey(context);
        if (validateString(username_auth) && validateString(sharedkey_auth) && validateString(appVersion) && validateString(emailID) && validateEmail(emailID)) {
            try {
                JSONObject forPasswordEntity = OPGRequest.getForgotPasswordEntity(emailID, appVersion);
                OPGHttpUrlRequest opgReqObj = OPGNetworkRequest.createRequestParams(context, forPasswordEntity, OPGSDKConstant.forgotPasswordAPIRoute, EMPTY_STRING);
                String forPasswordResponse = OPGNetworkRequest.performRequest(opgReqObj);
                forgotPassword = OPGParseResult.parseForgotPassword(context, forPasswordResponse);
            } catch (Exception e) {
                forgotPassword.setStatusMessage(e.toString());
                forgotPassword.setSuccess(false);
            }
        } else {
            forgotPassword.setSuccess(false);
            StringBuilder builder = new StringBuilder();
            if (!validateString(username_auth)) {
                builder.append(ERROR_NULL_ADMIN_NAME).append(NEW_LINE);
            }

            if (!validateString(sharedkey_auth)) {
                builder.append(ERROR_NULL_SHARED_KEY).append(NEW_LINE);
            }

            if (!validateString(appVersion)) {
                builder.append(ERROR_NULL_APP_VERSION).append(NEW_LINE);
            }

            if (!validateString(emailID)) {
                builder.append(OPGSDKConstant.ERROR_NULL_EMAILID).append(NEW_LINE);
            } else if (!validateEmail(emailID)) {
                builder.append(OPGSDKConstant.ERROR_INVALIDE_MAIL_ID).append(NEW_LINE);
            }
            forgotPassword.setStatusMessage(builder.toString());
        }
        return forgotPassword;
    }

    private boolean refreshSession(Context context) throws OPGException {
        OPGAuthenticate authenticate = new OPGAuthenticate();
        switch (OPGPreference.getLoginType(context)) {
            //OPGSDk Login
            case 0:
                authenticate = authenticate(OPGPreference.getAppLoginUsername(context), OPGPreference.getAppLoginPassword(context), context);
                break;
            //Google Login
            case 1:
                authenticate = authenticateWithGoogle(OPGPreference.getGoogleToken(context), context);
                break;
            //Facebook Login
            case 2:
                authenticate = authenticateWithFacebook(OPGPreference.getFacebookToken(context), context);
                break;
        }
        setUniqueID(authenticate.getUniqueID(), context);
        return authenticate.isSuccess();
    }

    /**
     * To get the panelist profile
     *
     * @param context
     * @return OPGPanellistProfile
     */
    protected OPGPanellistProfile getPanellistProfile(Context context) {
        OPGPanellistProfile opgPanelistProfile = new OPGPanellistProfile();
        try {
            String uniqueID = OPGPreference.getUniqueID(context);
            String sharedkey_auth = OPGPreference.getSharedKey(context);
            String username_auth = OPGPreference.getUsername(context);
            if (validateString(uniqueID) && validateString(sharedkey_auth) && validateString(username_auth)) {
                OPGHttpUrlRequest opgHttpUrlRequest = OPGNetworkRequest.createRequestParams(context, null, OPGSDKConstant.panellistProfileAPIRoute, uniqueID);
                String panelistProfileResponse = OPGNetworkRequest.performRequest(opgHttpUrlRequest, OPGSDKConstant.GET);
                if (!isValidSession(panelistProfileResponse)) {
                    if (refreshSession(context)) {
                        uniqueID = OPGPreference.getUniqueID(context);
                        opgHttpUrlRequest = OPGNetworkRequest.createRequestParams(context, null, OPGSDKConstant.panellistProfileAPIRoute, uniqueID);
                        panelistProfileResponse = OPGNetworkRequest.performRequest(opgHttpUrlRequest, OPGSDKConstant.GET);
                    } else {
                        opgPanelistProfile.setSuccess(false);
                        opgPanelistProfile.setStatusMessage(SESSION_TIME_OUT_ERROR);
                        return opgPanelistProfile;
                    }
                }
                opgPanelistProfile = OPGParseResult.parsePanellistProfile(context, panelistProfileResponse);
            } else {
                StringBuilder builder = new StringBuilder();
                opgPanelistProfile.setSuccess(false);
                if (!validateString(uniqueID)) {
                    builder.append(ERROR_NULL_UNIQUE_ID).append(NEW_LINE);
                }
                if (!validateString(username_auth)) {
                    builder.append(ERROR_NULL_ADMIN_NAME).append(NEW_LINE);
                }
                if (!validateString(sharedkey_auth)) {
                    builder.append(ERROR_NULL_SHARED_KEY).append(NEW_LINE);
                }
                opgPanelistProfile.setStatusMessage(builder.toString());
            }
        } catch (Exception exception) {
            opgPanelistProfile.setSuccess(false);
            opgPanelistProfile.setStatusMessage(exception.getMessage());
            //Log.i(OPGRoot.class.getName(),exception.getMessage());
        }
        return opgPanelistProfile;
    }

    /**
     *
     * @param context
     * @param panellistProfile
     * @return
     */
    protected OPGUpdatePanellistProfile updatePanellistProfile(Context context, OPGPanellistProfile panellistProfile) {
        OPGUpdatePanellistProfile opgUpdatePanellistProfile = new OPGUpdatePanellistProfile();
        try {
            String uniqueID = OPGPreference.getUniqueID(context);
            String sharedkey_auth = OPGPreference.getSharedKey(context);
            String username_auth = OPGPreference.getUsername(context);
            if (validateString(uniqueID) && validateString(sharedkey_auth) && validateString(username_auth)) {
                JSONObject jsonObject = OPGRequest.getUpdatePanellistProfileEntity(uniqueID, panellistProfile);
                OPGHttpUrlRequest httpUrlRequest = OPGNetworkRequest.createRequestParams(context, jsonObject, OPGSDKConstant.updatePanellistProfileAPIRoute, uniqueID);
                String response = OPGNetworkRequest.performRequest(httpUrlRequest);
                if (!isValidSession(response)) {
                    if (refreshSession(context)) {
                        uniqueID = OPGPreference.getUniqueID(context);
                        httpUrlRequest = OPGNetworkRequest.createRequestParams(context, OPGRequest.getUpdatePanellistProfileEntity(OPGPreference.getUniqueID(context), panellistProfile), OPGSDKConstant.updatePanellistProfileAPIRoute, uniqueID);
                        response = OPGNetworkRequest.performRequest(httpUrlRequest);
                    } else {
                        opgUpdatePanellistProfile.setSuccess(false);
                        opgUpdatePanellistProfile.setStatusMessage(SESSION_TIME_OUT_ERROR);
                        return opgUpdatePanellistProfile;
                    }
                }
                opgUpdatePanellistProfile = OPGParseResult.parseUpdatePanellistProfile(response);
            } else {
                StringBuilder builder = new StringBuilder();
                opgUpdatePanellistProfile.setSuccess(false);
                if (!validateString(uniqueID)) {
                    builder.append(ERROR_NULL_UNIQUE_ID).append(NEW_LINE);
                }
                if (!validateString(username_auth)) {
                    builder.append(ERROR_NULL_ADMIN_NAME).append(NEW_LINE);
                }
                if (!validateString(sharedkey_auth)) {
                    builder.append(ERROR_NULL_SHARED_KEY).append(NEW_LINE);
                }
                opgUpdatePanellistProfile.setStatusMessage(builder.toString());

            }
        } catch (Exception exception) {
            opgUpdatePanellistProfile.setSuccess(false);
            opgUpdatePanellistProfile.setStatusMessage(exception.getMessage());
        }
        return opgUpdatePanellistProfile;
    }

    /**
     * Used to upload media to server
     * @param mediaFilePath
     * @throws OPGException
     * @throws Exception
     */

    protected String uploadMediaFile(Context mContext, String mediaFilePath) throws Exception {
        {
            String mediaID = null;
            String uniqueID = OPGPreference.getUniqueID(mContext);

            String username_auth = OPGPreference.getUsername(mContext);
            String sharedkey_auth = OPGPreference.getSharedKey(mContext);
            if (validateString(username_auth) && validateString(sharedkey_auth) /*&& validateString(uniqueID)*/ && validateString(mediaFilePath)) {
                File mediaFile = new File(mediaFilePath);
                if (mediaFile != null && mediaFile.exists()) {
                    try {

                        OPGHttpUrlRequest uploadMediaFileRequest = OPGNetworkRequest.createRequestParams(mContext, null, OPGSDKConstant.MEDIA_POST, uniqueID);
                        String response = OPGNetworkRequest.uploadMediaRequest(uploadMediaFileRequest, mediaFilePath, null, 0);
                        if (!isValidSession(response)) {
                            if (refreshSession(mContext)) {
                                uniqueID = OPGPreference.getUniqueID(mContext);
                                uploadMediaFileRequest = OPGNetworkRequest.createRequestParams(mContext, null, OPGSDKConstant.MEDIA_POST, uniqueID);
                                response = OPGNetworkRequest.uploadMediaRequest(uploadMediaFileRequest, mediaFilePath, null, 0);
                            } else {
                                throw new OPGException(SESSION_TIME_OUT_ERROR);
                            }
                        }
                        mediaID = OPGParseResult.parseMediaUpload(mContext, response);
                    } catch (Exception e) {
                        throw e;
                    }
                } else {
                    throw new OPGException(OPGSDKConstant.ERROR_FILE_NOT_FOUND);
                }
            } else {
                StringBuilder builder = new StringBuilder();
                builder.append(OPGSDKConstant.CAUSED_BY);
                if (!validateString(username_auth)) {
                    //System.out.println("Username");
                    builder.append(ERROR_NULL_ADMIN_NAME).append(NEW_LINE);
                }

                if (!validateString(sharedkey_auth)) {
                    //System.out.println("sharedkey");
                    builder.append(ERROR_NULL_SHARED_KEY).append(NEW_LINE);
                }

                if (!validateString(mediaFilePath)) {
                    //System.out.println("mediapath");
                    builder.append(OPGSDKConstant.ERROR_NULL_MEDIA_PATH).append(NEW_LINE);
                }
                throw new OPGException(builder.toString());
            }
            return mediaID;
        }
    }

    /**
     * This method requires runtime permissions for storage (i.e READ_EXTERNAL_STORAGE , WRITE_EXTERNAL_STORAGE & WRITE_INTERNAL_STORAGE)
     * for android devices 6.0 and above
     * @param mContext
     * @param mediaID
     * @param mediaType
     * @return
     */
    protected OPGDownloadMedia downloadMediaFile(Context mContext, String mediaID, String mediaType) {
        {

            OPGDownloadMedia opgDownloadMedia = new OPGDownloadMedia();
            if (validateString(mediaID) && validateString(mediaType)) {
                try {
                    try {
                        String filePath = searchFile(mContext, mediaID, mediaType);
                        if (filePath != null) {
                            File file = new File(filePath);
                            if (file.exists()) {
                                file.delete();
                            }
                        }
                    } catch (Exception exception) {
                        if (BuildConfig.DEBUG) {
                            Log.e("OPGSDK", exception.getLocalizedMessage());
                        }
                    }
                    String tempDir = Environment.getExternalStorageDirectory() + File.separator + Utils.getApplicationName(mContext) + File.separator + OPG_MEDIA;
                    String fileName = getNewFileName(mContext, mediaID, mediaType);
                    boolean downloadStatus = OPGNetworkRequest.downloadMediaRequest(mContext, mediaID, mediaType, fileName, tempDir);
                    if (downloadStatus) {
                        opgDownloadMedia.setSuccess(true);
                        opgDownloadMedia.setStatusMessage(DOWNLOAD_SUCCESSFUL);
                        opgDownloadMedia.setMediaPath(tempDir + File.separator + fileName);
                    } else {
                        opgDownloadMedia.setSuccess(false);
                        opgDownloadMedia.setStatusMessage(ERROR_FAILED_DOWNLOAD);
                        opgDownloadMedia.setMediaPath(null);
                    }
                } catch (Exception e) {
                    opgDownloadMedia.setSuccess(false);
                    opgDownloadMedia.setStatusMessage(e.toString());
                    opgDownloadMedia.setMediaPath(null);
                }
            } else {
                StringBuilder builder = new StringBuilder();
                builder.append(OPGSDKConstant.CAUSED_BY);

                if (!validateString(mediaID)) {
                    //System.out.println("mediapath");
                    builder.append(OPGSDKConstant.ERROR_NULL_MEDIA_PATH).append(NEW_LINE);
                }

                if (!validateString(mediaType)) {
                    //System.out.println("mediapath");
                    builder.append(ERROR_NULL_MEDIA_TYPE).append(NEW_LINE);
                }
                opgDownloadMedia.setSuccess(false);
                opgDownloadMedia.setStatusMessage(builder.toString());
                opgDownloadMedia.setMediaPath(null);
            }
            return opgDownloadMedia;
        }
    }

    private String searchFile(Context mContext, String mediaID, String mediaType) {
        String tempDir = Environment.getExternalStorageDirectory() + File.separator + Utils.getApplicationName(mContext) + File.separator + OPG_MEDIA;
        File[] files = new File(tempDir).listFiles();
        if (files != null) {
            String filepath = null;
            for (File file : files) {
                if (file.getName().equalsIgnoreCase(mediaID + DOT + mediaType)) {
                    filepath = file.getAbsolutePath();
                    break;
                }
            }
            if (filepath != null) {
                //checking whether image is corrupted or not @Neeraj
                Bitmap bitmap = BitmapFactory.decodeFile(filepath);
                if (bitmap == null) {
                    File file = new File(filepath);
                    if (file.exists())
                        file.delete();
                    filepath = null;
                }
            }
            return filepath;
        }
        return null;
    }

    /**
     *
     * @param context
     */
    protected void logout(Context context) {
        OPGPreference.clearOPGPreference(context);
    }

    /**
     *
     * @param value
     * @return
     */
    protected boolean validateString(String value) {
        return !(value == null || value.isEmpty());
    }

    /**
     *
     * @param context
     * @param deviceToken
     * @return
     */
    @SuppressWarnings("HardwareIds")
    protected String register(Context context, String deviceToken) {
        String response = null;
        try {
            String uniqueID = OPGPreference.getUniqueID(context);
            String sharedkey_auth = OPGPreference.getSharedKey(context);
            String username_auth = OPGPreference.getUsername(context);
            String appVersion = OPGPreference.getAppVersion(context);
            if (validateString(deviceToken) && validateString(uniqueID) && validateString(sharedkey_auth) && validateString(username_auth) && validateString(appVersion)) {
                String deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                JSONObject notificationRegisterEntity = OPGRequest.getNotificationEntity(uniqueID, deviceToken, appVersion, deviceID);
                OPGHttpUrlRequest httpUrlRequest = OPGNetworkRequest.createRequestParams(context, notificationRegisterEntity, OPGSDKConstant.notificationRegisterAPIRoute, uniqueID);
                response = OPGNetworkRequest.performRequest(httpUrlRequest);
                if (!isValidSession(response)) {
                    if (refreshSession(context)) {
                        uniqueID = OPGPreference.getUniqueID(context);
                        httpUrlRequest = OPGNetworkRequest.createRequestParams(context, OPGRequest.getNotificationEntity(OPGPreference.getUniqueID(context), deviceToken, appVersion, deviceID), OPGSDKConstant.notificationRegisterAPIRoute, uniqueID);
                        response = OPGNetworkRequest.performRequest(httpUrlRequest);
                    } else {
                        response = SESSION_TIME_OUT_ERROR;
                    }
                }
            } else {
                StringBuilder builder = new StringBuilder();
                if (!validateString(uniqueID)) {
                    builder.append(ERROR_NULL_UNIQUE_ID).append(NEW_LINE);
                }
                if (!validateString(username_auth)) {
                    builder.append(ERROR_NULL_ADMIN_NAME).append(NEW_LINE);
                }
                if (!validateString(sharedkey_auth)) {
                    builder.append(ERROR_NULL_SHARED_KEY).append(NEW_LINE);
                }
                if (!validateString(appVersion)) {
                    builder.append(ERROR_NULL_APP_VERSION).append(NEW_LINE);
                }
                if (!validateString(deviceToken)) {
                    builder.append(ERROR_NULL_DEVICE_TOKEN).append(NEW_LINE);
                }
                response = builder.toString();
            }
        } catch (Exception exception) {
            response = exception.getMessage();
        }
        return response;
    }


    /**
     *
     * @param context
     * @param deviceToken
     * @return
     */
    @SuppressWarnings("HardwareIds")
    protected String unRegister(Context context, String deviceToken) {
        String response = null;
        try {
            String uniqueID = OPGPreference.getUniqueID(context);
            String sharedkey_auth = OPGPreference.getSharedKey(context);
            String username_auth = OPGPreference.getUsername(context);
            String appVersion = OPGPreference.getAppVersion(context);
            if (validateString(deviceToken) && validateString(uniqueID) && validateString(sharedkey_auth) && validateString(username_auth) && validateString(appVersion)) {
                String deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                JSONObject notificationUnRegisterEntity = OPGRequest.getNotificationEntity(uniqueID, deviceToken, appVersion, deviceID);
                OPGHttpUrlRequest httpUrlRequest = OPGNetworkRequest.createRequestParams(context, notificationUnRegisterEntity, OPGSDKConstant.notificationUnRegisterAPIRoute, uniqueID);
                response = OPGNetworkRequest.performRequest(httpUrlRequest, OPGSDKConstant.DELETE);
                if (!isValidSession(response)) {
                    if (refreshSession(context)) {
                        uniqueID = OPGPreference.getUniqueID(context);
                        httpUrlRequest = OPGNetworkRequest.createRequestParams(context, OPGRequest.getNotificationEntity(OPGPreference.getUniqueID(context), deviceToken, appVersion, deviceID), OPGSDKConstant.notificationUnRegisterAPIRoute, uniqueID);
                        response = OPGNetworkRequest.performRequest(httpUrlRequest);
                    } else {
                        response = SESSION_TIME_OUT_ERROR;
                    }
                }
            } else {
                StringBuilder builder = new StringBuilder();
                if (!validateString(uniqueID)) {
                    builder.append(ERROR_NULL_UNIQUE_ID).append(NEW_LINE);
                }
                if (!validateString(username_auth)) {
                    builder.append(ERROR_NULL_ADMIN_NAME).append(NEW_LINE);
                }
                if (!validateString(sharedkey_auth)) {
                    builder.append(ERROR_NULL_SHARED_KEY).append(NEW_LINE);
                }
                if (!validateString(appVersion)) {
                    builder.append(ERROR_NULL_APP_VERSION).append(NEW_LINE);
                }
                if (!validateString(deviceToken)) {
                    builder.append(ERROR_NULL_DEVICE_TOKEN).append(NEW_LINE);
                }

                response = builder.toString();
            }
        } catch (Exception exception) {
            response = exception.getMessage();
        }
        return response;
    }


    /**
     *
     * @param context
     * @return
     */
    protected OPGPanellistPanel getPanellistPanel(Context context) {
        OPGPanellistPanel opgPanellistPanel = new OPGPanellistPanel();
        try {
            String uniqueID = OPGPreference.getUniqueID(context);
            String sharedkey_auth = OPGPreference.getSharedKey(context);
            String username_auth = OPGPreference.getUsername(context);
            if (validateString(uniqueID) && validateString(sharedkey_auth) && validateString
                    (username_auth)) {
                OPGHttpUrlRequest httpUrlRequest = OPGNetworkRequest.createRequestParams(context, null, OPGSDKConstant.panellistPanelAPIRoute, uniqueID);
                String response = OPGNetworkRequest.performRequest(httpUrlRequest, OPGSDKConstant.GET);
                if (!isValidSession(response)) {
                    if (refreshSession(context)) {
                        uniqueID = OPGPreference.getUniqueID(context);
                        httpUrlRequest = OPGNetworkRequest.createRequestParams(context, null, OPGSDKConstant.panellistPanelAPIRoute, uniqueID);
                        response = OPGNetworkRequest.performRequest(httpUrlRequest, OPGSDKConstant.GET);
                    } else {
                        throw new OPGException(SESSION_TIME_OUT_ERROR);
                    }
                }
                opgPanellistPanel = OPGParseResult.parsePanellistPanel(response);
            } else {
                StringBuilder builder = new StringBuilder();
                if (!validateString(uniqueID)) {
                    builder.append(ERROR_NULL_UNIQUE_ID).append(NEW_LINE);
                }
                if (!validateString(username_auth)) {
                    builder.append(ERROR_NULL_ADMIN_NAME).append(NEW_LINE);
                }
                if (!validateString(sharedkey_auth)) {
                    builder.append(ERROR_NULL_SHARED_KEY).append(NEW_LINE);
                }
                opgPanellistPanel.setSuccess(false);
                opgPanellistPanel.setStatusMessage(builder.toString());
            }
        } catch (Exception exception) {
            opgPanellistPanel.setSuccess(false);
            opgPanellistPanel.setStatusMessage(exception.getMessage());
        }
        return opgPanellistPanel;
    }


    protected ArrayList<OPGCountry> getCountries(Context context) throws OPGException {
        ArrayList<OPGCountry> opgCountries = null;
        try {

            String uniqueID = OPGPreference.getUniqueID(context);
            String sharedkey_auth = OPGPreference.getSharedKey(context);
            String username_auth = OPGPreference.getUsername(context);
            if (validateString(uniqueID) && validateString(sharedkey_auth) && validateString(username_auth)) {

                OPGHttpUrlRequest httpUrlRequest = OPGNetworkRequest.createRequestParams(context, null, OPGSDKConstant.countriesAPIRoute, uniqueID);
                String response = OPGNetworkRequest.performRequest(httpUrlRequest, OPGSDKConstant.GET);
                if (!isValidSession(response)) {
                    if (refreshSession(context)) {
                        uniqueID = OPGPreference.getUniqueID(context);
                        httpUrlRequest = OPGNetworkRequest.createRequestParams(context, null, OPGSDKConstant.countriesAPIRoute, uniqueID);
                        response = OPGNetworkRequest.performRequest(httpUrlRequest, OPGSDKConstant.GET);
                    } else {
                        throw new OPGException(SESSION_TIME_OUT_ERROR);
                    }
                }
                opgCountries = (ArrayList<OPGCountry>) OPGParseResult.parseCountryList(response);
            } else {
                StringBuilder builder = new StringBuilder();
                if (!validateString(uniqueID)) {
                    builder.append(ERROR_NULL_UNIQUE_ID).append(NEW_LINE);
                }
                if (!validateString(username_auth)) {
                    builder.append(ERROR_NULL_ADMIN_NAME).append(NEW_LINE);
                }
                if (!validateString(sharedkey_auth)) {
                    builder.append(ERROR_NULL_SHARED_KEY).append(NEW_LINE);
                }
                throw new OPGException(builder.toString());
            }
        } catch (Exception exception) {
            //Log.i(OPGRoot.class.getName(),exception.getMessage());
            throw new OPGException(exception.getMessage());
        }
        return opgCountries;
    }

    /**
     *
     * @param mContext
     * @param mediaType
     * @return
     * @throws InvalidMediaTypeException
     */

    String getNewFileName(Context mContext,String mediaID,String mediaType) throws InvalidMediaTypeException
    {
        StringBuilder fileName = new StringBuilder();
        fileName.append(mediaID).append(DOT).append(mediaType);
        return fileName.toString();
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
        OPGGeofenceMonitor.getInstance().startGeofencingMonitor(mContext, googleApiClient, opgGeofencesList, opgGeofenceTriggerEvents);
    }

    /**
     *
     * @param mContext
     * @param googleApiClient
     * @throws OPGException
     */
    protected void stopGeofencingMonitor(final Context mContext, GoogleApiClient googleApiClient, final OPGGeofenceTriggerEvents opgGeofenceTriggerEvents) throws OPGException {
        OPGGeofenceMonitor.getInstance().stopGeofencingMonitor(mContext, googleApiClient, opgGeofenceTriggerEvents);
    }

    public OPGGeofenceTriggerEvents getOpgGeofenceTriggerEvents() {
        return OPGGeofenceMonitor.getInstance().getOpgGeofenceTriggerEvents();
    }

    public void setOpgGeofenceTriggerEvents(OPGGeofenceTriggerEvents opgGeofenceTriggerEvents) {
        OPGGeofenceMonitor.getInstance().setOpgGeofenceTriggerEvents(opgGeofenceTriggerEvents);
    }

    protected HashMap<String,String> getThemesForPanel(Context context, long panelThemeTemplateID, List<OPGTheme> opgThemes)
    {
        HashMap<String, String> hashMap = new HashMap<String, String>(5);
        for (OPGTheme opgTheme : opgThemes) {
            if (panelThemeTemplateID == opgTheme.getThemeTemplateID()) {
                hashMap.put(opgTheme.getName(), opgTheme.getValue());
            }
        }
        return hashMap;
    }


}

