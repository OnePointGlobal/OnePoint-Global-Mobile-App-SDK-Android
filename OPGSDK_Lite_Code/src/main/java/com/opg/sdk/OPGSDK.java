package com.opg.sdk;

import android.content.Context;

import com.allatori.annotations.DoNotRename;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.opg.sdk.models.OPGScript;
import com.opg.sdk.models.OPGSurvey;
import com.opg.sdk.models.OPGTheme;
import com.opg.sdk.models.OPGUpdatePanellistProfile;
import com.opg.sdk.models.OPGUploadResult;

import java.util.ArrayList;
import java.util.HashMap;
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
 * OPGSDK class is the main class of OPGSDK. .
 * It has all the API's to access OPDSDK.
 */
@DoNotRename
public class OPGSDK
{
    /**
     * returns whether SDK is full or lite.
     * @return boolean
     */
    @DoNotRename
    public static boolean isLiteSDK()
    {
        return true;
    }

    /**
     * It is mandatory to initialize the SDK at app launch. The initialize API
     * must be invoked on the UI Thread, preferably in the onCreate method of
     * the launcher activity.
     *
     * @param username  The input value is the admin username.
     * @param sharedKey The input value is the admin sdk key.
     * @param context   The context to use. Usually your android.app.Application or
     *                  android.app.Activity object.
     * @throws OPGException
     */
    @DoNotRename
    public static void initialize(String username, String sharedKey, Context context) throws
            OPGException {
        OPGRoot.getInstance().initialize(username, sharedKey, context);
    }

    /**
     * This method downloads the media file taking media ID, media type and context as input
     * parameters. It returns an object of type OPGDownloadMedia.
     * It throws OPGException.
     * @param mediaID The mediaId of media
     * @param mediaType The type of media
     * @param context The Context
     * @return OPGDownloadMedia object
     */
    @DoNotRename
    public static OPGDownloadMedia downloadMediaFile(Context context, String mediaID, String mediaType)
    {
        return OPGRoot.getInstance().downloadMediaFile(context, mediaID, mediaType);
    }

    /**
     * This method authenticates the user to use the SDK. It returns an OPGAuthenticate object
     * taking panelist username, panelist password and context as input.
     *
     * @param userName The app login username
     * @param password The app login password
     * @param mContext The Context
     * @return OPGAuthenticate
     * @throws Exception
     */
    @DoNotRename
    public OPGAuthenticate authenticate(String userName, String password, Context mContext)
    {
        OPGPreference.setLoginType(mContext,0);
        return OPGRoot.getInstance().authenticate(userName, password, mContext);
    }

    /**
     * This method authenticates the user to use the SDK. It returns an OPGAuthenticate object
     * taking user google tokenID and context as input.
     * @param googleTokenID
     * @param mContext
     * @return
     */
    @DoNotRename
    public OPGAuthenticate authenticateWithGoogle(String googleTokenID ,Context mContext)
    {
        OPGPreference.setLoginType(mContext,1);
        return OPGRoot.getInstance().authenticateWithGoogle(googleTokenID,mContext);
    }

    /**
     * This method authenticates the user to use the SDK. It returns an OPGAuthenticate object
     * taking user facebook tokenID and context as input.
     * @param facebookTokenID
     * @param mContext
     * @return
     */
    @DoNotRename
    public OPGAuthenticate authenticateWithFacebook(String facebookTokenID ,Context mContext)
    {
        OPGPreference.setLoginType(mContext,2);
        return OPGRoot.getInstance().authenticateWithFacebook(facebookTokenID,mContext);
    }

    /**
     * To get the UniqueID
     *
     * @param context The Context
     * @return uniqueId The uniqueId for individual panelist
     */

    @DoNotRename
    public String getUniqueID(Context context) {
        return OPGRoot.getInstance().getUniqueID(context);
    }

    /**
     * To set the uniqueID for particular panelist.
     * It throws Exception.
     * @param uniqueID The uniqueID for individual panelist
     * @param context The Context
     * @throws OPGException
     */

    @DoNotRename
    public void setUniqueID(String uniqueID, Context context) throws OPGException
    {
        OPGRoot.getInstance().setUniqueID(uniqueID, context);
    }

    /**
     * To set the App version.
     *It throws Exception.
     * @param appVersion App version
     * @param context The Context
     * @throws OPGException
     */
    @DoNotRename
    public void setAppVersion(String appVersion, Context context) throws OPGException
    {
        OPGRoot.getInstance().setAppVersion(appVersion, context);
    }

