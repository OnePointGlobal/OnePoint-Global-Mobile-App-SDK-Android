package com.opg.sdk.restclient;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opg.sdk.Aes256;
import com.opg.sdk.BuildConfig;
import com.opg.sdk.OPGPreference;
import com.opg.sdk.OPGR;
import com.opg.sdk.OPGSDKConstant;
import com.opg.sdk.exceptions.OPGException;
import com.opg.sdk.models.OPGAuthenticate;
import com.opg.sdk.models.OPGChangePassword;
import com.opg.sdk.models.OPGCountry;
import com.opg.sdk.models.OPGForgotPassword;
import com.opg.sdk.models.OPGGeofenceSurvey;
import com.opg.sdk.models.OPGPanel;
import com.opg.sdk.models.OPGPanelPanellist;
import com.opg.sdk.models.OPGPanellistPanel;
import com.opg.sdk.models.OPGPanellistProfile;
import com.opg.sdk.models.OPGScript;
import com.opg.sdk.models.OPGSurvey;
import com.opg.sdk.models.OPGSurveyPanel;
import com.opg.sdk.models.OPGTheme;
import com.opg.sdk.models.OPGUpdatePanellistProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import OnePoint.Common.Utils;

import static com.opg.sdk.OPGSDKConstant.COLON;
import static com.opg.sdk.OPGSDKConstant.ERROR_FORGOT_PASSWORD_KEY;
import static com.opg.sdk.OPGSDKConstant.NEW_LINE;
import static com.opg.sdk.OPGSDKConstant.STRING;

/**
 * This class has methods which is used to parse json data to respective api model object
 * @author Neeraj
 *
 */
public class OPGParseResult
{
    /*HTTP Status Code*/
    private static String CODE_200 = "200";


    private static Gson getGsonInstance()
    {
        return  new GsonBuilder().setDateFormat(OPGSDKConstant.DATE_FORMAT_T).create();
    }

    /**
     * To parse the response of Authentication
     * @param authResponse
     * @return OPGAuthenticate
     * @throws JSONException
     */
    public static OPGAuthenticate parseAuthenticate(Context context, String authResponse) throws JSONException
    {
        OPGAuthenticate opgAuthenticate = new OPGAuthenticate();
        if (authResponse != null || !authResponse.isEmpty())
        {
            JSONObject opgJsonResponse = new JSONObject(authResponse);
            if (opgJsonResponse.has(OPGSDKConstant.MESSAGE))
            {
                opgAuthenticate.setSuccess(false);
                opgAuthenticate.setStatusMessage(opgJsonResponse.getString(OPGSDKConstant.ERROR_MESSAGE));
                opgAuthenticate.setHttpStatusCode(opgJsonResponse.getLong(OPGSDKConstant.HTTP_STATUS_CODE));
            }
            else
            {
                opgAuthenticate.setSuccess(true);
                opgAuthenticate.setStatusMessage(OPGSDKConstant.SUCCESS);
                opgAuthenticate.setUniqueID(opgJsonResponse.getString(OPGSDKConstant.UNIQUE_ID));
                OPGPreference.setUniqueID(opgJsonResponse.getString(OPGSDKConstant.UNIQUE_ID), context);
                if (opgJsonResponse.has(OPGSDKConstant.URL) && !opgJsonResponse.getString(OPGSDKConstant.URL).isEmpty() && !opgJsonResponse.getString(OPGSDKConstant.URL).equals(OPGPreference.getApiURL(context)))
                {
                    OPGPreference.setApiURL(opgJsonResponse.getString(OPGSDKConstant.URL), context);
                }
                if (opgJsonResponse.has(OPGSDKConstant.INTERVIEW_URL) && !opgJsonResponse.getString(OPGSDKConstant.INTERVIEW_URL).isEmpty() && !opgJsonResponse.getString(OPGSDKConstant.INTERVIEW_URL).equals(OPGPreference.getInterviewURL(context)))
                {
                    OPGPreference.setInterviewURL(opgJsonResponse.getString(OPGSDKConstant.INTERVIEW_URL), context);
                }
            }
            if(opgJsonResponse.has(OPGSDKConstant.HTTP_STATUS_CODE)){
                opgAuthenticate.setHttpStatusCode(opgJsonResponse.getLong(OPGSDKConstant.HTTP_STATUS_CODE));
            }
        }
        else
        {
            opgAuthenticate.setSuccess(false);
            opgAuthenticate.setStatusMessage(OPGSDKConstant.ERROR_AUTH);
        }


        return opgAuthenticate;
    }

