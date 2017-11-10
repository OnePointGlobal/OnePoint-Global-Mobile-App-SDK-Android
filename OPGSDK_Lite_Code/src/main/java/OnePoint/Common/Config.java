package OnePoint.Common;

import com.allatori.annotations.StringEncryption;
import com.allatori.annotations.StringEncryptionType;

import java.util.Date;

import static com.opg.sdk.OPGSDKConstant.HYPHEN;

@StringEncryptionType(StringEncryption.MAXIMUM)
public class Config {

//	public static final String TAKE_SURVEY_URL = "http://apidev.1pt.mobi/i/interview"; // DEV
//	public static final String TAKE_SURVEY_URL = "http://api.1pt.mobi/i/interview"; // LIVE?
//	public static final String TAKE_SURVEY_URL = "http://apistaging.1pt.mobi/i/interview"; //APPSTAGING
	
//	public static final String TAKE_SURVEY_URL ="http://apidev.1pt.mobi/V2.0Vitaccess/interview/"; // Vitaccess dev
//	public static final String TAKE_SURVEY_URL ="http://apistaging.1pt.mobi/V2.0Vitaccess/interview/"; // Vitaccess staging

//	public static final String DOMAIN = "http://apidev.1pt.mobi/V2.0/api"; // DEV New
//	public static final String DOMAIN = "http://api.1pt.mobi/V2.0/api/"; // LIVE
//	public static final String DOMAIN = "http://apistaging.1pt.mobi/V2.0/api/"; // APPSTAGING
	
//	public static final String DOMAIN ="http://apidev.1pt.mobi/V2.0Vitaccess/api/"; // Vitaccess dev
//	public static final String DOMAIN ="http://apistaging.1pt.mobi/V2.0Vitaccess/api/"; // Vitaccess staging
	
	
	//public static final String TAKE_SURVEY_URL =""/*"https://api.vitaccess.com/ivitaccess/interview/"*/; // Vitaccess live
	//public static final String DOMAIN =""/*"https://api.vitaccess.com/V2.0Vitaccess/api/"*/; // Vitaccess live

//	public static final String RESOURCE_LOGIN = "Login";
	
	//public static final String OTA_APPVERSION="VitAccessApp-ANDROID-1.0.0-O"; // New
//  String versionDEV="Mysurvey-ANDROID-1.2.1-O"; //Old
	//public static final String PLAYSTORE_APPVERSION="VitAccessApp-ANDROID-1.0.0-A"; //New

 
	/** CORDOVA ACTION STRINGS */
	public static final String CORDOVA_ACTION_LOGIN = "ActionLogin";

	public static final boolean CHECK_FOR_UPDATES_FULL_REPLACEMENT = true;

	public static String getUTCDate(Date date) {
		int day = date.getDate();
		int month = date.getMonth() + 1;
		int year = date.getYear() + 1900;
		return day + HYPHEN + month + HYPHEN + year;
	}
}