    /**
     *  To get the App version.
     * @param context The Context
     * @return String appVersion
     */
    @DoNotRename
    public String getAppVersion(Context context)
    {
        return OPGRoot.getInstance().getAppVersion(context);
    }

    /**
     * This method sends a mail containing the link to reset your password. It takes e-mail id
     * and context as input parameters and returns an object of type OPGForgotPassword.
     * It throws Exception.
     * @param mailId The registered emailID of panelist.
     * @param mContext The context
     * @return OPGForgotPassword object
     * @throws Exception
     */
    @DoNotRename
    public OPGForgotPassword forgotPassword(String mailId, Context mContext) throws Exception
    {
        return OPGRoot.getInstance().forgotPassword(mailId, mContext);
    }

    /**
     * This method returns the list of all the surveys.
     * It takes context as input parameter.
     * It throws OPGException.
     * @param context The Context
     * @return The Lis of OPGSurvey object
     * @throws OPGException
     */
    @DoNotRename
    public ArrayList<OPGSurvey> getUserSurveyList(Context context) throws OPGException {
        return OPGRoot.getInstance().getUserSurveyList(context);
    }

    /**
     * This method returns the list of all the surveys for a particular panel ID.
     * It takes context and panelID as input parameters.
     * It throws OPGException.
     * @param context The Context
     * @param panelID The PanelID
     * @return The list of OPGSurvey Object
     * @throws OPGException
     */
    public List<OPGSurvey> getSurveys(Context context,String panelID) throws OPGException
    {
        return  OPGRoot.getInstance().getSurveys(context,panelID);
    }

    /**
     * This method is to get list of surveys based on admin username and sharedkey.
     * @param context
     * @return
     * @throws OPGException
     */
    public List<OPGSurvey> getSurveyList(Context context) throws OPGException
    {
        return  OPGRoot.getInstance().getSurveyList(context);
    }

    /**
     * This method allows you to change your password. It takes current password, new password
     * and context as input parameters and returns an object of type OPGChangePassword.
     *It throws OPGException.
     * @param currentPassword The current app login password
     * @param newPassword The new password
     * @return OPGChangePassword object
     */
    @DoNotRename
    public OPGChangePassword changePassword(Context context, String currentPassword, String newPassword) throws Exception {
        return OPGRoot.getInstance().changePassword(context, currentPassword, newPassword);
    }

    /**
     * This method takes an context as input parameter and returns the OPGPanelistProfile object.
     * @param context The Context
     * @return OPGPanellistProfile object
     */
    @DoNotRename
    public OPGPanellistProfile getPanellistProfile(Context context)
    {
        return OPGRoot.getInstance().getPanellistProfile(context);
    }

    /**
     * This method downloads the script file of a survey, taking SurveyReference  and Context as input
     * parameters.
     * SurveyReference is property of OPGSurvey object.
     * @param context The Context
     * @param surveyReference The surveyReference for particular OPGSurvey
     * @return OPGScript object
     */
    @DoNotRename
    public OPGScript getScript(Context context, String surveyReference)
    {
        if(OPGSDK.isLiteSDK())
        {
           // Toast.makeText(context,context.getString(R.string.msg_sdk_lite),Toast.LENGTH_SHORT).show();
            return null;
        }else{
            return OPGRoot.getInstance().getScript(context, surveyReference);
        }
    }

    @DoNotRename
    public void getScript(Context context, String surveyReference, OPGProgressUpdateInterface opgProgressUpdateInterface)
    {
        if(OPGSDK.isLiteSDK())
        {
            //Toast.makeText(context,context.getString(R.string.msg_sdk_lite),Toast.LENGTH_SHORT).show();
        }else{
            OPGRoot.getInstance().getScript(context,surveyReference, opgProgressUpdateInterface);
        }
    }

/**
 * This method takes Context as input parameter and returns  list of  OPGPanelPanelist object.
 * It throws OPGException.
 * @param context The Context
 * @return The list of OPGPanelPanelist object
 * @throws Exception
 */

/*@DoNotRename
public List<OPGPanelPanelist> getPanelPanelist(Context context) throws Exception
{
    return OPGRoot.getInstance().getPanelPanlist(context);
}*/

/**
 * This method takes Context as input parameter and returns the list  of OPGPanel objects.
 * It throws OPGException.
 * @param context The Context
 * @return List<OPGPanel> The list of OPGPanel objects
 * @throws OPGException
 */
/*@DoNotRename
public List<OPGPanel> getPanels(Context context) throws OPGException
{
    return OPGRoot.getInstance().getPanels(context);
}*/

/**
 * This method takes Context as input parameter and returns the list of OPGTheme objects.
 * It throws OPGException.
 * @param context The Context
 * @return List<OPGTheme>  The list of OPGTheme objects
 * @throws OPGException
 */
/*@DoNotRename
public List<OPGTheme> getThemes(Context context) throws OPGException
{
    return OPGRoot.getInstance().getThemes(context);
}*/

/**
 * This method takes Context as input parameter and returns an array of OPGSurveyPanel objects.
 * It throws OPGException.
 * @param context The Context
 * @return List<OPGSurveyPanel> The list of OPGSurveyPanel object
 * @throws OPGException
 */
/*@DoNotRename
public List<OPGSurveyPanel> getSurveyPanels(Context context) throws OPGException
{
    return OPGRoot.getInstance().getSurveyPanels(context);
}*/