    /**
     *To parse the response for OPGChangePassword
     * @param changePwdResponse
     * @return
     * @throws JSONException
     */
    public static OPGChangePassword parseChangePassword(String changePwdResponse ) throws JSONException
    {
        OPGChangePassword opgChangePassword = new OPGChangePassword();
        JSONObject jsonObject = new JSONObject(changePwdResponse);
        if(jsonObject.getString(OPGSDKConstant.HTTP_STATUS_CODE).equals(CODE_200))
        {
            opgChangePassword.setSuccess(true);
            opgChangePassword.setStatusMessage(jsonObject.getString(OPGSDKConstant.MESSAGE));
        }
        else
        {
            opgChangePassword.setSuccess(false);
            opgChangePassword.setStatusMessage(jsonObject.getString(OPGSDKConstant.ERROR_MESSAGE));
            opgChangePassword.setHttpStatusCode(jsonObject.getLong(OPGSDKConstant.HTTP_STATUS_CODE));
        }
        return opgChangePassword;

    }


    /**
     * To parse the response for OPGForgotPassword
     * @param context
     * @param forgotPwdResponse
     * @return
     * @throws JSONException
     */
    public static OPGForgotPassword parseForgotPassword(Context context, String forgotPwdResponse) throws JSONException
    {
        OPGForgotPassword opgForgotPassword = new OPGForgotPassword();
        if (forgotPwdResponse != null || !forgotPwdResponse.isEmpty())
        {
            JSONObject opgJsonResponse = new JSONObject(forgotPwdResponse);
            opgForgotPassword.setHttpStatusCode(opgJsonResponse.getInt(OPGSDKConstant.HTTP_STATUS_CODE));
            if(200 == opgJsonResponse.getInt(OPGSDKConstant.HTTP_STATUS_CODE))
            {
                opgForgotPassword.setSuccess(true);
                opgForgotPassword.setStatusMessage(opgJsonResponse.getString(OPGSDKConstant.MESSAGE));
            }
            else
            {
                opgForgotPassword.setSuccess(false);
                opgForgotPassword.setStatusMessage(opgJsonResponse.getString(OPGSDKConstant.ERROR_MESSAGE));
            }
        }
        else
        {
            opgForgotPassword.setSuccess(false);
            opgForgotPassword.setStatusMessage(OPGR.getString(context,STRING,ERROR_FORGOT_PASSWORD_KEY));
        }
        return opgForgotPassword;
    }

    /**
     *To parse the response of Authentication
     * @param context
     * @param panelistProfileResponse
     * @return
     * @throws Exception
     */
    public static OPGPanellistProfile parsePanellistProfile(Context context, String panelistProfileResponse) throws Exception
    {
        OPGPanellistProfile panelistProfile = new OPGPanellistProfile();
        if (panelistProfileResponse != null && !panelistProfileResponse.isEmpty())
        {
            Gson gson = getGsonInstance();
            panelistProfile = gson.fromJson(panelistProfileResponse, OPGPanellistProfile.class);
            panelistProfile.setSuccess(true);
            panelistProfile.setStatusMessage(OPGSDKConstant.SUCCESS);
            if (panelistProfile.getErrorMessage() != null)
            {
                panelistProfile.setSuccess(false);
                panelistProfile.setStatusMessage(panelistProfile.getErrorMessage());
            }else{
                JSONObject jsonObject = new JSONObject(panelistProfileResponse);
                if(jsonObject.has(OPGSDKConstant.COUNTRY)){
                    OPGCountry countryGSON = gson.fromJson(jsonObject.getJSONObject(OPGSDKConstant.COUNTRY).toString(),OPGCountry.class);
                    panelistProfile.setCountryName(countryGSON.getCountryName());
                    panelistProfile.setStd(countryGSON.getStd());
                }
            }
        }
        else
        {
            panelistProfile.setSuccess(false);
            panelistProfile.setStatusMessage(OPGSDKConstant.ERROR_PANELIST_PROFILE);
        }
        return panelistProfile;
    }



