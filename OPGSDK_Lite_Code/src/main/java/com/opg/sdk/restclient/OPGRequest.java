package com.opg.sdk.restclient;

import com.google.gson.Gson;
import com.opg.sdk.OPGSDKConstant;
import com.opg.sdk.models.OPGPanellistProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class OPGRequest {

	/**
	 * Encodes the given string using MD5 alogrithm
	 * @param input
	 * @return
	 */
	public static String getMd5Hash(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance(OPGSDKConstant.MD5);
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String md5 = number.toString(16);

			while (md5.length() < 32)
				md5 = OPGSDKConstant.ZERO + md5;

			return md5;
		} catch (NoSuchAlgorithmException e) {
			//Log.e("MD5", e.getLocalizedMessage());
			return null;
		}
	}

	/**
	 * Creating a Json object from the authenicate parameters
	 * @param userName
	 * @param password
	 * @param appVersion
	 * @param signInTimeUTC
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getAuthenticateEntity(String userName, String password, String appVersion,String signInTimeUTC) throws JSONException {
		JSONObject authEntity = new JSONObject();
		authEntity.put(OPGSDKConstant.USERNAME, userName);
		authEntity.put(OPGSDKConstant.PASSWORD, getMd5Hash(password));
		authEntity.put(OPGSDKConstant.APP_VERSION, appVersion);
		authEntity.put(OPGSDKConstant.SIGNIN_TIME_UTC, signInTimeUTC);
		return authEntity;
	}

	/**
	 * Creating a Json object from the google authenicate parameters
	 * @param googleTokenID
	 * @param appVersion
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getGoogleAuthEntity(String googleTokenID,String appVersion) throws JSONException {
		JSONObject authEntity = new JSONObject();
		authEntity.put(OPGSDKConstant.GoogleToken, googleTokenID);
		authEntity.put(OPGSDKConstant.APP_VERSION, appVersion);
		return authEntity;
	}

	/**
	 * Creating a Json object from the facebook authenicate parameters
	 * @param facebookTokenID
	 * @param appVersion
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getFacebookAuthEntity(String facebookTokenID,String appVersion) throws JSONException {
		JSONObject authEntity = new JSONObject();
		authEntity.put(OPGSDKConstant.FacebookToken, facebookTokenID);
		authEntity.put(OPGSDKConstant.APP_VERSION, appVersion);
		return authEntity;
	}
	/**
	 *  Creating a Json object from the forgotPassword parameters
	 * @param emailID
	 * @param appVersion
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getForgotPasswordEntity(String emailID,String appVersion) throws JSONException {
		JSONObject forPasswordEntity = new JSONObject();
		forPasswordEntity.put(OPGSDKConstant.EMAIL_ID, emailID);
		forPasswordEntity.put(OPGSDKConstant.APP_VERSION, appVersion);
		return forPasswordEntity;
	}


	/**
	 *
	 * @param uniqueID
	 * @param currentPassword
	 * @param newPassword
	 * @return
	 * @throws JSONException
     */
	public static JSONObject getChangePasswordEntity(String uniqueID, String currentPassword, String newPassword) throws JSONException
	{
		JSONObject changePwdEntity = new JSONObject();
		changePwdEntity.put(OPGSDKConstant.SESSIONID, uniqueID);
		changePwdEntity.put(OPGSDKConstant.CURRENT_PASSWORD, getMd5Hash(currentPassword));
		changePwdEntity.put(OPGSDKConstant.NEW_PASSWORD, getMd5Hash(newPassword));
		return changePwdEntity;
	}

	/**
	 *
	 * @param uniqueID
	 * @return
	 * @throws JSONException
     */
	public static JSONObject getSurveyListEntity(String uniqueID) throws JSONException
	{
		JSONObject getSurveyEntity = new JSONObject();
		getSurveyEntity.put(OPGSDKConstant.SESSIONID, uniqueID);
		return getSurveyEntity;
	}

	/**
	 *
	 * @param uniqueID
	 * @return
	 * @throws JSONException
     */
	public static JSONObject getCountryListEntity(String uniqueID) throws JSONException
	{
		JSONObject getSurveyEntity = new JSONObject();
		getSurveyEntity.put(OPGSDKConstant.SESSIONID, uniqueID);
		return getSurveyEntity;
	}

	/**
	 *
	 * @param uniqueID
	 * @param panelID
	 * @return
	 * @throws JSONException
     */
	public static JSONObject getSurveyListEntityForPanelID(String uniqueID,String panelID) throws JSONException
	{
		JSONObject getSurveyEntity = new JSONObject();
		getSurveyEntity.put(OPGSDKConstant.SESSIONID, uniqueID);
		getSurveyEntity.put(OPGSDKConstant.PANEL_ID,panelID);
		return getSurveyEntity;

	}

	/**
	 *
	 * @param uniqueID
	 * @param latitude
	 * @param longitude
	 * @return
	 * @throws JSONException
     */

	public static JSONObject getGeofenceSurveyListEntity(String uniqueID,float latitude ,float longitude ) throws JSONException
	{
		JSONObject getGeofenceSurveysEntity = new JSONObject();
		getGeofenceSurveysEntity.put(OPGSDKConstant.SESSIONID, uniqueID);
		getGeofenceSurveysEntity.put(OPGSDKConstant.LATITUDE,latitude);
		getGeofenceSurveysEntity.put(OPGSDKConstant.LONGITUDE,longitude);
		return getGeofenceSurveysEntity;
	}

	/**
	 *
	 * @param uniqueID
	 * @return
	 * @throws JSONException
     */
	public static JSONObject getPanelistProfileEntity(String uniqueID) throws JSONException
	{
		JSONObject panellistProfileEntity = new JSONObject();
		panellistProfileEntity.put(OPGSDKConstant.SESSIONID, uniqueID);
		return panellistProfileEntity;
	}

	/**
	 *
	 * @param uniqueID
	 * @return
	 * @throws JSONException
     */
	public static JSONObject getPanellistPanelEntity(String uniqueID) throws JSONException
	{
		JSONObject panellistPanelEntity = new JSONObject();
		panellistPanelEntity.put(OPGSDKConstant.SESSIONID, uniqueID);
		return panellistPanelEntity;
	}

	/**
	 *
	 * @param uniqueID
	 * @return
	 * @throws JSONException
     */
	public static JSONObject getPanelPanelistEntity(String uniqueID) throws JSONException
	{
		JSONObject panelPanelistEntity = new JSONObject();
		panelPanelistEntity.put(OPGSDKConstant.SESSIONID, uniqueID);
		return panelPanelistEntity;
	}

	/**
	 *
	 * @param uniqueID
	 * @param profile
	 * @return
	 * @throws JSONException
     */
	public static JSONObject getUpdatePanellistProfileEntity(String uniqueID,OPGPanellistProfile profile) throws JSONException
	{
		Gson gson = new Gson();
		String jsonResponse = gson.toJson(profile);
		JSONObject updateProfile = new JSONObject(jsonResponse);
		updateProfile.put(OPGSDKConstant.SESSIONID, uniqueID);
		updateProfile.put(OPGSDKConstant.COUNTRY_CODE,profile.getStd());//we are mapping the std vale to countrycode.
		return updateProfile;
	}

	/**
	 *
	 * @param uniqueID
	 * @param surveyID
	 * @return
	 * @throws JSONException
     */
	public static JSONObject getSurveyScriptEntity(String uniqueID,String surveyID) throws JSONException
	{
		JSONObject surveyScriptEntity = new JSONObject();
		surveyScriptEntity.put(OPGSDKConstant.SESSIONID, uniqueID);
		surveyScriptEntity.put(OPGSDKConstant.SURVEY_REF, surveyID);
		return surveyScriptEntity;
	}

	/**
	 *
	 * @param uniqueID
	 * @param deviceToken
	 * @param appVersion
	 * @param deviceID
	 * @return
     * @throws JSONException
     */

	public static JSONObject getNotificationEntity(String uniqueID,String deviceToken,String appVersion,String deviceID) throws JSONException
	{
		JSONObject surveyScriptEntity = new JSONObject();
		surveyScriptEntity.put(OPGSDKConstant.SESSIONID, uniqueID);
		surveyScriptEntity.put(OPGSDKConstant.DEVICE_TOKEN_ID,deviceToken);
		surveyScriptEntity.put(OPGSDKConstant.PLATFORM, OPGSDKConstant.TWO); //(ex: for IOS =1 , android = 2).
		surveyScriptEntity.put(OPGSDKConstant.VERSION, appVersion);
		surveyScriptEntity.put(OPGSDKConstant.DEVICE_ID, deviceID);
		return surveyScriptEntity;
	}

}