    /**
     * This method update the profile of panelist.
     * This method takes an OPGPanelistProfile object and Context as input parameters and returns the
     * OPGUpdatePanelistProfile object.
     *
     * @param context The Context
     * @param panellistProfile The OPGPanelistProfile object
     * @return OPGUpdatePanelistProfile object
     */
    @DoNotRename
    public OPGUpdatePanellistProfile updatePanellistProfile(Context context, OPGPanellistProfile panellistProfile)
    {
        return OPGRoot.getInstance().updatePanellistProfile(context, panellistProfile);
    }

    /**
     * This method uploads a media file taking the media path and Context as input parameters. It
     * returns a string which is the Media ID of the file uploaded.
     *
     * @param mediaPath The path of the media to be upload
     * @param context The Context
     * @return String the mediaId
     * @throws Exception
     */
    @DoNotRename
    public String uploadMediaFile(String mediaPath, Context context) throws Exception
    {
        return OPGRoot.getInstance().uploadMediaFile(context, mediaPath);
    }

    /**
     * This method terminates the session with the SDK. So you need to
     * login again to call the OPGSDK API's.
     *
     * @param context The Context
     *    @return
     */

    @DoNotRename
    public void logout(Context context)
    {
        OPGRoot.getInstance().logout(context);
    }

    /* @DoNotRename
     public void  uploadResults(Context context,String surveyReference,String panelistID)
     {
     OPGRoot.getInstance().uploadResults(context,surveyReference,panelistID);
     }*/

    /**
      * This method takes a device token ID and Context as input parameters and returns the String value to register
     * the user for push notifications service.
     * @param context
     * @param deviceToken
     * @return String
     */
    @DoNotRename
    public String registerNotifications(Context context,String deviceToken)
    {
        return   OPGRoot.getInstance().register(context,deviceToken);
    }

    /**
     * This method takes a device token ID and Context as input parameters and returns the String value to unregister
     * the user for push notifications service.
     * @param context
     * @param deviceToken
     * @return String
     */
    @DoNotRename
    public String unRegisterNotifications(Context context,String deviceToken)
    {
        return  OPGRoot.getInstance().unRegister(context,deviceToken);
    }

    /**
     * This method takes Context as input parameter and returns the OPGPanellistPanel object.
     * @param context
     * @return OPGPanellistPanel
     */
    @DoNotRename
    public OPGPanellistPanel getPanellistPanel(Context context)
    {
        return OPGRoot.getInstance().getPanellistPanel(context);
    }

    /**
     * This method returns the list of OPGCountries to be used.
     * @param context
     * @return
     * @throws OPGException
     */

    @DoNotRename
    public List<OPGCountry> getCountries(Context context) throws OPGException {
        return OPGRoot.getInstance().getCountries(context);
    }

    /**
     *This will give  the list of geofence surveys for this location.
     * This will take input as context, latitude and longitude and returns the OPGGeofenceSurvey list.
     * @param context
     * @param latitude
     * @param longitude
     * @return
     * @throws OPGException
     */
    @DoNotRename
    public List<OPGGeofenceSurvey> getGeofenceSurveys(Context context, float latitude, float longitude)  throws OPGException
    {
        return OPGRoot.getInstance().getGeofenceSurveys(context,latitude,longitude);
    }