    /**
     *To parse the response for OPGUpdatePanelistProfile
     * @param updateProfileResponse
     * @return
     * @throws JSONException
     */
    public static OPGUpdatePanellistProfile parseUpdatePanellistProfile(String updateProfileResponse) throws JSONException
    {
        OPGUpdatePanellistProfile updatePanelistProfile = new OPGUpdatePanellistProfile();
        if(updateProfileResponse != null && !updateProfileResponse.isEmpty())
        {
            JSONObject jsonObject = new JSONObject(updateProfileResponse);
            if(jsonObject.has(OPGSDKConstant.HTTP_STATUS_CODE )&& jsonObject.getInt(OPGSDKConstant.HTTP_STATUS_CODE) != 200)
            {
                updatePanelistProfile.setSuccess(false);
                updatePanelistProfile.setStatusMessage(jsonObject.getString(OPGSDKConstant.ERROR_MESSAGE));
            }
            else
            {
                updatePanelistProfile.setSuccess(true);
                updatePanelistProfile.setStatusMessage(OPGSDKConstant.SUCCESS);
            }
        }
        else
        {
            updatePanelistProfile.setSuccess(false);
            updatePanelistProfile.setStatusMessage(OPGSDKConstant.ERROR_UPDATE_PANELIST_PROFILE);
        }
        return updatePanelistProfile;
    }

    /**
     *To parse the response for OPGScript
     * @param surveyScriptResponse
     * @return
     * @throws Exception
     */
    /*public static OPGScript parseSurveyScript(Context context,String surveyScriptResponse,String surveyID) throws Exception
    {
        OPGScript opgScript = new OPGScript();
        opgScript.setSurveyRef(surveyID);
        if (surveyScriptResponse != null && !surveyScriptResponse.isEmpty())
        {
            JSONObject jsonObject = new JSONObject(surveyScriptResponse);
            if(jsonObject.has(OPGSDKConstant.HTTP_STATUS_CODE))
            {
                opgScript.setSuccess(false);
                opgScript.setStatusMessage(jsonObject.getString(OPGSDKConstant.MESSAGE));
            }
            else
            {
                opgScript.setSuccess(true);
                opgScript.setStatusMessage(OPGSDKConstant.SUCCESS);
                JSONObject scriptJson = jsonObject.getJSONObject(OPGSDKConstant.SCRIPT_CONTENT);
                String byteCode = scriptJson.getString(OPGSDKConstant.BYTE_CODE);
                if (byteCode != null && byteCode.length() > 0)
                {
                    opgScript.setScriptFilePath(saveScriptFile(context,byteCode, surveyID));
                }
                else
                {
                    opgScript.setSuccess(false);
                    opgScript.setStatusMessage(OPGSDKConstant.ERROR_NO_SCRIPT);
                }
            }
        }
        else
        {
            opgScript.setSuccess(false);
            opgScript.setStatusMessage(OPGSDKConstant.ERROR_SCRIPT);
        }
        return opgScript;
    }*/

    /*private static String saveScriptFile(Context context,String data,String surveyID)
    {
        String tmpDir;
        try
        {
            File tempFile  = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Utils.getApplicationName(context) + File.separator + OPGSDKConstant.SCRIPTS);
            if(!tempFile.exists())
            {
                tempFile.mkdirs();
            }
            File scriptFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Utils.getApplicationName(context) + File.separator + OPGSDKConstant.SCRIPTS + File.separator +surveyID+OPGSDKConstant.OPGS);
            if(scriptFile.exists())
            {
                scriptFile.delete();
            }
            scriptFile.createNewFile();
            byte[] byteCodeArray = Aes256.Base64ConvertedArray(data);
            ByteArrayInputStream bis = new ByteArrayInputStream(byteCodeArray);
            FileOutputStream fos = new FileOutputStream(scriptFile);

            byte[] buf = new byte[1024];
            for (int readNum; (readNum = bis.read(buf)) != -1;)
            {
                fos.write(buf, 0, readNum);
            }
            fos.close();
            bis.close();

            //FileWriter fileWriter = new FileWriter(scriptFile);
            //fileWriter.write(data);
            //fileWriter.close();
            tmpDir = scriptFile.getAbsolutePath();
        }
        catch(Exception exception)
        {
            tmpDir = OPGSDKConstant.EXCEPTION_OCCURRED+exception.getMessage();
            exception.printStackTrace();
        }
        return tmpDir;
    }*/