    /**
     * This method is used to start monitoring based on the given OPGGeofenceSurveys list.
     * And it gives the status of the monitoring in the callback onResult()
     * method of the OPGGeofenceTriggerEvents which is passed to this method..
     * @param mContext
     * @param googleApiClient
     * @param opgGeofencesList
     * @param opgGeofenceTriggerEvents
     * @throws OPGException
     */
    @DoNotRename
    public void startGeofencingMonitor(Context mContext, GoogleApiClient googleApiClient, List<OPGGeofenceSurvey> opgGeofencesList,
                                       OPGGeofenceTriggerEvents opgGeofenceTriggerEvents) throws OPGException{
        OPGRoot.getInstance().startGeofencingMonitor(mContext,googleApiClient,opgGeofencesList,opgGeofenceTriggerEvents);
    }

    /**
     * This method is used to stop monitoring the geofences.
     * And it gives the status of the monitoring in the callback onResult()
     * method of the OPGGeofenceTriggerEvents which is passed to this method..
     * @param mContext
     * @param googleApiClient
     * @param opgGeofenceTriggerEvents
     * @throws OPGException
     */
    @DoNotRename
    public void stopGeofencingMonitor(Context mContext,GoogleApiClient googleApiClient,
                                      OPGGeofenceTriggerEvents opgGeofenceTriggerEvents) throws OPGException{
        OPGRoot.getInstance().stopGeofencingMonitor(mContext,googleApiClient,opgGeofenceTriggerEvents);
    }


    public OPGGeofenceTriggerEvents getOpgGeofenceTriggerEvents(){
        return OPGRoot.getInstance().getOpgGeofenceTriggerEvents();
    }

    /**
     * This method will upload the offline survey results for particular panelist.
     * @param context
     * @param surveyId
     * @param panelID
     * @param panelistID
     */
    @DoNotRename
    public OPGUploadResult uploadSurveyResults(Context context, long surveyId,long panelID, long panelistID, OPGUploadProgress opgUploadProgress)
    {
        if(OPGSDK.isLiteSDK())
        {
          //  Toast.makeText(context,context.getString(R.string.msg_sdk_lite),Toast.LENGTH_SHORT).show();
            return null;
        }else{
            return OPGRoot.getInstance().uploadResults(context,surveyId+EMPTY_STRING,panelID+EMPTY_STRING,panelistID+EMPTY_STRING,opgUploadProgress);
        }

    }

    /**
     * This  will upload all results present in SDCard.
     * @param context
     * @param opgUploadProgress
     */
    @DoNotRename
    public void uploadAllResults(Context context,OPGUploadProgress opgUploadProgress)
    {

        if(OPGSDK.isLiteSDK())
        {
          //  Toast.makeText(context,context.getString(R.string.msg_sdk_lite),Toast.LENGTH_SHORT).show();
        }else{
            OPGRoot.getInstance().uploadAllResults(context,opgUploadProgress);
        }
    }

    /**
     * This method return the number of time survey taken
     * @param surveyId
     * @param panelID
     * @param panelistID
     * @return
     */
    @DoNotRename
    public int getSurveyTakenCount(Context context,long surveyId,long panelID, long panelistID)
    {
        if(OPGSDK.isLiteSDK())
        {
            //Toast.makeText(context,context.getString(R.string.msg_sdk_lite),Toast.LENGTH_SHORT).show();
            return 0;
        }else{
            return  OPGRoot.getInstance().getSurveyTakenCount(context,surveyId,panelID,panelistID);
        }
    }

    /**
     * Checks whether any result is present or not.
     * @return
     */
    @DoNotRename
    public boolean isSurveyResultsPresent(Context context)
    {
        return  OPGRoot.getInstance().isSurveyResultsPresent(context);
    }

    @DoNotRename
    public static List<OPGGeofenceSurvey> getOPGGeofenceSurveys(Context context){
        return OPGRoot.getInstance().getOpgGeofenceSurveyList(context);
    }

    @DoNotRename
    public static void setOPGGeofenceSurveys(Context context , List<OPGGeofenceSurvey> opgGeofenceSurveyList){
        OPGRoot.getInstance().setOpgGeofenceSurveyList(context,opgGeofenceSurveyList);
    }

    /**
     * This method will return the HashMap  of themeKey and value for particular panel.
     * @param context
     * @param panelThemeTemplateID
     * @param opgThemes
     * @return HashMap  of themeKey and value
     */
    @DoNotRename
    public HashMap<String,String> getThemesForPanel(Context context, long panelThemeTemplateID, List<OPGTheme> opgThemes)
    {
        return  OPGRoot.getInstance().getThemesForPanel(context,panelThemeTemplateID,opgThemes);
    }

}