    /**
     *To parse the response for array of OPGSurvey object
     * @param  surveyListResponse
     * @return List<OPGSurvey>
     * @throws JSONException
     */
    @SuppressWarnings("unchecked")//explanation for @SuppressWarnings - http://stackoverflow.com/questions/14642985/type-safety-unchecked-cast-from-object-to-listmyobject
    public static List<OPGSurvey> parseSurveyList(String surveyListResponse) throws JSONException, OPGException
    {
        List<OPGSurvey> surveyList = null;
        if (surveyListResponse != null && !surveyListResponse.isEmpty())
        {
            if(surveyListResponse.startsWith(OPGSDKConstant.OPEN_SQUARE_BRACKET))
            {
                Gson gson = getGsonInstance();
                Type listType = new TypeToken<List<OPGSurvey>>()
                {
                }.getType();
                surveyList =  gson.fromJson(surveyListResponse, listType);
            }
            else
            {
                JSONObject jsonObject = new JSONObject(surveyListResponse);
                if(jsonObject.has(OPGSDKConstant.ERROR_MESSAGE))
                {
                    throw  new OPGException(jsonObject.getString(OPGSDKConstant.ERROR_MESSAGE));
                }
            }
        }
        else
        {
            throw new OPGException(OPGSDKConstant.ERROR_THEME);
        }
        return surveyList;
    }


    /**
     * To parse the response for array of OPGGeofenceSurveys List object
     * @param geofenceSurveyListResponse
     * @return
     * @throws JSONException
     * @throws OPGException
     */
    public static List<OPGGeofenceSurvey> parseGeofenceSurveyList(String geofenceSurveyListResponse) throws JSONException, OPGException
    {
        List<OPGGeofenceSurvey> geofenceSurveyList = null;
        if (geofenceSurveyListResponse != null && !geofenceSurveyListResponse.isEmpty())
        {
            if(geofenceSurveyListResponse.startsWith(OPGSDKConstant.OPEN_SQUARE_BRACKET))
            {
                Gson gson = getGsonInstance();
                Type listType = new TypeToken<List<OPGGeofenceSurvey>>()
                {
                }.getType();
                geofenceSurveyList =  gson.fromJson(geofenceSurveyListResponse, listType);
            }
            else
            {
                JSONObject jsonObject = new JSONObject(geofenceSurveyListResponse);
                if(jsonObject.has(OPGSDKConstant.ERROR_MESSAGE))
                {
                    throw  new OPGException(jsonObject.getString(OPGSDKConstant.ERROR_MESSAGE));
                }
            }
        }
        else
        {
            throw new OPGException(OPGSDKConstant.ERROR_NULL_RESPONSE);
        }
        return geofenceSurveyList;
    }


    /**
     * To parse the response and get the list of OPGCountry
     * @param countryListResponse
     * @return
     * @throws OPGException
     * @throws JSONException
     */
    public static List<OPGCountry> parseCountryList(String countryListResponse) throws OPGException, JSONException {
        List<OPGCountry> opgCountryList = null;
        if(countryListResponse !=null && !countryListResponse.isEmpty()){
            if(countryListResponse.startsWith(OPGSDKConstant.OPEN_SQUARE_BRACKET))
            {
                Gson gson = getGsonInstance();
                Type listType = new TypeToken<List<OPGCountry>>()
                {
                }.getType();
                opgCountryList =  gson.fromJson(countryListResponse, listType);
            }else{
                JSONObject jsonObject = new JSONObject(countryListResponse);
                if(jsonObject.has(OPGSDKConstant.ERROR_MESSAGE))
                {
                    throw  new OPGException(jsonObject.getString(OPGSDKConstant.ERROR_MESSAGE));
                }
            }
        }
        else
        {
            throw new OPGException(OPGSDKConstant.ERROR_NULL_RESPONSE);
        }

        return opgCountryList;
    }

    /**
     *To parse the response  for array of  OPGTheme object
     * @param themeResponse
     * @return List<OPGTheme>
     * @throws JSONException
     */
    @SuppressWarnings("unchecked")
    public static List<OPGTheme> parseThemeList(String themeResponse) throws JSONException, OPGException
    {
        List<OPGTheme> themeList = new ArrayList<OPGTheme>();
        if (themeResponse != null && !themeResponse.isEmpty())
        {
            JSONObject jsonObject = new JSONObject(themeResponse);
            if (!jsonObject.has(OPGSDKConstant.ERROR_MESSAGE))
            {
                JSONArray jsonArray = jsonObject.getJSONArray(OPGSDKConstant.THEMES);
                Gson gson =getGsonInstance();
                Type listType = new TypeToken<List<OPGTheme>>()
                {
                }.getType();
                for(int i = 0;i<jsonArray.length() ; i++)
                {
                    JSONArray themeArray = jsonArray.getJSONArray(i);
                    themeList.addAll((List<OPGTheme>)gson.fromJson(themeArray.toString(), listType));
                }

               /* JSONArray themeArray0 = jsonArray.getJSONArray(0);
                Gson gson =getGsonInstance();
                Type listType = new TypeToken<List<OPGTheme>>()
                {
                }.getType();
                themeList =  gson.fromJson(themeArray0.toString(), listType);

                if(jsonArray.length() > 1)
                {
                    JSONArray themeArray1 = jsonArray.getJSONArray(1);
                    List<OPGTheme> opgThemes2 = gson.fromJson(themeArray1.toString(), listType);
                    themeList.addAll(opgThemes2);
                }*/
            }
            else
            {
                throw new OPGException(jsonObject.getString(OPGSDKConstant.ERROR_MESSAGE));
            }
        }
        return themeList;
    }

    /**
     *To parse the response for OPGPanel object
     * @param panelsResponse
     * @return  List<OPGPanel>
     * @throws JSONException
     */
    @SuppressWarnings("unchecked")
    public static List<OPGPanel> parsePanelList(String panelsResponse) throws JSONException, OPGException
    {
        List<OPGPanel> surveyList = new ArrayList<OPGPanel>();
        if (panelsResponse != null && !panelsResponse.isEmpty())
        {
            JSONObject jsonObject = new JSONObject(panelsResponse);
            if (!jsonObject.has(OPGSDKConstant.ERROR_MESSAGE))
            {
                Gson gson = getGsonInstance();
                JSONArray jsonArray = jsonObject.getJSONArray(OPGSDKConstant.PANELS);
                Type listType = new TypeToken<List<OPGPanel>>()
                {
                }.getType();
                surveyList = gson.fromJson(jsonArray.toString(), listType);
            }
            else
            {
                throw new OPGException(jsonObject.getString(OPGSDKConstant.ERROR_MESSAGE));
            }
        }
        return surveyList;
    }

    public static List<OPGPanelPanellist> parsePanelPanellist(String panelPanellistJsonResponse) throws JSONException, OPGException
    {
        List<OPGPanelPanellist> panelPanellistList = new ArrayList<OPGPanelPanellist>();
        if (panelPanellistJsonResponse != null && !panelPanellistJsonResponse.isEmpty())
        {
            JSONObject jsonObject = new JSONObject(panelPanellistJsonResponse);
            if (!jsonObject.has(OPGSDKConstant.ERROR_MESSAGE))
            {
                Gson gson = getGsonInstance();
                JSONArray jsonArray = jsonObject.getJSONArray(OPGSDKConstant.PANEL_PANELLIST);
                Type listType = new TypeToken<List<OPGPanelPanellist>>()
                {
                }.getType();
                panelPanellistList =  gson.fromJson(jsonArray.toString(), listType);
            }
            else
            {
                throw new OPGException(jsonObject.getString(OPGSDKConstant.ERROR_MESSAGE));
            }
        }
        return panelPanellistList;
    }

    /**
     *To parse the response for array of OPGSurveyPanel
     * @param panelsResponse
     * @return List<OPGSurveyPanel>
     * @throws JSONException
     */
    @SuppressWarnings("unchecked")
    public static List<OPGSurveyPanel> parseSurveyPanel(String panelsResponse) throws JSONException, OPGException
    {
        List<OPGSurveyPanel> surveyPanelList = new ArrayList<OPGSurveyPanel>();
        if (panelsResponse != null && !panelsResponse.isEmpty())
        {
            JSONObject jsonObject = new JSONObject(panelsResponse);
            if (!jsonObject.has(OPGSDKConstant.ERROR_MESSAGE))
            {
                Gson gson = getGsonInstance();
                JSONArray jsonArray = jsonObject.getJSONArray(OPGSDKConstant.SURVEYPANEL);
                Type listType = new TypeToken<List<OPGSurveyPanel>>()
                {
                }.getType();
                surveyPanelList = (List<OPGSurveyPanel>) gson.fromJson(jsonArray.toString(), listType);
            }
            else
            {
                throw new OPGException(jsonObject.getString(OPGSDKConstant.ERROR_MESSAGE));
            }
        }
        return surveyPanelList;
    }

    /**
     *To parse the response of MediaUpload
     * @param context
     * @param response
     * @return mediaId
     * @throws OPGException
     * @throws JSONException
     */
    public static String parseMediaUpload(Context context,String response) throws OPGException, JSONException {
        String mediaId = null;
        if(response!=null && !response.isEmpty())
        {
            if(response.startsWith(OPGSDKConstant.OPEN_CURLY_BRACKET))
            {
                JSONObject jsonObject = new JSONObject(response);
                StringBuilder builder = new StringBuilder();
                if(jsonObject.has(OPGSDKConstant.HTTP_STATUS_CODE))
                    builder.append(NEW_LINE+OPGSDKConstant.HTTP_STATUS_CODE+COLON).append(jsonObject.getString(OPGSDKConstant.HTTP_STATUS_CODE));
                builder.append(OPGSDKConstant.CAUSED_BY);
                if(jsonObject.has(OPGSDKConstant.ERROR_MESSAGE))
                    if(jsonObject.has(OPGSDKConstant.ERROR_MESSAGE))
                        builder.append(NEW_LINE+OPGSDKConstant.ERROR_MESSAGE+COLON).append(jsonObject.getString(OPGSDKConstant.ERROR_MESSAGE));
                    else
                        builder.append(NEW_LINE+OPGSDKConstant.ERROR_MESSAGE+COLON).append(jsonObject.getString(OPGSDKConstant.MESSAGE));

                throw new OPGException(builder.toString());
            }else if(response.startsWith(OPGSDKConstant.OPEN_SQUARE_BRACKET)){
                JSONArray array = new JSONArray(response);
                if(array.length()>0){
                    mediaId = array.getString(0);
                }
            }else{
                throw new OPGException(OPGSDKConstant.ERROR_UPLOAD_FILE);
            }
        }else{
            throw new OPGException(OPGSDKConstant.ERROR_UPLOAD_FILE);
        }
        return  mediaId;
    }

    public static OPGPanellistPanel parsePanellistPanel(String jsonResponse) throws OPGException,JSONException
    {
        OPGPanellistPanel opgPanellistPanel = new OPGPanellistPanel();
        try {
            if (jsonResponse != null && !jsonResponse.isEmpty())
            {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                if (!jsonObject.has(OPGSDKConstant.ERROR_MESSAGE))
                {

                    opgPanellistPanel.setPanelPanellistArray(parsePanelPanellist(jsonResponse));
                    opgPanellistPanel.setPanelArray(parsePanelList(jsonResponse));
                    opgPanellistPanel.setThemeArray(parseThemeList(jsonResponse));
                    opgPanellistPanel.setSurveyPanelArray(parseSurveyPanel(jsonResponse));

                    opgPanellistPanel.setSuccess(true);
                    opgPanellistPanel.setStatusMessage(OPGSDKConstant.SUCCESS);
                }
                else
                {
                    opgPanellistPanel.setSuccess(false);
                    opgPanellistPanel.setStatusMessage(jsonObject.getString(OPGSDKConstant.ERROR_MESSAGE));
                }
            }
        }catch (Exception ex )
        {
            if (BuildConfig.DEBUG) {
                Log.i(OPGParseResult.class.getName(), ex.getMessage());
            }
            throw ex;
        }
        return opgPanellistPanel;

    }

    public static boolean parseNotificationResponse(Context context, String notificationResponse) throws JSONException
    {
       boolean status = false;
        if (notificationResponse != null || !notificationResponse.isEmpty())
        {
            JSONObject opgJsonResponse = new JSONObject(notificationResponse);
            if(200 == opgJsonResponse.getInt(OPGSDKConstant.HTTP_STATUS_CODE))
            {
               status = true;
            }
        }

        return status;
    }

    public static boolean parseUploadResult(Context context,String uploadResponse) throws Exception{
        if(uploadResponse!=null && !uploadResponse.isEmpty()){
            JSONObject opguploadResponse = new JSONObject(uploadResponse);
            if(200 == opguploadResponse.getInt(OPGSDKConstant.HTTP_STATUS_CODE))
            {
                return true;
            }else{
                if(opguploadResponse.has(OPGSDKConstant.ERROR_MESSAGE)){
                    throw  new OPGException(opguploadResponse.getString(OPGSDKConstant.ERROR_MESSAGE));
                }else{
                    throw  new OPGException(OPGSDKConstant.ERROR_UPLOAD_FILE);
                }
            }
        }else{
            throw  new OPGException(OPGSDKConstant.ERROR_UPLOAD_FILE);
        }
    }

}
